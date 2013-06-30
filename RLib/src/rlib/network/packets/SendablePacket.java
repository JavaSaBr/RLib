package rlib.network.packets;

import java.nio.ByteBuffer;

/**
 * Интерфейс для реализации отправляемых пакетов.
 *
 * @author Ronn
 */
public interface SendablePacket<C> extends Packet<C>
{
	/**
	 * Завершение записи в буффер.
	 */
	public void complete();

	/**
	 * Уменьшить счетчик отправлений.
	 */
	public void decreaseSends();

	/**
	 * Уменьшить счетчик отправлений.
	 */
	public void decreaseSends(int count);

	/**
	 * Увеличить счетчик отправлений пакета.
	 */
	public void increaseSends();

	/**
	 * Увеличить счетчик отправлений пакета.
	 */
	public void increaseSends(int count);

	/**
	 * @return записывать ли синхронно пакет.
	 */
	public boolean isSynchronized();

	/**
	 * Записать пакет в указанный буфер.
	 *
	 * @param buffer локальный буффер.
	 */
	public void write(ByteBuffer buffer);

	/**
	 * Записать размер пакета вначало пакета.
	 *
	 * @param length размер пакета.
	 */
	public void writeHeader(ByteBuffer buffer, int length);

	/**
	 * Записать в буффер, который занесен в поле пакета.
	 */
	public void writeLocal();

	/**
	 * Установка на позицию для записи пакета.
	 */
	public void writePosition(ByteBuffer buffer);
}