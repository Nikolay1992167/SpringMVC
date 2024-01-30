package ru.clevertec.house.cache.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LFUCacheImplTest {

    @Autowired
    private LFUCacheImpl<Integer, String> lfuCache;

    @Test
    public void testPut() {
        // given
        String expected = "One";
        lfuCache.put(1, expected);

        // when
        String actual = lfuCache.get(1);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGet() {
        // given
        String expected = "Two";
        lfuCache.put(2, expected);

        // when
        String actual = lfuCache.get(2);

        // then
        assertThat(actual).isEqualTo(expected);
        assertThat(lfuCache.get(3)).isNull();
    }

    @Test
    public void testRemove() {
        // given
        String value = "Three";
        lfuCache.put(3, value);

        // when
        lfuCache.remove(3);

        // then
        assertThat(lfuCache.get(3)).isNull();
    }

    @Test
    public void testEvict() {
        // given, when
        for (int i = 0; i < 10; i++) {
            lfuCache.put(i, "Value" + i);
        }

        // then
        assertThat(lfuCache.get(2)).isNull();
    }
}