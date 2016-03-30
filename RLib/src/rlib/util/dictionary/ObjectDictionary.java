package rlib.util.dictionary;

import rlib.util.array.Array;

/**
 * Интерфейс для реализации словаря с объектным ключем.
 *
 * @author Ronn
 */
public interface ObjectDictionary<K, V> extends Dictionary<K, V> {

    /**
     * Проверка наличия значения в словаре по указанному ключу.
     *
     * @param key проверяемый ключ.
     */
    public default boolean containsKey(final K key) {
        throw new RuntimeException("not supported.");
    }

    /**
     * Вовзращает значения по указанному ключу.
     *
     * @param key ключ.
     */
    public default V get(final K key) {
        throw new RuntimeException("not supported.");
    }

    /**
     * @param container контейнер для ключей.
     * @return массив ключей словаря.
     */
    public default Array<K> keyArray(final Array<K> container) {
        throw new RuntimeException("not supported.");
    }

    /**
     * @return массив ключей словаря.
     */
    public default Array<K> keyArray(final Class<K> type) {
        throw new RuntimeException("not supported.");
    }

    /**
     * Добавляет новое значение по указанному ключу, и если уже есть элемент с таким ключем,
     * возвращает его.
     *
     * @param key   ключ значения.
     * @param value вставляемое значение.
     */
    public default V put(final K key, final V value) {
        throw new RuntimeException("not supported.");
    }

    /**
     * Удаляет значение по ключу.
     *
     * @param key ключ значения.
     */
    public default V remove(final K key) {
        throw new RuntimeException("not supported.");
    }
}
