package by.clevertec.config;

import by.clevertec.exception.handler.ControllerAdvice;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class HandlerExceptionAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    public void shouldReturnControllerAdviceBeanIfEnabledTrue() {
        contextRunner.withPropertyValues("exception.handler.enabled=true")
                .withUserConfiguration(HandlerExceptionAutoConfiguration.class)
                .run(context ->
                    assertThat(context).hasSingleBean(ControllerAdvice.class)
                );
    }

    @Test
    public void shouldCheckNotCreateControllerAdviceBeanIfEnabledFalse() {
        contextRunner.withPropertyValues("exception.handler.enabled=false")
                .withUserConfiguration(HandlerExceptionAutoConfiguration.class)
                .run(context ->
                    assertThat(context).doesNotHaveBean(ControllerAdvice.class)
                );
    }
}