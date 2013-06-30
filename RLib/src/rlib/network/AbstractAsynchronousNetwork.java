package rlib.network;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.util.array.Array;
import rlib.util.array.Arrays;


/**
 * Базовая модель асинхронной сети.
 * 
 * @author Ronn
 */
public abstract class AbstractAsynchronousNetwork implements AsynchronousNetwork
{
	protected static final Logger log = Loggers.getLogger(AsynchronousNetwork.class);
	
	/** пул буфферов для чтения */
	protected final Array<ByteBuffer> readBufferPool;
	/** пул буфферов для записи */
	protected final Array<ByteBuffer> writeBufferPool;
	
	/** конфигурация сети */
	protected final NetworkConfig config;
	
	protected AbstractAsynchronousNetwork(NetworkConfig config)
	{
		// запоминаем конфиг
		this.config = config;
		// создаем пул читаемых буфферов
		this.readBufferPool = Arrays.toConcurrentArray(ByteBuffer.class);
		// создаем пул записываемых буфферов
		this.writeBufferPool = Arrays.toConcurrentArray(ByteBuffer.class);
	}
	
	@Override
	public NetworkConfig getConfig()
	{
		return config;
	}

	@Override
	public ByteBuffer getReadByteBuffer()
	{
		readBufferPool.writeLock();
		try
		{
			// извлекаем свободный буффер из пула
			ByteBuffer buffer = readBufferPool.pop();

			// если буффера нети
			if(buffer == null)
				// создаем новый
				buffer = ByteBuffer.allocate(config.getReadBufferSize()).order(ByteOrder.LITTLE_ENDIAN);

			// возвращаем буффер
			return buffer;
		}
		finally
		{
			readBufferPool.writeUnlock();
		}
	}

	@Override
	public ByteBuffer getWriteByteBuffer()
	{
		writeBufferPool.writeLock();
		try
		{
			// извлекаем свободный буффер из пула
			ByteBuffer buffer = writeBufferPool.pop();

			// если буффера нети
			if(buffer == null)
				// создаем новый
				buffer = ByteBuffer.allocate(config.getWriteBufferSize()).order(ByteOrder.LITTLE_ENDIAN);

			// возвращаем буффер
			return buffer;
		}
		finally
		{
			writeBufferPool.writeUnlock();
		}
	}

	@Override
	public void putReadByteBuffer(ByteBuffer buffer)
	{
		if(buffer == null)
			return;

		// добавляем в пул
		readBufferPool.add(buffer);
	}

	@Override
	public void putWriteByteBuffer(ByteBuffer buffer)
	{
		if(buffer == null)
			return;

		// добавляем в пул
		writeBufferPool.add(buffer);
	}
}
