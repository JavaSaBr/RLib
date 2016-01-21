package rlib.util.dictionary;

/**
 * Интерфейс для реализации внутреннего API словарей с объектным ключем.
 *
 * @author Ronn
 */
public interface UnsafeObjectDictionary<K, V> extends ObjectDictionary<K, V> {

    /**
     * @return массив ячеяк словаря.
     */
    public ObjectDictionaryEntry<K, V>[] content();

    /**
     * Удаление ячейки по указанному ключу.
     *
     * @param key ключ ячейки.
     * @return удаленная ячейка.
     */
    public ObjectDictionaryEntry<K, V> removeEntryForKey(final K key);
}
