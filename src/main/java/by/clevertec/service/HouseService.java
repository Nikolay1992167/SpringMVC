package by.clevertec.service;

import by.clevertec.dto.request.HouseRequest;
import by.clevertec.dto.response.HouseResponse;

import java.util.List;
import java.util.UUID;

public interface HouseService {

    HouseResponse findById(UUID uuid);

    List<HouseResponse> findAll(int pageNumber, int pageSize);

    HouseResponse save(HouseRequest houseRequest);

    HouseResponse update(UUID uuid, HouseRequest houseRequest);

    void delete(UUID uuid);
}
