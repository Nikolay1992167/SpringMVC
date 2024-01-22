package ru.clevertec.house.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;

import java.util.Map;
import java.util.UUID;

public interface PersonService {

    PersonResponse findById(UUID uuid);

    Page<PersonResponse> findAll(Pageable pageable);

    Page<PersonResponse> findPersonsWhichLiveInHouse(UUID houseId, Pageable pageable);

    Page<PersonResponse> findPersonsWhichSomeTimeLiveInHouse(UUID houseId, Pageable pageable);

    Page<PersonResponse> findPersonsWhichSomeTimeOwnHouse(UUID houseId, Pageable pageable);

    Page<PersonResponse> findPersonsFullTextSearch(String searchTerm, Pageable pageable);

    PersonResponse save(PersonRequest personRequest);

    PersonResponse update(UUID uuid, PersonRequest personRequest);

    PersonResponse patchUpdate(UUID uuid, Map<String, Object> fields);

    void delete(UUID uuid);
}