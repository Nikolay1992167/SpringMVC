package ru.clevertec.house.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;

import java.util.Map;
import java.util.UUID;

public interface HouseService {

    HouseResponse findById(UUID uuid);

    Page<HouseResponse> findAll(Pageable pageable);

    Page<HouseResponse> findHousesWhichSomeTimeLivesPerson(UUID personId, Pageable pageable);

    Page<HouseResponse> findHousesWhichOwnPerson(UUID personId, Pageable pageable);

    Page<HouseResponse> findHousesWhichSomeTimeOwnPerson(UUID personId, Pageable pageable);

    Page<HouseResponse> findHousesFullTextSearch(String searchTerm, Pageable pageable);

    HouseResponse save(HouseRequest houseRequest);

    HouseResponse update(UUID uuid, HouseRequest houseRequest);

    HouseResponse patchUpdate(UUID uuid, Map<String, Object> fields);

    void delete(UUID uuid);
}