package ru.clevertec.house.service.impl;

import ru.clevertec.house.dao.HouseDao;
import ru.clevertec.house.dao.PersonDao;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.mapper.PersonMapper;
import ru.clevertec.house.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonDao personDao;

    private final HouseDao houseDao;

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

        List<PersonResponse> persons = personDao.findAll(pageNumber, pageSize)
                .stream()
                .map(personMapper::toResponse)
                .toList();
        log.info("Person method findAll {}", persons.size());

        return new ArrayList<>(persons);
    }

    @Override
    public PersonResponse save(PersonRequest personRequest) {

        House house = getHouse(personRequest);

        Person personToSave = personMapper.toPerson(personRequest);
        personToSave.setHouse(house);
        Person saved = personDao.save(personToSave);
        PersonResponse response = personMapper.toResponse(saved);
        log.info("Person method save {}", response);

        return response;
    }

    @Override
    public PersonResponse update(UUID uuid, PersonRequest personRequest) {

        House house = getHouse(personRequest);

        Person personToUpdate = personMapper.toPerson(personRequest);

        Person personInDB = personDao.findById(uuid)
                .orElseThrow(()->NotFoundException.of(Person.class, uuid));

        personToUpdate.setId(personInDB.getId());
        personToUpdate.setUuid(uuid);
        personToUpdate.setHouse(house);
        personToUpdate.setCreateDate(personInDB.getCreateDate());

        Person updated = personDao.update(personToUpdate);
        PersonResponse response = personMapper.toResponse(updated);
        log.info("Person method update {}", response);

        return response;
    }

    @Override
    public void delete(UUID uuid) {

        Person person = personDao.delete(uuid)
                .orElseThrow(()->NotFoundException.of(Person.class, uuid));
        log.info("Person method delete {}", person);
    }

    private House getHouse(PersonRequest personRequest) {
        return houseDao.findById(personRequest.getHouseUUID()).get();
    }
}
