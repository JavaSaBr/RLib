package rlib.network.packets;

import java.nio.ByteBuffer;

/**
 * Интерфейс для реализации сетевого пакета на стороне сервера.
 * 
 * @author Ronn
 */
public interface Packet<C> {

	/**
	 * @return буффер данных.
	 */
	public ByteBuffer getBuffer();

	/**
	 * @return тип пакета.
	 */
	public String getName();

	/**
	 * @return владелец пакета.
	 */
	public C getOwner();

	/**
	 * @param buffer буффер данных.
	 */
	public void setBuffer(ByteBuffer buffer);

	/**
	 * Установка владельца пакета.
	 * 
	 * @param owner владелец пакета..
	 */
	public void setOwner(C owner);
}
