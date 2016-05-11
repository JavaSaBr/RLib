package rlib.util.dictionary;

/**
 * Интерфейс для реализации внутреннего API словарей с примитивным ключем int.
 *
 * @author Ronn
 */
public interface UnsafeIntegerDictionary<V> extends IntegerDictionary<V> {

    /**
     * @return массив ячеяк.
     */
    public IntegerEntry<V>[] content();

    /**
     * Удаление значения из ячейки по указанному ключу.
     *
     * @param key ключ ячейки.
     * @return удаленная ячейка.
     */
    public IntegerEntry<V> removeEntryForKey(final int key);
}
