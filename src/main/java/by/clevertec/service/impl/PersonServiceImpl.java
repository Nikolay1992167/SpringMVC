package by.clevertec.service.impl;

import by.clevertec.dao.PersonDao;
import by.clevertec.dto.request.PersonRequest;
import by.clevertec.dto.response.PersonResponse;
import by.clevertec.entity.Person;
import by.clevertec.exception.NotFoundException;
import by.clevertec.mapper.PersonMapper;
import by.clevertec.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonDao personDao;

    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);

    @Override
    public PersonResponse findById(UUID uuid){
        PersonResponse personResponse = personDao.findById(uuid)
                .map(personMapper::toResponse)
                .orElseThrow(()-> NotFoundException.of(Person.class, uuid));
        log.info("House method finById {}", personResponse);
        return personResponse;
    }

    @Override
    public List<PersonResponse> findAll(int pageNumber, int pageSize) {

        return null;
    }

    @Override
    public PersonResponse save(PersonRequest personRequest) {
        return null;
    }

    @Override
    public PersonResponse update(UUID uuid, PersonRequest personRequest) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }
}
