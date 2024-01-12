package ru.clevertec.house.dao.jdbc.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.enums.Sex;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class PersonRowMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {

        Person person = new Person();

        person.setId(rs.getLong("id"));
        person.setUuid(UUID.fromString(rs.getString("uuid")));
        person.setName(rs.getString("name"));
        person.setSurname(rs.getString("surname"));
        person.setSex(Sex.valueOf(rs.getString("sex")));

        Passport passport = new Passport();
        passport.setSeries(rs.getString("passport_series"));
        passport.setNumber(rs.getString("passport_number"));

        person.setPassport(passport);
        person.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
        person.setUpdateDate(rs.getTimestamp("update_date").toLocalDateTime());

        return person;
    }
}
