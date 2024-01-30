package config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import ru.clevertec.house.mapper.HouseMapperImpl;
import ru.clevertec.house.mapper.PersonMapperImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = {HouseMapperImpl.class, PersonMapperImpl.class})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public @interface ServiceTest {
}
