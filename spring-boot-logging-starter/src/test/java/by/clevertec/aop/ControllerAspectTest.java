package by.clevertec.aop;

import by.clevertec.util.TestController;
import by.clevertec.util.TestHandler;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        ControllerAspect.class,
        AnnotationAwareAspectJAutoProxyCreator.class,
        TestController.class,
        TestHandler.class
})
class ControllerAspectTest {

    @Autowired
    private TestController controller;

    @Autowired
    private TestHandler handler;

    @SpyBean
    private ControllerAspect aspect;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private Signature signature;

    @Test
    @SneakyThrows
    void shouldCheckCorrectLoggingForClassesWithHandlerInName() {
        // given, when
        handler.test("param", 123);

        // then
        verify(aspect, times(1)).loggingMethod(any());
    }

    @Test
    @SneakyThrows
    void shouldCheckCorrectLoggingForClasses() {
        // given, when
        controller.test("param", 123);

        // then
        verify(aspect, times(1)).loggingMethod(any());
    }

    @Test
    @SneakyThrows
    void shouldCheckInvokePointcut() {
        // given
        when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringType()).thenReturn(TestController.class);
        when(proceedingJoinPoint.proceed()).thenReturn(null);
        when(proceedingJoinPoint.getTarget()).thenReturn(new TestController());

        // when
        aspect.loggingMethod(proceedingJoinPoint);

        // then
        verify(aspect, times(1)).loggingMethod(proceedingJoinPoint);
    }
}