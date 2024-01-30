package ru.clevertec.house.cache.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LRUCacheImplTest {

    private LRUCacheImpl<Integer, String> lruCache;

    @BeforeEach
    public void setUp() {
        lruCache = new LRUCacheImpl<>(3);
    }

    @Test
    public void testPutAndGet() {
        // given
        String expected = "One";
        lruCache.put(1, expected);

        // when
        String actual = lruCache.get(1);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testRemove() {
        // given
        String expected = "Two";
        lruCache.put(2, expected);

        // when
        lruCache.remove(2);

        // then
        assertThat(lruCache.get(2)).isNull();
    }

    @Test
    public void testEvict() {
        // given, when
        lruCache.put(1, "One");
        lruCache.put(2, "Two");
        lruCache.put(3, "Three");
        lruCache.put(4, "Four");

        // then
        assertThat(lruCache.get(1)).isNull();
    }
}