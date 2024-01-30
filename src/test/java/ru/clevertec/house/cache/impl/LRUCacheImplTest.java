package ru.clevertec.house.cache.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LRUCacheImplTest {

    private LRUCacheImpl<Integer, String> lruCache;

    @BeforeEach
    public void setUp() {
        lruCache = new LRUCacheImpl<>(3);
    }

    @Test
    public void testPutAndGet() {
        lruCache.put(1, "One");
        assertEquals("One", lruCache.get(1));
    }

    @Test
    public void testRemove() {
        lruCache.put(2, "Two");
        assertEquals("Two", lruCache.get(2));
        lruCache.remove(2);
        assertNull(lruCache.get(2));
    }

    @Test
    public void testEvict() {
        lruCache.put(1, "One");
        lruCache.put(2, "Two");
        lruCache.put(3, "Three");
        lruCache.put(4, "Four");
        assertNull(lruCache.get(1));
    }
}