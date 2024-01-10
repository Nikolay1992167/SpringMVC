package by.clevertec.service.impl;

import by.clevertec.dao.HouseDao;
import by.clevertec.dto.request.HouseRequest;
import by.clevertec.dto.response.HouseResponse;
import by.clevertec.entity.House;
import by.clevertec.exception.NotFoundException;
import by.clevertec.mapper.HouseMapper;
import by.clevertec.service.HouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseDao houseDao;

    private final HouseMapper houseMapper = Mappers.getMapper(HouseMapper.class);

    @Override
    public HouseResponse findById(Long id) {
        HouseResponse houseResponse = houseDao.findById(id)
                .map(houseMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("House with ID " + id + " does not found!"));
        log.info("House findById {}", houseResponse);
        return houseResponse;
    }

    @Override
    public List<HouseResponse> findAll(int pageNumber, int pageSize) {
        List<HouseResponse> houses = houseDao.findAll(pageNumber, pageSize)
                .stream()
                .map(houseMapper::toResponse)
                .toList();
        log.info("House findAll {}", houses.size());
        return houses;
    }

    @Override
    public HouseResponse save(HouseRequest houseRequest) {
        House houseToSave = houseMapper.toHouse(houseRequest);
        House saved = houseDao.save(houseToSave);
        HouseResponse response = houseMapper.toResponse(saved);
        log.info("House save {}", response);
        return  response;
    }

    @Override
    public HouseResponse update(HouseRequest houseRequest) {
        House houseToUpdate = houseMapper.toHouse(houseRequest);
        House houseById = houseDao.findById(houseToUpdate.getId())
                .orElseThrow(() -> new NotFoundException("House with ID " + houseToUpdate.getId() + " does not found!"));
        houseToUpdate.setUuid(houseById.getUuid());
        houseToUpdate.setCreateDate(houseById.getCreateDate());
        checkFieldWhenNull(houseToUpdate, houseById);
        House updated = houseDao.update(houseToUpdate);
        HouseResponse response = houseMapper.toResponse(updated);
        log.info("House update {}", response);
        return response;
    }

    @Override
    public void delete(Long id) {
        House house = houseDao.delete(id)
                .orElseThrow(() -> new NotFoundException("There is no House with ID " + id + " to delete"));
        log.info("House delete {}", house);
    }

    private void checkFieldWhenNull(House expectedHouse, House actualHouse){
        expectedHouse.setArea(expectedHouse.getArea() != null ? expectedHouse.getArea() : actualHouse.getArea());
        expectedHouse.setCountry(expectedHouse.getCountry() != null ? expectedHouse.getArea() : actualHouse.getCountry());
        expectedHouse.setCity(expectedHouse.getCity() != null ? expectedHouse.getCity() : actualHouse.getCity());
        expectedHouse.setStreet(expectedHouse.getStreet() != null ? expectedHouse.getStreet() : actualHouse.getStreet());
        expectedHouse.setNumber(expectedHouse.getNumber() != null ? expectedHouse.getNumber() : actualHouse.getNumber());
        expectedHouse.setResidents(!expectedHouse.getResidents().isEmpty() ? expectedHouse.getResidents() : actualHouse.getResidents());
        expectedHouse.setOwners(!expectedHouse.getOwners().isEmpty() ? expectedHouse.getOwners() : actualHouse.getOwners());
    }
}
