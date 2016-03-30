package rlib.util.dictionary;

import rlib.util.array.IntegerArray;

/**
 * Интерфейс для реализации словаря с примитивным ключем int.
 *
 * @author Ronn
 */
public interface IntegerDictionary<V> extends Dictionary<IntKey, V> {

    /**
     * Проверка наличия значения в словаре по указанному ключу.
     *
     * @param key проверяемый ключ.
     */
    public default boolean containsKey(final int key) {
        throw new RuntimeException("not supported.");
    }

    /**
     * Вовзращает значения по указанному ключу.
     *
     * @param key ключ.
     */
    public default V get(final int key) {
        throw new RuntimeException("not supported.");
    }

    /**
     * @return массив ключей словаря.
     */
    public default IntegerArray keyIntegerArray() {
        throw new RuntimeException("not supported.");
    }

    /**
     * @param container контейнер для ключей.
     * @return массив ключей таблицы.
     */
    public default IntegerArray keyIntegerArray(final IntegerArray container) {
        throw new RuntimeException("not supported.");
    }

    /**
     * Добавляет новое значение по указанному ключу, и если уже есть элемент с таким ключем,
     * возвращает его.
     *
     * @param key   ключ значения.
     * @param value вставляемое значение.
     */
    public default V put(final int key, final V value) {
        throw new RuntimeException("not supported.");
    }

    /**
     * Удаляет значение по ключу.
     *
     * @param key ключ значения.
     */
    public default V remove(final int key) {
        throw new RuntimeException("not supported.");
    }

}
