package rlib.util;

/**
 * Интерфейс для блокируемых объектов.
 *
 * @author Ronn
 */
public interface Lockable {

    /**
     * Заблокировать изминения.
     */
    public void lock();

    /**
     * Разблокировать изминения.
     */
    public void unlock();
}
