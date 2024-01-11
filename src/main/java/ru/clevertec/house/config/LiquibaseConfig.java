package ru.clevertec.house.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    /**
     * Creates a new instance of the {@link SpringLiquibase} class.
     * * The method sets the data source to be used by SpringLiquibase,
     * by calling the dataSource passed in the parameter.
     * It then specifies the path to the Liquibase change file using the setChangeLog() method.
     * @param dataSource the data source for Liquibase.
     * @return a SpringLiquibase object configured with a data source and a Liquibase change file.
     */
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {

        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db.changelog/db.changelog-master.yml");
        return liquibase;
    }
}
