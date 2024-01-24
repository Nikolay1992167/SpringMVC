package ru.clevertec.house.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;
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
public class HouseServiceConcurrencyTest extends PostgresqlTestContainer {

    private final HouseServiceImpl houseService;

    @Test
    public void checkHouseServiceWithExecutor() throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newFixedThreadPool(6);

        List<Callable<HouseResponse>> houseResponseTasks = new ArrayList<>();
        List<Callable<Void>> deleteTasks = new ArrayList<>();

        Callable<HouseResponse> saveTask = () -> houseService.save(HouseRequest.builder()
                .area("Гродненская")
                .country("Беларусь")
                .city("Островец")
                .street("Атомная")
                .number(86)
                .build());
        houseResponseTasks.add(saveTask);

        Callable<HouseResponse> findByIdTask = () -> houseService.findById(UUID.fromString("0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6"));
        houseResponseTasks.add(findByIdTask);

        Callable<HouseResponse> findByIdTaskSecond = () -> houseService.findById(UUID.fromString("0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6"));
        houseResponseTasks.add(findByIdTaskSecond);

        Callable<HouseResponse> updateTask = () -> houseService.update(UUID.fromString("9724b9b8-216d-4ab9-92eb-e6e06029580d"),
                HouseRequest.builder()
                        .area("Гродненская")
                        .country("Беларусь")
                        .city("Берестовица")
                        .street("Приграничная")
                        .number(23)
                        .build());
        houseResponseTasks.add(updateTask);

        Callable<HouseResponse> findByIdTaskThird = () -> houseService.findById(UUID.fromString("9724b9b8-216d-4ab9-92eb-e6e06029580d"));
        houseResponseTasks.add(findByIdTaskThird);

        Callable<Void> deleteTask = () -> {
            houseService.delete(UUID.fromString("e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15"));
            return null;
        };
        deleteTasks.add(deleteTask);

        List<Future<HouseResponse>> houseResponseFutures = executor.invokeAll(houseResponseTasks);

        HouseResponse savedHouse = houseResponseFutures.get(0).get();
        HouseResponse foundHouse = houseResponseFutures.get(1).get();
        HouseResponse foundHouseSecond = houseResponseFutures.get(2).get();
        HouseResponse updatedHouse = houseResponseFutures.get(3).get();
        HouseResponse foundHouseThird = houseResponseFutures.get(4).get();

        assertThat(savedHouse).isNotNull();
        assertThat(savedHouse.getArea()).isEqualTo("Гродненская");
        assertThat(savedHouse.getCountry()).isEqualTo("Беларусь");

        assertThat(foundHouse).isNotNull();
        assertThat(foundHouse.getUuid()).isEqualTo(UUID.fromString("0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6"));

        assertThat(foundHouseSecond).isNotNull();
        assertThat(foundHouseSecond.getUuid()).isEqualTo(UUID.fromString("0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6"));

        assertThat(updatedHouse).isNotNull();
        assertThat(updatedHouse.getUuid()).isEqualTo(UUID.fromString("9724b9b8-216d-4ab9-92eb-e6e06029580d"));
        assertThat(updatedHouse.getArea()).isEqualTo("Гродненская");
        assertThat(updatedHouse.getCountry()).isEqualTo("Беларусь");

        assertThat(foundHouseThird).isNotNull();
        assertThat(foundHouseThird.getUuid()).isEqualTo(UUID.fromString("9724b9b8-216d-4ab9-92eb-e6e06029580d"));

        List<Future<Void>> voidFutures = executor.invokeAll(deleteTasks);

        Future<Void> voidFuture = voidFutures.get(0);
        boolean delete = voidFuture.isDone();
        assertThat(delete).isTrue();

        executor.shutdown();
    }
}
