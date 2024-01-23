package ru.clevertec.house.cache.impl;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.clevertec.house.cache.Cache;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Component
@Scope("prototype")
@ConditionalOnProperty(prefix = "spring.cache", name = "algorithm", havingValue = "LRUCache")
public class LRUCacheImpl<K, V> implements Cache<K, V> {

    private final Map<K, V> cacheMap;
    private final int capacity;

    public LRUCacheImpl(@Value("${spring.cache.capacity}") int capacity) {
        this.capacity = capacity;
        this.cacheMap = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }

    /**
     * Puts an object in the Cache Map.
     *
     * @param key - the key of the object for the Map cache.
     * @param value - an object by key in the cache Map.
     */
    @Override
    public void put(K key, V value) {
        cacheMap.put(key, value);
    }

    /**
     * Returns an object by key from the Map cache.
     *
     * @param key - the key of the object for the Map cache.
     * @return object based on the key passed to the Map.
     */
    @Override
    public V get(K key) {
        return cacheMap.get(key);
    }

    /**
     * Deletes an object by key from the Map cache.
     *
     * @param key - the key of the object to be deleted for the Map.
     */
    @Override
    public void remove(K key) {
        cacheMap.remove(key);
    }
}
