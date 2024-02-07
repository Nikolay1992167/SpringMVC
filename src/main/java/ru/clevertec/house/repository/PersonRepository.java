package ru.clevertec.house.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.enums.TypePerson;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    /**
     * Finds one {@link Person} entity by UUID.
     *
     * @param uuid the UUID of the Person entity.
     * @return an {@link Optional} containing the Person entity with the specified UUID, or an empty Optional if
     * no such entity exists in the database.
     */
    @EntityGraph(attributePaths = {"house", "personHouseHistories", "ownedHouses"})
    Optional<Person> findPersonByUuid(UUID uuid);

    /**
     * Finds all {@link Person} entity.
     *
     * @param pageable the {@link Pageable} which will be parameters for pagination.
     * @return a list of the Person entity, or an empty if
     * no such entity exists in the database.
     */
    @Override
    @EntityGraph(attributePaths = {"house"})
    Page<Person> findAll(Pageable pageable);

    /**
     * Finds all {@link Person} entity by House's UUID, in which lives a Person.
     *
     * @param houseId  the UUID of the House entity.
     * @param pageable the {@link Pageable} which will be parameters for pagination.
     * @return a list of the Person entity, or an empty if
     * no such entity exists in the database.
     */
    @EntityGraph(attributePaths = {"house", "personHouseHistories", "ownedHouses"})
    Page<Person> findAllByHouseUuid(UUID houseId, Pageable pageable);

    /**
     * Finds all {@link Person} entity by House's UUID, which sometimes tenants or owners.
     *
     * @param uuid     the UUID of the House entity.
     * @param type     type person.
     * @param pageable the {@link Pageable} which will be parameters for pagination.
     * @return a list of the Person entity, or an empty if
     * no such entity exists in the database.
     */
    @EntityGraph(attributePaths = {"house", "personHouseHistories", "ownedHouses"})
    Page<Person> findByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType(UUID uuid, TypePerson type, Pageable pageable);

    /**
     * Finds all {@link Person} by the specified element.
     *
     * @param searchTerm the specified element for search.
     * @param pageable   the {@link Pageable} which will be parameters for pagination.
     * @return a list of the Person entity, or an empty if
     * no such entity exists in the database.
     */
    @Query("""
            SELECT DISTINCT p FROM Person p
            JOIN FETCH p.house
            WHERE (:searchTerm IS NULL OR CONCAT(p.name, ' ', p.surname)
            LIKE %:searchTerm%)
            """)
    Page<Person> findPersonsFullTextSearch(String searchTerm, Pageable pageable);

    /**
     * Deletes one {@link Person} entity by UUID.
     *
     * @param uuid the UUID of the Person entity.
     */
    void deletePersonByUuid(UUID uuid);
}
