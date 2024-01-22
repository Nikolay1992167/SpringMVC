package ru.clevertec.house.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.house.entity.Person;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    Optional<Person> findPersonByUuid(UUID uuid);

    @Query(value = """
            SELECT p.* FROM persons p
            JOIN houses h ON p.house_id = h.id
            WHERE h.uuid = ?
            """, nativeQuery = true)
    Page<Person> findPersonsWhichLiveInHouse(UUID houseId, Pageable pageable);

    @Query(value = """
            SELECT p.* FROM persons p 
            JOIN house_history hh ON p.id = hh.person_id
            JOIN houses h ON hh.house_id = h.id
            WHERE h.uuid = :houseId AND hh.type_person = 'TENANT'
            """, nativeQuery = true)
    Page<Person> findPersonsWhichSomeTimeLiveInHouse(UUID houseId, Pageable pageable);

    @Query(value = """
            SELECT p.* FROM persons p
            JOIN house_history hh ON p.id = hh.person_id
            JOIN houses h ON hh.house_id = h.id
            WHERE h.uuid = :houseId AND hh.type_person = 'OWNER'
            """, nativeQuery = true)
    Page<Person> findPersonsWhichSomeTimeOwnHouse(UUID houseId, Pageable pageable);

    @Query(value = """
            SELECT * FROM persons
            WHERE CONCAT(name, ' ', surname)
            LIKE '%' || ? || '%'
            """, nativeQuery = true)
    Page<Person> findPersonsFullTextSearch(String searchTerm, Pageable pageable);

    void deletePersonByUuid(UUID uuid);
}
