package ru.clevertec.house.cache.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class LFUCacheImplTest {

    @Autowired
    private LFUCacheImpl<Integer, String> lfuCache;

    @Test
    public void testPut() {
        lfuCache.put(1, "One");
        assertEquals("One", lfuCache.get(1));
    }

    @Test
    public void testGet() {
        lfuCache.put(2, "Two");
        assertEquals("Two", lfuCache.get(2));
        assertNull(lfuCache.get(3));
    }

    @Test
    public void testRemove() {
        lfuCache.put(3, "Three");
        assertEquals("Three", lfuCache.get(3));
        lfuCache.remove(3);
        assertNull(lfuCache.get(3));
    }

    @Test
    public void testEvict() {
        for (int i = 0; i < 10; i++) {
            lfuCache.put(i, "Value" + i);
        }
        assertNull(lfuCache.get(2));
    }
}