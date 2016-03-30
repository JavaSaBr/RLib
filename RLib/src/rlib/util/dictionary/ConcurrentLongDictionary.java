package rlib.util.dictionary;

/**
 * Интерфейс для реализации конкуретного словаря с примитивным ключем long.
 *
 * @author Ronn
 */
public interface ConcurrentLongDictionary<V> extends LongDictionary<V>, ConcurrentDictionary<LongKey, V> {

}
