package by.clevertec.dao.impl;

import by.clevertec.dao.PersonDao;
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

@Slf4j
@Repository
@RequiredArgsConstructor
public class PersonDaoImpl implements PersonDao {

    private final SessionFactory sessionFactory;

    @Override
    public Optional<Person> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Person person = session.createQuery("SELECT p FROM Person p LEFT JOIN FETCH p.ownedHouses WHERE p.id = :personId", Person.class)
                    .setParameter("personId", id)
                    .uniqueResult();
            if (person != null) {
                person.setHouse(session.createQuery("SELECT h FROM House h LEFT JOIN FETCH h.residents r WHERE r.id = :personId", House.class)
                        .setParameter("personId", person.getId())
                        .uniqueResult());
            }
            return Optional.ofNullable(person);
        }
    }

    @Override
    public List<Person> findAll(int pageNumber, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            Query<Person> query = session.createQuery("SELECT p FROM Person p LEFT JOIN FETCH p.ownedHouses", Person.class);
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
            List<Person> persons = query.getResultList();
            persons.forEach(person -> person.setHouse(session.createQuery("SELECT h FROM House h LEFT JOIN FETCH h.residents r WHERE r.id = :personId", House.class)
                    .setParameter("personId", person.getId())
                    .uniqueResult()));
            return persons;
        }
    }

    @Override
    public Person save(Person person) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(person);
            session.getTransaction().commit();
            return person;
        }
    }

    @Override
    public Person update(Person person) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Person merged = session.merge(person);
            session.getTransaction().commit();
            return  merged;
        }
    }

    @Override
    public Optional<Person> delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Person> person = findById(id);
            if (person.isEmpty()) {
                return Optional.empty();
            }
            session.remove(person.get());
            session.getTransaction().commit();
            return person;
        }
    }

    @Override
    public List<Person> findAllPersonWhenLiveInHouse(Long houseId) {
        return null;
    }
}
