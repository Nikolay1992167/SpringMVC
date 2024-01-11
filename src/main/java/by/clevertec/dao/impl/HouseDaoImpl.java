package by.clevertec.dao.impl;

import by.clevertec.dao.HouseDao;
import by.clevertec.entity.House;
import by.clevertec.entity.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class HouseDaoImpl implements HouseDao {

    private final SessionFactory sessionFactory;

    @Override
    public Optional<House> findById(UUID uuid) {

        try (Session session = sessionFactory.openSession()) {

            House house = session.createQuery("SELECT h FROM House h LEFT JOIN FETCH h.owners WHERE h.uuid = :uuid", House.class)
                    .setParameter("uuid", uuid)
                    .uniqueResult();

            if (house != null) {
                List<Person> residents = session.createQuery("SELECT p FROM Person p WHERE p.house.id = :houseId", Person.class)
                        .setParameter("houseId", house.getId())
                        .getResultList();
                house.setResidents(residents);
            }

            return Optional.ofNullable(house);
        }
    }

    @Override
    public List<House> findAll(int pageNumber, int pageSize) {

        try (Session session = sessionFactory.openSession()) {

            Query<House> query = session.createQuery("SELECT h FROM House h LEFT JOIN FETCH h.owners", House.class);
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
            List<House> houses = query.getResultList();

            houses.forEach(house -> house.setResidents(session.createQuery("SELECT p FROM Person p LEFT JOIN FETCH p.house h WHERE h.id = :houseId", Person.class)
                    .setParameter("houseId", house.getId())
                    .getResultList()));

            return houses;
        }
    }

    @Override
    public House save(House house) {

        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            session.persist(house);
            session.getTransaction().commit();

            return house;
        }
    }

    @Override
    public House update(House house) {

        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            House merged = session.merge(house);
            session.getTransaction().commit();

            return merged;
        }
    }

    @Override
    public Optional<House> delete(UUID uuid) {

        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            Optional<House> house = findById(uuid);

            if (house.isEmpty()) {
                return Optional.empty();
            }

            session.remove(house.get());
            session.getTransaction().commit();

            return house;
        }
    }

    @Override
    public List<House> findAllHouseWhenOwnPerson(UUID personId) {
        return null;
    }
}
