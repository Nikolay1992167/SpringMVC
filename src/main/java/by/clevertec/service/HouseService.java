package by.clevertec.service;

import by.clevertec.dto.request.HouseRequest;
import by.clevertec.dto.response.HouseResponse;

import java.util.List;

public interface HouseService {

    HouseResponse findById(Long id);

    List<HouseResponse> findAll(int pageNumber, int pageSize);

    HouseResponse save(HouseRequest houseRequest);

    HouseResponse update(HouseRequest houseRequest);

    void delete(Long id);
}
