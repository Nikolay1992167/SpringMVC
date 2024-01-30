package ru.clevertec.house.cache.impl;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.clevertec.house.cache.Cache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Component
@Scope("prototype")
@ConditionalOnProperty(prefix = "spring.cache", name = "algorithm", havingValue = "LFUCache")
public class LFUCacheImpl<K, V> implements Cache<K, V> {

    private final int maxSize;
    private final Map<K, V> cacheMap;
    private final Map<K, Integer> keyFrequency;
    private final Map<Integer, Set<K>> frequencyKeys;
    private int minFrequency;

    public LFUCacheImpl(@Value("${spring.cache.capacity}") int maxSize) {
        this.maxSize = maxSize;
        this.cacheMap = new HashMap<>();
        this.keyFrequency = new HashMap<>();
        this.frequencyKeys = new HashMap<>();
        this.minFrequency = 0;
        this.frequencyKeys.put(1, new LinkedHashSet<>());
    }

    /**
     * Puts an object in the Cache Map. If necessary, it calls the method for
     * removing the redundant object from the Map cache.
     * @param key - the key of the object for the Map cache.
     * @param value - an object by key in the cache Map.
     */
    @Override
    public void put(K key, V value) {
        if (maxSize > 0) {
            if (cacheMap.containsKey(key)) {
                cacheMap.put(key, value);
            } else {
                if (cacheMap.size() >= maxSize) {
                    evict();
                }
                cacheMap.put(key, value);
                keyFrequency.put(key, 1);
                frequencyKeys.get(1).add(key);
                minFrequency = 1;
            }
        }
    }

    /**
     * Returns an object by key from the Map cache. Updates the usage frequency value
     * for the returned object. Updates the Usage Frequency Map.
     * @param key - the key of the object for the Map cache.
     * @return object based on the key passed to the Map.
     */
    @Override
    public V get(K key) {
        if (!cacheMap.containsKey(key)) {
            return null;
        }
        int frequency = keyFrequency.get(key);
        keyFrequency.put(key, frequency + 1);
        frequencyKeys.get(frequency).remove(key);
        if (frequency == minFrequency && frequencyKeys.get(frequency).isEmpty()) {
            minFrequency++;
        }
        if (!frequencyKeys.containsKey(frequency + 1)) {
            frequencyKeys.put(frequency + 1, new LinkedHashSet<>());
        }
        frequencyKeys.get(frequency + 1).add(key);
        return cacheMap.get(key);
    }

    /**
     * Deletes the key object from the Map cache and updates information about the frequency of key usage.
     *
     * @param key - the key to be deleted from the cache.
     */
    @Override
    public void remove(K key) {
        if (cacheMap.containsKey(key)) {
            int frequency = keyFrequency.get(key);
            keyFrequency.remove(key);
            frequencyKeys.get(frequency).remove(key);
            cacheMap.remove(key);
            if (frequency == minFrequency && frequencyKeys.get(frequency).isEmpty()) {
                minFrequency++;
            }
        }
    }

    /**
     * Deletes the key object with the lowest frequency of use from the cache.
     */
    private void evict() {
        K keyToRemove = frequencyKeys.get(minFrequency).iterator().next();
        frequencyKeys.get(minFrequency).remove(keyToRemove);
        cacheMap.remove(keyToRemove);
        keyFrequency.remove(keyToRemove);
    }
}
