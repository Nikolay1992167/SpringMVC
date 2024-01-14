package ru.clevertec.house.dao.util;

public class SQLRequest {

    public static final String SQL_FOR_FIND_PERSONS = """
            SELECT * FROM persons 
            WHERE house_id = (SELECT id FROM houses WHERE uuid = ?)
            """;

    public static final String SQL_FOR_FULL_TEXT_SEARCH_PERSONS = """            
            SELECT * FROM persons
               WHERE CONCAT(name, ' ', surname)
               LIKE '%' || ? || '%'
            """;

    public static final String SQL_FOR_FIND_HOUSES = """
            SELECT * FROM houses h 
            JOIN houses_persons hp ON h.id = hp.houses_id 
            JOIN persons p ON hp.persons_id = p.id WHERE p.uuid = ?
            """;

    public static final String SQL_FOR_FULL_TEXT_SEARCH_HOUSES = """            
            SELECT * FROM houses
            WHERE CONCAT(area, ' ', country, ' ', city, ' ', street) 
            LIKE '%' || ? || '%'
            """;
}
