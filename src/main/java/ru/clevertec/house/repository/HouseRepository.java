package ru.clevertec.house.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.enums.TypePerson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HouseRepository extends JpaRepository<House, UUID> {

    @EntityGraph(attributePaths = {"residents", "houseHistories", "owners"})
    Optional<House> findHouseByUuid(UUID uuid);

    List<House> findAllByUuidIn(List<UUID> uuids);

    Page<House> findByHouseHistoriesPersonUuidAndHouseHistoriesType(UUID uuid, TypePerson type, Pageable pageable);

    Page<House> findByOwnersUuid(UUID personId, Pageable pageable);

    @Query("""
            SELECT h FROM House h
            WHERE (:searchTerm IS NULL OR CONCAT(h.area, ' ', h.country, ' ', h.city, ' ', h.street) 
            LIKE %:searchTerm%)
            """)
    Page<House> findHousesFullTextSearch(String searchTerm, Pageable pageable);

    @EntityGraph(attributePaths = {"residents", "houseHistories", "owners"})
    void deleteHouseByUuid(UUID uuid);
}