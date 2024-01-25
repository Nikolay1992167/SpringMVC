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

    /**
     * Finds one {@link House} entity by UUID.
     *
     * @param uuid the UUID of the House entity.
     * @return an {@link Optional} containing the House entity with the specified UUID, or an empty Optional if
     * no such entity exists in the database.
     */
    @EntityGraph(attributePaths = {"residents", "houseHistories", "owners"})
    Optional<House> findHouseByUuid(UUID uuid);

    /**
     * Finds all {@link House} entity by List of UUID.
     *
     * @param uuids the UUID of the House entity.
     * @return a list of the House entity with the specified UUID, or an empty if
     * no such entity exists in the database.
     */
    List<House> findAllByUuidIn(List<UUID> uuids);

    /**
     * Finds all {@link House} entity by Person's UUID, which sometimes tenants or owners.
     *
     * @param uuid the UUID of the Person entity.
     * @param type type person.
     * @param pageable the {@link Pageable} which will be parameters for pagination.
     * @return a list of the House entity, or an empty if
     * no such entity exists in the database.
     */
    Page<House> findByHouseHistoriesPersonUuidAndHouseHistoriesType(UUID uuid, TypePerson type, Pageable pageable);

    /**
     * Finds all {@link House} entity by Person's UUID, which owns a Person.
     *
     * @param personId the UUID of the Person entity.
     * @param pageable the {@link Pageable} which will be parameters for pagination.
     * @return a list of the House entity, or an empty if
     * no such entity exists in the database.
     */
    Page<House> findByOwnersUuid(UUID personId, Pageable pageable);

    /**
     * Finds all {@link House} by the specified element.
     *
     * @param searchTerm the specified element for search.
     * @param pageable the {@link Pageable} which will be parameters for pagination.
     * @return a list of the House entity, or an empty if
     * no such entity exists in the database.
     */
    @Query("""
            SELECT h FROM House h
            WHERE (:searchTerm IS NULL OR CONCAT(h.area, ' ', h.country, ' ', h.city, ' ', h.street) 
            LIKE %:searchTerm%)
            """)
    Page<House> findHousesFullTextSearch(String searchTerm, Pageable pageable);

    /**
     * Deletes one {@link House} entity by UUID.
     *
     * @param uuid the UUID of the House entity.
     */
    @EntityGraph(attributePaths = {"residents", "houseHistories", "owners"})
    void deleteHouseByUuid(UUID uuid);
}