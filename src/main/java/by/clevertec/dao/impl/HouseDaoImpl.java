package by.clevertec.dao.impl;

import by.clevertec.dao.HouseDao;
import by.clevertec.entity.House;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class HouseDaoImpl implements HouseDao {

    private final SessionFactory sessionFactory;

    @Override
    public Optional<House> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<House> findAll() {
        return null;
    }

    @Override
    public House save(House house) {
        return null;
    }

    @Override
    public House update(House house) {
        return null;
    }

    @Override
    public void delete(Long id) {
    }

    @Override
    public List<House> findAllHouseWhenOwnPerson(Long personId) {
        return null;
    }
}
