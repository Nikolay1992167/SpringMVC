package ru.clevertec.house.proxy;

import by.clevertec.exception.NotFoundException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ru.clevertec.house.cache.Cache;
import ru.clevertec.house.cache.factory.CacheFactory;
import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.mapper.HouseMapper;
import ru.clevertec.house.repository.HouseRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Aspect
@Component
@ConditionalOnBean(CacheFactory.class)
public class CacheAspectHouse {

    private final Cache<Object, Object> cache;

    private final HouseRepository houseRepository;

    private final HouseMapper houseMapper = Mappers.getMapper(HouseMapper.class);

    public CacheAspectHouse(CacheFactory factoryCache, HouseRepository houseRepository) {
        this.cache = factoryCache.getCacheAlgorithm();
        this.houseRepository = houseRepository;
    }

    @Around("@annotation(ru.clevertec.house.proxy.Cache) && execution(* ru.clevertec.house.service.impl.HouseServiceImpl.findById(..))")
    public Object cacheGet(ProceedingJoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        UUID uuid = (UUID) args[0];
        if (cache.get(uuid) != null) {
            return cache.get(uuid);
        }
        HouseResponse result;
        try {
            result = (HouseResponse) joinPoint.proceed();
        } catch (NotFoundException exception) {
            throw NotFoundException.of(House.class, uuid);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        cache.put(uuid, result);
        return result;
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.house.proxy.Cache) && execution(* ru.clevertec.house.service.impl.HouseServiceImpl.save(..))", returning = "uuid")
    public void cacheCreate(UUID uuid) {

        Optional<House> optionalHouse = houseRepository.findHouseByUuid(uuid);
        optionalHouse.ifPresent(house -> cache.put(uuid, houseMapper.toResponse(house)));
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.house.proxy.Cache) && execution(* ru.clevertec.house.service.impl.HouseServiceImpl.update(..)) && args(uuid, houseRequest)", argNames = "uuid, houseRequest")
    public void cacheUpdate(UUID uuid, HouseRequest houseRequest) {

        House house = houseRepository.findHouseByUuid(uuid).orElseThrow(() -> NotFoundException.of(House.class, uuid));
        cache.put(uuid, houseMapper.toResponse(house));
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.house.proxy.Cache) && execution(* ru.clevertec.house.service.impl.HouseServiceImpl.patchUpdate(..)) && args(uuid, fields)", argNames = "uuid, fields")
    public void cachePatchUpdate(UUID uuid, Map<String, Object> fields) {

        House house = houseRepository.findHouseByUuid(uuid).orElseThrow(() -> NotFoundException.of(House.class, uuid));
        cache.put(uuid, houseMapper.toResponse(house));
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.house.proxy.Cache) && execution(* ru.clevertec.house.service.impl.HouseServiceImpl.delete(..)) && args(uuid)", argNames = "uuid")
    public void cacheDelete(UUID uuid) {

        cache.remove(uuid);
    }
}
