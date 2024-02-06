package by.clevertec.config;

import by.clevertec.exception.handler.ControllerAdvice;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "exception.handler", name = "enabled", havingValue = "true", matchIfMissing = true)
public class HandlerExceptionAutoConfiguration {

    /**
     * Initializes the HandlerExceptionAutoConfiguration class and logs the initialization message.
     */
    @PostConstruct
    void init() {
        log.info("HandlerExceptionAutoConfiguration initialized");
    }

    /**
     * Returns a ControllerAdvice bean if it is not already present in the Spring application context.
     *
     * @return The ControllerAdvice bean.
     */
    @Bean
    @ConditionalOnMissingBean
    public ControllerAdvice createExceptionHandler() {
        return new ControllerAdvice();
    }
}
