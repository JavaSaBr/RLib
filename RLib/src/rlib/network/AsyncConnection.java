package rlib.network;

import rlib.concurrent.lock.Lockable;

/**
 * Интерфейс для реализации сетевого подключения.
 *
 * @author JavaSaBr
 */
public interface AsyncConnection<R, S> extends Lockable {

    /**
     * Закрытие коннекта.
     */
    public void close();

    /**
     * @return время последней активности коннекта.
     */
    public long getLastActive();

    /**
     * @param lastActive время последней активности коннекта.
     */
    public void setLastActive(long lastActive);

    /**
     * @return закрыт ли коннект.
     */
    public boolean isClosed();

    /**
     * Добавить пакет в очередь на отправку.
     *
     * @param packet отправляемый пакет.
     */
    public void sendPacket(S packet);

    /**
     * Активация ожидания чтения пакетов.
     */
    public void startRead();
}
