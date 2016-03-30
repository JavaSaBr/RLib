package rlib.util.dictionary;

/**
 * Реализация набора утильных методов для работы со словарями.
 *
 * @author Ronn
 */
public class DictionaryUtils {

    /**
     * Удаление из словаря значение по указанному ключу в блоке {@link
     * ConcurrentDictionary#writeLock()}.
     *
     * @param dictionary словарь из которого надо удлаить значение.
     * @param key        ключ по которому надо удалить значение.
     */
    public static <V> void removeToInWriteLock(ConcurrentIntegerDictionary<V> dictionary, int key) {
        dictionary.writeLock();
        try {
            dictionary.remove(key);
        } finally {
            dictionary.writeUnlock();
        }
    }
}
