package ru.clevertec.house.service;

import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;

import java.util.List;
import java.util.UUID;

public interface HouseService {

    HouseResponse findById(UUID uuid);

    List<HouseResponse> findAll(int pageNumber, int pageSize);

    HouseResponse save(HouseRequest houseRequest);

    HouseResponse update(UUID uuid, HouseRequest houseRequest);

    void delete(UUID uuid);
}