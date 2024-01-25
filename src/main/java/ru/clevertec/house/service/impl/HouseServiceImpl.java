package ru.clevertec.house.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.enums.TypePerson;
import ru.clevertec.house.exception.CheckEmptyException;
import ru.clevertec.house.exception.HouseNotEmptyException;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.mapper.HouseMapper;
import ru.clevertec.house.proxy.Cache;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.service.HouseService;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;

    private final HouseMapper houseMapper = Mappers.getMapper(HouseMapper.class);

    /**
     * Finds one {@link HouseResponse} by UUID.
     *
     * @param uuid the field uuid of the {@link House}.
     * @return HouseResponse with the specified UUID and mapped from House entity.
     * @throws NotFoundException if House is not exists by finding it by UUID.
     */
    @Cache
    @Override
    public HouseResponse findById(UUID uuid) {

        HouseResponse response = houseRepository.findHouseByUuid(uuid)
                .map(houseMapper::toResponse)
                .orElseThrow(() -> NotFoundException.of(House.class, uuid));

        log.info("HouseService method findById {}", response);

        return response;
    }

    /**
     * Finds all {@link HouseResponse}.
     *
     * @param pageable the {@link Pageable} which will be parameters for pagination.
     * @return mapped from entity to dto list of all HouseResponse.
     */
    @Override
    public Page<HouseResponse> findAll(Pageable pageable) {

        Page<HouseResponse> response = houseRepository.findAll(pageable)
                .map(houseMapper::toResponse);

        log.info("HouseService method findAll {}", response.stream().count());

        return response;
    }

    /**
     * Finds and returns a list of {@link HouseResponse} objects that represent houses some time lives by a specific person.
     *
     * @param personId the UUID of the person to find houses for.
     * @param pageable the {@link Pageable} which will be parameters for pagination.
     * @return a list of {@link HouseResponse} objects, each representing a house lived by the person.
     * The list is empty if no houses are found.
     */
    @Override
    public Page<HouseResponse> findHousesWhichSomeTimeLivesPerson(UUID personId, Pageable pageable) {

        TypePerson typePerson = TypePerson.TENANT;

        Page<HouseResponse> responses = houseRepository.findByHouseHistoriesPersonUuidAndHouseHistoriesType(personId, typePerson, pageable)
                .map(houseMapper::toResponse);

        log.info("HouseService method findHousesWhichSomeTimeLivesPerson {}", responses.stream().count());

        return responses;
    }

    /**
     * Finds and returns a list of {@link HouseResponse} objects that represent houses owned by a specific person.
     *
     * @param personId the UUID of the person to find houses for.
     * @param pageable the {@link Pageable} which will be parameters for pagination.
     * @return a list of {@link HouseResponse} objects, each representing a house owned by the person.
     * The list is empty if no houses are found.
     */
    @Override
    public Page<HouseResponse> findHousesWhichOwnPerson(UUID personId, Pageable pageable) {

        Page<HouseResponse> responses = houseRepository.findByOwnersUuid(personId, pageable)
                .map(houseMapper::toResponse);

        log.info("HouseService method findHousesWhichOwnPerson {}", responses.stream().count());

        return responses;
    }

    /**
     * Finds and returns a list of {@link HouseResponse} objects that represent houses some time owned by a specific person.
     *
     * @param personId the UUID of the person to find houses for.
     * @param pageable the {@link Pageable} which will be parameters for pagination.
     * @return a list of {@link HouseResponse} objects, each representing a house owned by the person.
     * The list is empty if no houses are found.
     */
    @Override
    public Page<HouseResponse> findHousesWhichSomeTimeOwnPerson(UUID personId, Pageable pageable) {

        TypePerson typePerson = TypePerson.OWNER;

        Page<HouseResponse> responses = houseRepository.findByHouseHistoriesPersonUuidAndHouseHistoriesType(personId, typePerson, pageable)
                .map(houseMapper::toResponse);

        log.info("HouseService method findHousesWhichSomeTimeOwnPerson {}", responses.stream().count());

        return responses;
    }

    /**
     * Performs a full-text search to find houses based on a search term.
     *
     * @param searchTerm the term to search for in any text field of the houses.
     * @param pageable   the {@link Pageable} which will be parameters for pagination.
     * @return a list of {@link HouseResponse} objects, each representing a house that matches the search term.
     * The list is empty if no matches are found.
     */
    @Override
    public Page<HouseResponse> findHousesFullTextSearch(String searchTerm, Pageable pageable) {

        Page<HouseResponse> responses = houseRepository.findHousesFullTextSearch(searchTerm, pageable)
                .map(houseMapper::toResponse);

        log.info("HouseService method findHousesFullTextSearch {}", responses.stream().count());

        return responses;
    }

    /**
     * Saves one {@link House}.
     *
     * @param houseRequest the {@link HouseRequest} which will be mapped to House
     *                     and saved in database by dao.
     * @return the saved {@link HouseResponse} which was mapped from House entity.
     */
    @Cache
    @Override
    @Transactional
    public HouseResponse save(HouseRequest houseRequest) {

        House houseToSave = houseMapper.toHouse(houseRequest);
        House savedHouse = houseRepository.save(houseToSave);

        HouseResponse responseHouse = houseMapper.toResponse(savedHouse);
        log.info("HouseService method save {}", responseHouse);

        return responseHouse;
    }

    /**
     * Updates one {@link House}.
     *
     * @param uuid         the field uuid of the {@link House}.
     * @param houseRequest the {@link HouseRequest} which will be mapped to House and
     *                     updated in database by dao.
     * @return the updated {@link HouseResponse} which was mapped from House entity.
     * @throws NotFoundException if House is not exists by finding it by UUID.
     */
    @Cache
    @Override
    @Transactional
    public HouseResponse update(UUID uuid, HouseRequest houseRequest) {

        House houseToUpdate = houseMapper.toHouse(houseRequest);

        House houseInDB = houseRepository.findHouseByUuid(uuid)
                .orElseThrow(() -> NotFoundException.of(House.class, uuid));

        houseToUpdate.setId(houseInDB.getId());
        houseToUpdate.setUuid(uuid);
        houseToUpdate.setCreateDate(houseInDB.getCreateDate());

        House updatedHouse = houseRepository.save(houseToUpdate);

        HouseResponse response = houseMapper.toResponse(updatedHouse);
        log.info("HouseService method update {}", response);

        return response;
    }

    /**
     * Patch updates one {@link House}.
     *
     * @param uuid   the field uuid of the {@link House}.
     * @param fields map of parameters for update {@link House} with uuid.
     * @return the updated {@link HouseResponse} which was mapped from House entity.
     * @throws NotFoundException if House is not exists by finding it by UUID.
     */
    @Override
    @Transactional
    public HouseResponse patchUpdate(UUID uuid, Map<String, Object> fields) {

        if (fields == null ||
            fields.isEmpty() ||
            fields.containsValue(null) ||
            fields.containsValue("")) {
            throw CheckEmptyException.of(House.class, fields);
        }

        Optional<House> houseInDB = houseRepository.findHouseByUuid(uuid);

        if (houseInDB.isPresent()) {
            House house = houseInDB.get();

            fields.forEach((k, v) -> {

                Field field = ReflectionUtils.findField(House.class, k);

                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, house, v);
                }
            });
            House updatedHouse = houseRepository.save(house);
            HouseResponse response = houseMapper.toResponse(updatedHouse);

            log.info("HouseService method patchUpdate {}", response);

            return response;
        } else {
            throw NotFoundException.of(House.class, uuid);
        }
    }

    /**
     * Deletes one {@link House} by UUID.
     *
     * @param uuid the field of the House.
     * @throws NotFoundException if House is not exists by finding it by UUID.
     */
    @Cache
    @Override
    @Transactional
    public void delete(UUID uuid) {

        House houseInDB = houseRepository.findHouseByUuid(uuid)
                .orElseThrow(() -> NotFoundException.of(House.class, uuid));

        if (houseInDB != null) {

            if (!houseInDB.getResidents().isEmpty()) {
                throw new HouseNotEmptyException("Cannot delete house: there are residents living in it");
            }

            houseRepository.deleteHouseByUuid(uuid);
        }
        log.info("HouseService method delete {}", houseInDB);
    }
}