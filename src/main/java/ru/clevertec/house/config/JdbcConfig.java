package ru.clevertec.house.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JdbcConfig {

    /**
     * Creates a new instance of the {@link JdbcTemplate} class.
     * The method sets the data source to be used by JdbcTemplate,
     * by calling the dataSource passed in the parameter.
     * @param dataSource the data source for JdbcTemplate.
     * @return a JdbcTemplate object configured with a data source.
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
