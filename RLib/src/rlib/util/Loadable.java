package rlib.util;

/**
 * Интерфейс для реализации загружаемых объектов.
 *
 * @author JavaSaBr
 */
public interface Loadable {

    /**
     * Загруть данные.
     */
    public default void load() {
    }

    /**
     * Перезагрузить данные.
     */
    public default void reload() {
    }

    /**
     * Выгрузить даные.
     */
    public default void unload() {
    }
}
