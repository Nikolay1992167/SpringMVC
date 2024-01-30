package ru.clevertec.house.proxy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ru.clevertec.house.cache.Cache;
import ru.clevertec.house.cache.factory.CacheFactory;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.mapper.PersonMapper;
import ru.clevertec.house.repository.PersonRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Aspect
@Component
@ConditionalOnBean(CacheFactory.class)
public class CacheAspectPerson {

    private final Cache<Object, Object> cache;

    private final PersonRepository personRepository;

    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);

    public CacheAspectPerson(CacheFactory factoryCache, PersonRepository personRepository) {
        this.cache = factoryCache.getCacheAlgorithm();
        this.personRepository = personRepository;
    }

    @Around("@annotation(ru.clevertec.house.proxy.Cache) && execution(* ru.clevertec.house.service.impl.PersonServiceImpl.findById(..))")
    public Object cacheGet(ProceedingJoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        UUID uuid = (UUID) args[0];
        if (cache.get(uuid) != null) {
            return cache.get(uuid);
        }
        PersonResponse result;
        try {
            result = (PersonResponse) joinPoint.proceed();
        } catch (NotFoundException exception) {
            throw NotFoundException.of(Person.class, uuid);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        cache.put(uuid, result);
        return result;
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.house.proxy.Cache) && execution(* ru.clevertec.house.service.impl.PersonServiceImpl.save(..))", returning = "uuid")
    public void cacheCreate(UUID uuid) {

        Optional<Person> optionalPerson = personRepository.findPersonByUuid(uuid);
        optionalPerson.ifPresent(person -> cache.put(uuid, personMapper.toResponse(person)));
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.house.proxy.Cache) && execution(* ru.clevertec.house.service.impl.PersonServiceImpl.update(..)) && args(uuid, personRequest)", argNames = "uuid, personRequest")
    public void cacheUpdate(UUID uuid, PersonRequest personRequest) {

        Person person = personRepository.findPersonByUuid(uuid).orElseThrow(() -> NotFoundException.of(Person.class, uuid));
        cache.put(uuid, personMapper.toResponse(person));
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.house.proxy.Cache) && execution(* ru.clevertec.house.service.impl.PersonServiceImpl.patchUpdate(..)) && args(uuid, fields)", argNames = "uuid, fields")
    public void cachePatchUpdate(UUID uuid, Map<String, Object> fields) {

        Person person = personRepository.findPersonByUuid(uuid).orElseThrow(() -> NotFoundException.of(Person.class, uuid));
        cache.put(uuid, personMapper.toResponse(person));
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.house.proxy.Cache) && execution(* ru.clevertec.house.service.impl.PersonServiceImpl.delete(..)) && args(uuid)", argNames = "uuid")
    public void cacheDelete(UUID uuid) {

        cache.remove(uuid);
    }
}
