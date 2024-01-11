package ru.clevertec.house.service.impl;

import ru.clevertec.house.dao.HouseDao;
import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.mapper.HouseMapper;
import ru.clevertec.house.service.HouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseDao houseDao;

    private final HouseMapper houseMapper = Mappers.getMapper(HouseMapper.class);

    @Override
    public HouseResponse findById(UUID uuid) {

        HouseResponse houseResponse = houseDao.findById(uuid)
                .map(houseMapper::toResponse)
                .orElseThrow(() -> NotFoundException.of(House.class, uuid));
        log.info("House method findById {}", houseResponse);

        return houseResponse;
    }

    @Override
    public List<HouseResponse> findAll(int pageNumber, int pageSize) {

        List<HouseResponse> houses = houseDao.findAll(pageNumber, pageSize)
                .stream()
                .map(houseMapper::toResponse)
                .toList();
        log.info("House method findAll {}", houses.size());

        return new ArrayList<>(houses);
    }

    @Override
    public HouseResponse save(HouseRequest houseRequest) {

        House houseToSave = houseMapper.toHouse(houseRequest);
        House saved = houseDao.save(houseToSave);
        HouseResponse response = houseMapper.toResponse(saved);
        log.info("House method save {}", response);

        return response;
    }

    @Override
    public HouseResponse update(UUID uuid, HouseRequest houseRequest) {

        House houseToUpdate = houseMapper.toHouse(houseRequest);
        House houseInDB = houseDao.findById(uuid)
                .orElseThrow(() -> NotFoundException.of(House.class, uuid));

        houseToUpdate.setId(houseInDB.getId());
        houseToUpdate.setUuid(uuid);
        houseToUpdate.setCreateDate(houseInDB.getCreateDate());

        House updated = houseDao.update(houseToUpdate);
        HouseResponse response = houseMapper.toResponse(updated);
        log.info("House method update {}", response);

        return response;
    }

    @Override
    public void delete(UUID uuid) {

        House house = houseDao.delete(uuid)
                .orElseThrow(() -> NotFoundException.of(House.class, uuid));
        log.info("House method delete {}", house);
    }
}
