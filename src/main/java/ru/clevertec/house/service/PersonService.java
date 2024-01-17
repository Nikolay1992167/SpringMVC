package ru.clevertec.house.service;

import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PersonService {

    PersonResponse findById(UUID uuid);

    List<PersonResponse> findAll(int pageNumber, int pageSize);

    PersonResponse save(PersonRequest personRequest);

    PersonResponse update(UUID uuid, PersonRequest personRequest);

    PersonResponse patchUpdate(UUID uuid, Map<String, Object> fields);

    void delete(UUID uuid);
}