package ru.clevertec.house.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.proxy.CacheAspectHouse;
import util.HouseTestData;
import util.PostgresqlTestContainer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static util.initdata.ConstantsForHouse.HOUSE_UUID;

@Slf4j
@RequiredArgsConstructor
public class HouseServiceConcurrencyTest extends PostgresqlTestContainer {

    private final HouseServiceImpl houseService;

    @MockBean
    private CacheAspectHouse cacheAspectHouse;

    @Test
    public void testMultithreadedCache() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(6);
        CountDownLatch latch = new CountDownLatch(6);
        AtomicInteger counter = new AtomicInteger(0);

        for (int i = 0; i < 6; i++) {
            executorService.submit(() -> {
                try {
                    int count = counter.incrementAndGet();

                    if (count == 1) {
                        HouseRequest request = HouseTestData.builder().build().getRequestDto();
                        HouseResponse response = houseService.save(request);
                        log.info("Response: {}", response);

                    } else if (count >= 2 && count <= 4) {
                        HouseResponse response = houseService.findById(HOUSE_UUID);
                        log.info("Response: {}", response);

                    } else if (count == 5) {
                        HouseRequest request = HouseTestData.builder().build().getRequestDto();
                        HouseResponse response = houseService.update(HOUSE_UUID, request);
                        log.info("Response: {}", response);

                    } else if (count == 6) {
                        houseService.delete(HOUSE_UUID);
                        log.info("Deleted");
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        verify(cacheAspectHouse, times(3)).cacheGet(any());
    }
}
