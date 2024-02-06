package ru.clevertec.house.cache;

public interface Cache<K, V> {

    /**
     * Saves or updates an object of type V with the specified K.
     * @param key id of an object of type K
     * @param value an object of type V that needs to be saved or updated
     */
    void put(K key, V value);

    /**
     * Returns an object of type V by the specified identifier.
     * @param key id of an object of type K
     * @return an object of type V or empty if no such object exists
     */
    V get(K key);

    /**
     * Deletes an object of type V with the specified identifier.
     * @param key id of an object of type K
     */
    void remove(K key);
}
