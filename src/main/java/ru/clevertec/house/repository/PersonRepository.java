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

    @EntityGraph(attributePaths = {"house", "personHouseHistories", "ownedHouses"})
    Optional<Person> findPersonByUuid(UUID uuid);

    Page<Person> findAllByHouseUuid(UUID houseId, Pageable pageable);

    Page<Person> findByPersonHouseHistoriesHouseUuidAndPersonHouseHistoriesType(UUID uuid, TypePerson type, Pageable pageable);

    @Query("""
            SELECT p FROM Person p
            WHERE (:searchTerm IS NULL OR CONCAT(p.name, ' ', p.surname)
            LIKE %:searchTerm%)
            """)
    Page<Person> findPersonsFullTextSearch(String searchTerm, Pageable pageable);

    @EntityGraph(attributePaths = {"house", "personHouseHistories", "ownedHouses"})
    void deletePersonByUuid(UUID uuid);
}
