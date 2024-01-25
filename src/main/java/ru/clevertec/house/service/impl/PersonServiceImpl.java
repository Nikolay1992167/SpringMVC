package ru.clevertec.house.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.enums.Sex;
import ru.clevertec.house.enums.TypePerson;
import ru.clevertec.house.exception.CheckEmptyException;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.mapper.PersonMapper;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.service.PersonService;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    private final HouseRepository houseRepository;

    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);

    /**
     * Finds one {@link PersonResponse} by UUID.
     *
     * @param uuid the field uuid of the {@link Person}.
     * @return PersonResponse with the specified UUID and mapped from Person entity.
     * @throws NotFoundException if Person is not exists by finding it by UUID.
     */
    @Override
    public PersonResponse findById(UUID uuid) {

        PersonResponse personResponse = personRepository.findPersonByUuid(uuid)
                .map(personMapper::toResponse)
                .orElseThrow(() -> NotFoundException.of(Person.class, uuid));

        log.info("PersonService method finById {}", personResponse);

        return personResponse;
    }

    /**
     * Finds all {@link PersonResponse}.
     *
     * @param pageable the {@link Pageable} which will be parameters for pagination.
     * @return mapped from entity to dto list of all PersonResponse.
     */
    @Override
    public Page<PersonResponse> findAll(Pageable pageable) {

        Page<PersonResponse> responses = personRepository.findAll(pageable)
                .map(personMapper::toResponse);

        log.info("PersonService method findAll {}", responses.stream().count());

        return responses;
    }

    /**
     * Finds and returns a list of {@link PersonResponse} objects that represent persons lives by a specific house.
     *
     * @param houseId  the UUID of the house to find persons for.
     * @param pageable the {@link Pageable} which will be parameters for pagination.
     * @return a list of {@link PersonResponse} objects, each representing a person lived by the house.
     * The list is empty if no persons are found.
     */
    @Override
    public Page<PersonResponse> findPersonsWhichLiveInHouse(UUID houseId, Pageable pageable) {

        Page<PersonResponse> response = personRepository.findAllByHouseUuid(houseId, pageable)
                .map(personMapper::toResponse);

        log.info("PersonService method findPersonsWhichLiveInHouse {}", response.stream().count());

        return response;
    }

    /**
     * Finds and returns a list of {@link PersonResponse} objects that represent persons some time lives by a specific house.
     *
     * @param houseId  the UUID of the house to find persons for.
     * @param pageable the {@link Pageable} which will be parameters for pagination.
     * @return a list of {@link PersonResponse} objects, each representing a person lived by the house.
     * The list is empty if no persons are found.
     */
    @Override
    public Page<PersonResponse> findPersonsWhichSomeTimeLiveInHouse(UUID houseId, Pageable pageable) {

        TypePerson typePerson = TypePerson.TENANT;

        Page<PersonResponse> responses = personRepository.findByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType(houseId, typePerson, pageable)
                .map(personMapper::toResponse);

        log.info("PersonService method findPersonsWhichSomeTimeLiveInHouse {}", responses.stream().count());

        return responses;
    }

    /**
     * Finds and returns a list of {@link PersonResponse} objects that represent persons some time owns by a specific house.
     *
     * @param houseId  the UUID of the house to find persons for.
     * @param pageable the {@link Pageable} which will be parameters for pagination.
     * @return a list of {@link PersonResponse} objects, each representing a person owned by the house.
     * The list is empty if no persons are found.
     */
    @Override
    public Page<PersonResponse> findPersonsWhichSomeTimeOwnHouse(UUID houseId, Pageable pageable) {

        TypePerson typePerson = TypePerson.OWNER;

        Page<PersonResponse> responses = personRepository.findByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType(houseId, typePerson, pageable)
                .map(personMapper::toResponse);

        log.info("PersonService method findPersonsWhichSomeTimeOwnHouse {}", responses.stream().count());

        return responses;
    }

    /**
     * Performs a full-text search to find persons based on a search term.
     *
     * @param searchTerm the term to search for in any text field of the persons.
     * @param pageable   the {@link Pageable} which will be parameters for pagination.
     * @return a list of {@link PersonResponse} objects, each representing a person that matches the search term.
     * The list is empty if no matches are found.
     */
    @Override
    public Page<PersonResponse> findPersonsFullTextSearch(String searchTerm, Pageable pageable) {

        Page<PersonResponse> responses = personRepository.findPersonsFullTextSearch(searchTerm, pageable)
                .map(personMapper::toResponse);

        log.info("PersonService method findPersonsFullTextSearch {}", responses.stream().count());

        return responses;
    }

    /**
     * Saves one {@link Person}.
     *
     * @param personRequest the {@link PersonRequest} which will be mapped to Person
     *                      and saved in database by dao.
     * @return the saved {@link PersonResponse} which was mapped from Person entity.
     */
    @Override
    @Transactional
    public PersonResponse save(PersonRequest personRequest) {

        House house = getHouse(personRequest);

        Person personToSave = personMapper.toPerson(personRequest);
        personToSave.setHouse(house);

        List<House> ownedHouses = personToSave.getOwnedHouses();

        List<UUID> houseUuids = personRequest.getOwnedHouses().stream()
                .map(House::getUuid)
                .collect(Collectors.toList());

        List<House> existingHouses = houseRepository.findAllByUuidIn(houseUuids);

        Set<UUID> existingHouseUuids = existingHouses.stream()
                .map(House::getUuid)
                .collect(Collectors.toSet());

        List<House> housesToCreate = new ArrayList<>();

        for (House houseOwn : ownedHouses) {

            if (!existingHouseUuids.contains(houseOwn.getUuid())) {
                housesToCreate.add(houseOwn);
            }
        }

        List<House> createdHouses = houseRepository.saveAll(housesToCreate);

        List<House> checkOwnedHouse = Stream.concat(existingHouses.stream(), createdHouses.stream())
                .collect(Collectors.toList());

        personToSave.setOwnedHouses(checkOwnedHouse);

        Person saved = personRepository.save(personToSave);
        PersonResponse response = personMapper.toResponse(saved);
        log.info("PersonService method save {}", response);

        return response;
    }

    /**
     * Updates one {@link Person}.
     *
     * @param personRequest the {@link PersonRequest} which will be mapped to Person and
     *                      updated in database by dao.
     * @return the updated {@link PersonResponse} which was mapped from Person entity.
     * @throws NotFoundException if Person is not exists by finding it by UUID.
     */
    @Override
    @Transactional
    public PersonResponse update(UUID uuid, PersonRequest personRequest) {

        House house = getHouse(personRequest);

        Person personToUpdate = personMapper.toPerson(personRequest);

        Person personInDB = personRepository.findPersonByUuid(uuid)
                .orElseThrow(() -> NotFoundException.of(Person.class, uuid));

        personToUpdate.setId(personInDB.getId());
        personToUpdate.setUuid(uuid);
        personToUpdate.setHouse(house);
        personToUpdate.setCreateDate(personInDB.getCreateDate());
        personToUpdate.setUpdateDate(LocalDateTime.now());

        Person updated = personRepository.save(personToUpdate);
        PersonResponse response = personMapper.toResponse(updated);
        log.info("PersonService method update {}", response);

        return response;
    }

    /**
     * Patch updates one {@link Person}.
     * @param uuid the field uuid of the {@link Person}.
     * @param fields map of parameters for update {@link Person} with uuid.
     * @return the updated {@link PersonResponse} which was mapped from Person entity.
     * @throws NotFoundException if Person is not exists by finding it by UUID.
     */
    @Override
    @Transactional
    public PersonResponse patchUpdate(UUID uuid, Map<String, Object> fields) {

        if (fields == null ||
            fields.isEmpty() ||
            fields.containsValue(null) ||
            fields.containsValue("")) {
            throw CheckEmptyException.of(Person.class, fields);
        }

        Optional<Person> personInDB = personRepository.findPersonByUuid(uuid);

        if (personInDB.isPresent()) {
            Person person = personInDB.get();

            fields.forEach((k, v) -> {
                Field field = ReflectionUtils.findField(Person.class, k);

                if (field != null) {
                    field.setAccessible(true);
                    if (field.getType().equals(Sex.class)) {
                        Sex sex = Sex.valueOf((String) v);
                        ReflectionUtils.setField(field, person, sex);
                    } else {
                        ReflectionUtils.setField(field, person, v);
                    }
                }
            });
            Person updated = personRepository.save(person);
            PersonResponse response = personMapper.toResponse(updated);
            log.info("PersonService method patchUpdate {}", response);

            return response;
        } else {
            throw NotFoundException.of(Person.class, uuid);
        }
    }

    /**
     * Deletes one {@link Person} by UUID.
     *
     * @param uuid the field of the Person.
     * @throws NotFoundException if Person is not exists by finding it by UUID.
     */
    @Override
    @Transactional
    public void delete(UUID uuid) {

        Person personInDB = personRepository.findPersonByUuid(uuid)
                .orElseThrow(() -> NotFoundException.of(Person.class, uuid));

        if(personInDB!=null){
            personRepository.deletePersonByUuid(uuid);
        }

        log.info("PersonService method delete {}", personInDB);
    }

    private House getHouse(PersonRequest personRequest) {
        return houseRepository.findHouseByUuid(personRequest.getHouseUUID()).get();
    }
}