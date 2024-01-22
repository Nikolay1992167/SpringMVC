package ru.clevertec.house.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.clevertec.house.entity.House;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HouseRepository extends JpaRepository<House, UUID> {

    Optional<House> findHouseByUuid(UUID uuid);

    @Query(value = """
        SELECT * FROM houses 
        WHERE uuid IN (:uuids)
        """, nativeQuery = true)
    List<House> findAllByUuids(@Param("uuids") List<UUID> uuids);

    @Query(value = """
                SELECT h.* FROM houses h
                JOIN house_history hh ON h.id = hh.house_id
                JOIN persons p ON hh.person_id = p.id
                WHERE p.uuid = :personId AND hh.type_person = 'TENANT'
            """, nativeQuery = true)
    Page<House> findHousesWhichSomeTimeLivesPerson(UUID personId, Pageable pageable);

    @Query(value = """
            SELECT h.* FROM houses h
            JOIN houses_persons hp ON h.id = hp.houses_id
            JOIN persons p ON hp.persons_id = p.id 
            WHERE p.uuid = ?
            """, nativeQuery = true)
    Page<House> findHousesWhichOwnPerson(UUID personId, Pageable pageable);

    @Query(value = """
            SELECT h.* FROM houses h
            JOIN house_history hh ON h.id = hh.house_id
            JOIN persons p ON hh.person_id = p.id
            WHERE p.uuid = :personId AND hh.type_person = 'OWNER'
            """, nativeQuery = true)
    Page<House> findHousesWhichSomeTimeOwnPerson(UUID personId, Pageable pageable);

    @Query(value = """
            SELECT * FROM houses
            WHERE CONCAT(area, ' ', country, ' ', city, ' ', street)\s
            LIKE '%' || ? || '%'
            """, nativeQuery = true)
    Page<House> findHousesFullTextSearch(String searchTerm, Pageable pageable);

    void deleteHouseByUuid(UUID uuid);
}
