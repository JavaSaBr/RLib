package rlib.util.pools;

/**
 * Интерфейс для реализации методов по финалзации и переинициализации используемых объектов совместо
 * с {@link ReusablePool}.
 *
 * @author JavaSaBr
 * @see ReusablePool
 */
public interface Reusable {

    /**
     * Очистка объекта перед завершением работы с ним.
     */
    public default void free() {
    }

    /**
     * Подготовка объекта к новому переиспользованию.
     */
    public default void reuse() {
    }

    /**
     * Завершение испольозвания объекта.
     */
    public default void release() {
    }
}
