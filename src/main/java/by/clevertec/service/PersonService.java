package by.clevertec.service;

import by.clevertec.dto.request.PersonRequest;
import by.clevertec.dto.response.PersonResponse;

import java.util.List;
import java.util.UUID;

public interface PersonService {

    PersonResponse findById(UUID uuid);

    List<PersonResponse> findAll(int pageNumber, int pageSize);

    PersonResponse save(PersonRequest personRequest);

    PersonResponse update(UUID uuid, PersonRequest personRequest);

    void delete(UUID uuid);
}
