package rlib.util.dictionary;

/**
 * Интерфейс для реализации конкуретного словаря с примитивным ключем int.
 * 
 * @author Ronn
 */
public interface ConcurrentIntegerDictionary<V> extends IntegerDictionary<V>, ConcurrentDictionary<IntKey, V> {

}
