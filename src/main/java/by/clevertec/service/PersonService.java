package by.clevertec.service;

import by.clevertec.dto.request.PersonRequest;
import by.clevertec.dto.response.PersonResponse;

import java.util.List;

public interface PersonService {

    PersonResponse findById(Long id);

    List<PersonResponse> findAll(int pageNumber, int pageSize);

    PersonResponse save(PersonRequest personRequest);

    PersonResponse update(PersonRequest personRequest);

    void delete(Long id);
}
