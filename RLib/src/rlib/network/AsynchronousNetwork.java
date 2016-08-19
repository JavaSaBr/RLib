package rlib.network;

import java.nio.ByteBuffer;

/**
 * Интерфейс для реализации модели асинхронной сети.
 *
 * @author JavaSaBr
 */
public interface AsynchronousNetwork {

    /**
     * @return конфигурация сети.
     */
    public NetworkConfig getConfig();

    /**
     * @return получить из пула свободный буфер для чтения.
     */
    public ByteBuffer takeReadBuffer();

    /**
     * @return получить из пула свободный буффер для записи.
     */
    public ByteBuffer takeWriteBuffer();

    /**
     * @param buffer складываем освобожденнй буффер чтения в пул.
     */
    public void putReadBuffer(ByteBuffer buffer);

    /**
     * @param buffer складываем освобожденнй буффер записи в пул.
     */
    public void putWriteBuffer(ByteBuffer buffer);
}
