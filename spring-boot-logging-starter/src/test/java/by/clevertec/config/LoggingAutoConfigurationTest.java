package by.clevertec.config;

import by.clevertec.aop.ControllerAspect;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class LoggingAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    public void shouldReturnControllerAspectBeanIfEnabledTrue() {
        contextRunner.withPropertyValues("aop.logging.enabled=true")
                .withUserConfiguration(LoggingAutoConfiguration.class)
                .run(context ->
                    assertThat(context).hasSingleBean(ControllerAspect.class)
                );
    }

    @Test
    public void shouldCheckNotCreateControllerAspectBeanIfEnabledFalse() {
        contextRunner.withPropertyValues("aop.logging.enabled=false")
                .withUserConfiguration(LoggingAutoConfiguration.class)
                .run(context ->
                    assertThat(context).doesNotHaveBean(ControllerAspect.class)
                );
    }
}