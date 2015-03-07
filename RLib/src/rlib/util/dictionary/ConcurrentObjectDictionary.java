package rlib.util.dictionary;

/**
 * Интерфейс для реализации конкуретного словаря с объектным ключем.
 * 
 * @author Ronn
 */
public interface ConcurrentObjectDictionary<K, V> extends ObjectDictionary<K, V>, ConcurrentDictionary<K, V> {

}
