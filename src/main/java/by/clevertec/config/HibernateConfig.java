package by.clevertec.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Properties;

@Configuration
@ComponentScan("by.clevertec")
public class HibernateConfig {

    @Value("${spring.hibernate.ddl}")
    private String ddl;

    @Value("${spring.hibernate.dialect}")
    private String dialect;

    @Value("${spring.hibernate.show_Sql}")
    private String showSql;

    @Value("${spring.hibernate.format_Sql}")
    private String formatSql;

    /**
     * Creates a new instance of the {@link LocalSessionFactoryBean} class. The method sets the data source to be
     * used by the SessionFactory object by calling the {@link HikariDataSource} bean.
     * It also specifies the package where the Hibernate entities are located using the setPackagesToScan() method.
     * Then it sets the Hibernate properties using the {@link #hibernateProperties()} method.
     *
     * @return a LocalSessionFactoryBean object configured with the data source and Hibernate properties.
     */
    @Bean
    public LocalSessionFactoryBean sessionFactory(HikariDataSource hikariDataSource) {

        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(hikariDataSource);
        sessionFactory.setPackagesToScan("by.clevertec");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    /**
     * Creates a new instance of the {@link HibernateTransactionManager} class. The method sets
     * the {@link org.hibernate.SessionFactory} object to be used by the transaction manager by calling the getObject()
     * method on the {@link LocalSessionFactoryBean} returned by the LocalSessionFactoryBean.
     *
     * @return a {@link PlatformTransactionManager} object configured with the SessionFactory object.
     */
    @Bean
    public PlatformTransactionManager hibernateTransactionManager(LocalSessionFactoryBean sessionFactory) {

        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory.getObject());
        return transactionManager;
    }

    /**
     * Create a new instance of the {@link Properties} class. The method sets necessary properties for Hibernate.
     *
     * @return a {@link Properties} object containing the Hibernate properties.
     */
    private Properties hibernateProperties() {

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", ddl);
        hibernateProperties.setProperty("hibernate.dialect", dialect);
        hibernateProperties.setProperty("hibernate.show_sql", showSql);
        hibernateProperties.setProperty("hibernate.format_sql", formatSql);
        return hibernateProperties;
    }
}
