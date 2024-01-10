package by.clevertec.service.impl;

import by.clevertec.dto.request.PersonRequest;
import by.clevertec.dto.response.PersonResponse;
import by.clevertec.service.PersonService;

import java.util.List;

public class PersonServiceImpl implements PersonService {

    @Override
    public PersonResponse findById(Long id) {
        return null;
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
    public PersonResponse update(PersonRequest personRequest) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
