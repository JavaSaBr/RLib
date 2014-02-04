package rlib.network;

import java.nio.ByteBuffer;

import rlib.network.NetworkConfig;

/**
 * Интерфейс для реализации модели асинхронной сети.
 * 
 * @author Ronn
 */
public interface AsynchronousNetwork {

	/**
	 * @return конфигурация сети.
	 */
	public NetworkConfig getConfig();

	/**
	 * @return получить из пула свободный буфер для чтения.
	 */
	public ByteBuffer getReadByteBuffer();

	/**
	 * @return получить из пула свободный буффер для записи.
	 */
	public ByteBuffer getWriteByteBuffer();

	/**
	 * @param buffer складываем освобожденнй буффер чтения в пул.
	 */
	public void putReadByteBuffer(ByteBuffer buffer);

	/**
	 * @param buffer складываем освобожденнй буффер записи в пул.
	 */
	public void putWriteByteBuffer(ByteBuffer buffer);
}
