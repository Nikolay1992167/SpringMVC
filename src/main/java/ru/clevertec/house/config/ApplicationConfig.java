package ru.clevertec.house.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Objects;
import java.util.Properties;

@Configuration
@ComponentScan("ru.clevertec.house")
@EnableTransactionManagement
public class ApplicationConfig {

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
}
