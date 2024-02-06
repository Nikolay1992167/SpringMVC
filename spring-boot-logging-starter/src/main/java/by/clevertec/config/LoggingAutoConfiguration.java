package by.clevertec.config;

import by.clevertec.aop.ControllerAspect;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "aop.logging", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LoggingAutoConfiguration {

    /**
     * Initializes the LoggingAutoConfiguration class and logs the initialization message.
     */
    @PostConstruct
    void init() {
        log.info("LoggingAutoConfiguration initialized");
    }

    /**
     * Returns a ControllerAspect bean if it is not already present in the Spring application context.
     *
     * @return The ControllerAspect bean.
     */
    @Bean
    public ControllerAspect loggingAspect() {

        return new ControllerAspect();
    }
}
