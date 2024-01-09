package by.clevertec.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Objects;
import java.util.Properties;

@Configuration
@ComponentScan("by.clevertec")
@RequiredArgsConstructor
@EnableTransactionManagement
@PropertySource("classpath:application.yml")
public class DatabaseConfig {

    @Value("${spring.datasource.driver}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("#{T(java.lang.Integer).parseInt('${spring.datasource.poolSize}')}")
    private Integer poolSize;

    @Value("${spring.hibernate.ddl}")
    private String ddl;

    @Value("${spring.hibernate.dialect}")
    private String dialect;

    @Value("${spring.hibernate.show_sql}")
    private String showSql;

    @Value("${spring.hibernate.format_sql}")
    private String formatSql;

    /**
     * Creates a new instance of the {@link BeanFactoryPostProcessor} interface, which allows for custom modification of an application context's bean definitions.
     * The method creates a new {@link PropertySourcesPlaceholderConfigurer} object, which is used to replace ${...} placeholders with properties from a {@link Properties} instance.
     * Then it creates a new {@link YamlPropertiesFactoryBean} object and sets the 'application.yml' file as its resource. The {@link YamlPropertiesFactoryBean} object is used to load YAML (`.yml`) files and convert them into a {@link Properties} object.
     * The method then retrieves the {@link Properties} object from the {@link YamlPropertiesFactoryBean} object and sets it on the {@link PropertySourcesPlaceholderConfigurer} object.
     *
     * @return the new {@link PropertySourcesPlaceholderConfigurer} object is returned to be used as a {@link BeanFactoryPostProcessor} for the Spring application context.
     */
    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        PropertySourcesPlaceholderConfigurer configure = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        Properties yamlObject = Objects.requireNonNull(yaml.getObject(), "Yaml not found.");
        configure.setProperties(yamlObject);
        return configure;
    }

    /**
     * Creates a new instance of the {@link HikariDataSource} class, which provides a connection pool for the database.
     * The method retrieves the database connection properties from the application.yml file using the
     * {@Value}. Then creates a new {@link HikariConfig} class and sets these properties
     * on it before passing it to the constructor of a new {@link HikariDataSource} object.
     *
     * @return the new {@link HikariDataSource} object is returned to be used as a data source for the Hibernate ORM.
     */
    @Bean
    public HikariDataSource hikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(poolSize);
        return new HikariDataSource(hikariConfig);
    }

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
        sessionFactory.setPackagesToScan("ru.clevertec.ecl.giftcertificates");
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
