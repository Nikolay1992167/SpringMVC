package ru.clevertec.house.cache.factory;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.clevertec.house.cache.Cache;

@Component
@ConditionalOnProperty(prefix = "spring.cache", name = "algorithm")
public abstract class CacheFactory {

    @Lookup
    public abstract Cache<Object, Object> getCacheAlgorithm();
}
