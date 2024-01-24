package ru.clevertec.house.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.enums.Sex;
import util.PostgresqlTestContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class PersonServiceConcurrencyTest extends PostgresqlTestContainer {

    private final PersonServiceImpl personService;

    @Test
    public void checkHouseServiceWithExecutor() throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newFixedThreadPool(6);

        List<Callable<PersonResponse>> personResponseTasks = new ArrayList<>();
        List<Callable<Void>> deleteTasks = new ArrayList<>();

        Callable<PersonResponse> saveTask = () -> personService.save(PersonRequest.builder()
                .name("Сергей")
                .surname("Васильков")
                .sex(Sex.MALE)
                .passport(Passport.builder()
                        .series("OP")
                        .number("486259")
                        .build())
                .houseUUID(UUID.fromString("9724b9b8-216d-4ab9-92eb-e6e06029580d"))
                .ownedHouses(List.of(House.builder()
                        .uuid(UUID.fromString("c8adac62-9266-486f-8540-21e976b635b3"))
                        .area("Гомельская")
                        .country("Республика Беларусь")
                        .city("Наровля")
                        .street("Лесная")
                        .number(25)
                        .build()))
                .build());
        personResponseTasks.add(saveTask);

        Callable<PersonResponse> findByIdTask = () -> personService.findById(UUID.fromString("922e0213-e543-48ef-b8cb-92592afd5100"));
        personResponseTasks.add(findByIdTask);

        Callable<PersonResponse> findByIdTaskSecond = () -> personService.findById(UUID.fromString("922e0213-e543-48ef-b8cb-92592afd5100"));
        personResponseTasks.add(findByIdTaskSecond);

        Callable<PersonResponse> updateTask = () -> personService.update(UUID.fromString("63a1faca-a963-4d4b-bfb9-2dafaedc36fe"),
                PersonRequest.builder()
                        .name("Наташа")
                        .surname("Жарова")
                        .sex(Sex.FEMALE)
                        .passport(Passport.builder()
                                .series("XC")
                                .number("433259")
                                .build())
                        .houseUUID(UUID.fromString("9724b9b8-216d-4ab9-92eb-e6e06029580d"))
                        .ownedHouses(List.of(House.builder()
                                .uuid(UUID.fromString("0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6"))
                                .area("Гомельская")
                                .country("Беларусь")
                                .city("Ельск")
                                .street("Ленина")
                                .number(2)
                                .build()))
                        .build());
        personResponseTasks.add(updateTask);

        Callable<PersonResponse> findByIdTaskThird = () -> personService.findById(UUID.fromString("f188d7be-146b-4668-a729-09a2d4fdc784"));
        personResponseTasks.add(findByIdTaskThird);

        Callable<Void> deleteTask = () -> {
            personService.delete(UUID.fromString("40291d40-5948-448c-b66a-d09591d3500f"));
            return null;
        };
        deleteTasks.add(deleteTask);

        List<Future<PersonResponse>> personResponseFutures = executor.invokeAll(personResponseTasks);

        PersonResponse savedPerson = personResponseFutures.get(0).get();
        PersonResponse foundPerson = personResponseFutures.get(1).get();
        PersonResponse foundPersonSecond = personResponseFutures.get(2).get();
        PersonResponse updatedPerson = personResponseFutures.get(3).get();
        PersonResponse foundPersonThird = personResponseFutures.get(4).get();

        assertThat(savedPerson).isNotNull();
        assertThat(savedPerson.getName()).isEqualTo("Сергей");
        assertThat(savedPerson.getSurname()).isEqualTo("Васильков");

        assertThat(foundPerson).isNotNull();
        assertThat(foundPerson.getUuid()).isEqualTo(UUID.fromString("922e0213-e543-48ef-b8cb-92592afd5100"));

        assertThat(foundPersonSecond).isNotNull();
        assertThat(foundPersonSecond.getUuid()).isEqualTo(UUID.fromString("922e0213-e543-48ef-b8cb-92592afd5100"));

        assertThat(updatedPerson).isNotNull();
        assertThat(updatedPerson.getUuid()).isEqualTo(UUID.fromString("63a1faca-a963-4d4b-bfb9-2dafaedc36fe"));
        assertThat(updatedPerson.getName()).isEqualTo("Наташа");
        assertThat(updatedPerson.getSurname()).isEqualTo("Жарова");

        assertThat(foundPersonThird).isNotNull();
        assertThat(foundPersonThird.getUuid()).isEqualTo(UUID.fromString("f188d7be-146b-4668-a729-09a2d4fdc784"));

        List<Future<Void>> voidFutures = executor.invokeAll(deleteTasks);

        Future<Void> voidFuture = voidFutures.get(0);
        boolean delete = voidFuture.isDone();
        assertThat(delete).isTrue();

        executor.shutdown();
    }
}
