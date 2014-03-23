package rlib.network;

import rlib.util.Synchronized;

/**
 * Интерфейс для реализации сетевого подключения.
 * 
 * @author Ronn
 */
public interface AsynConnection<R, S> extends Synchronized {

	/**
	 * Закрытие коннекта.
	 */
	public void close();

	/**
	 * @return время последней активности коннекта.
	 */
	public long getLastActive();

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
	 * @param lastActive время последней активности коннекта.
	 */
	public void setLastActive(long lastActive);

	/**
	 * Активация ожидания чтения пакетов.
	 */
	public void startRead();
}
