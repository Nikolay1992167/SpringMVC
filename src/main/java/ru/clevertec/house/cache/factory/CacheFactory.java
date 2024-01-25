package ru.clevertec.house.cache.factory;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.clevertec.house.cache.Cache;

@Component
@ConditionalOnProperty(prefix = "spring.cache", name = "algorithm")
public abstract class CacheFactory {

    /**
     * Creates the {@link Cache} object, which defines the algorithm for working with the cache.
     *
     * @return object {@link Cache} containing caching logic.
     */
    @Lookup
    public abstract Cache<Object, Object> getCacheAlgorithm();
}
