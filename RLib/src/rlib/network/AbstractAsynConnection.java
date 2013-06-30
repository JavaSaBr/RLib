package rlib.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.Locks;
import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.network.NetworkConfig;
import rlib.util.array.Array;
import rlib.util.array.Arrays;


/**
 * Базовая модель асинхронного конекта.
 *
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractAsynConnection<N extends AsynchronousNetwork, R, S> implements AsynConnection<R, S>
{
	protected static final Logger log = Loggers.getLogger(AsynConnection.class);

	/** модель сети, в которой этот коннект */
	protected final N network;

	/** список ожидающих пакетов на отправку */
	protected final Array<S> waitPackets;

	/** канал конекта */
	protected final AsynchronousSocketChannel channel;

	/** промежуточный буффер с присылаемыми данными */
	protected final ByteBuffer readBuffer;
	/** промежуточный буффер с отсылаемыми данными */
	protected final ByteBuffer writeBuffer;

	/** конфиг сети */
	protected final NetworkConfig config;

	/** блокировщик */
	protected final Lock lock;

	/** счетчик записи пакета */
	protected volatile int write;

	/** флаг закрытости конекта */
	protected volatile boolean closed;

	/** время последней активности конекта */
	protected long lastActive;

	/**
	 * Обработчик чтения пакетов.
	 */
	private final CompletionHandler<Integer, AbstractAsynConnection> readHandler = new CompletionHandler<Integer, AbstractAsynConnection>()
	{
		@Override
		public void completed(Integer result, AbstractAsynConnection attachment)
		{
			// если канал закрыт
			if(result.intValue() == -1)
			{
				finish();
				return;
			}

			// обновляем время последней активности
			setLastActive(System.currentTimeMillis());

			// подготавливаем к обработки
			readBuffer.flip();
			
			// если пакеты полностью прочитаны
			if(isReady(readBuffer))
				// обработка прочитанных пакетов
				readPacket(readBuffer);

			// очищаем
			readBuffer.clear();

			// ставимся опять на ожидание новых байтов
			channel.read(readBuffer, attachment, this);
		}

		@Override
		public void failed(Throwable exc, AbstractAsynConnection attachment)
		{
			if(config.isVesibleReadException())
				// выводим лог ошибки
				log.warning(this, new Exception(exc));

			// если закрыт конект, выходим
			if(isClosed())
				return;

			finish();
		}
	};

	/**
	 * Обработчик записи пакетов.
	 */
	private final CompletionHandler<Integer, S> writeHandler = new CompletionHandler<Integer, S>()
	{
		@Override
		public void completed(Integer result, S packet)
		{
			// если канал закрыт
			if(result.intValue() == -1)
			{
				finish();
				return;
			}

			// если не доконца записали
			if(writeBuffer.remaining() > 0)
			{
				// дозаписываем
				channel.write(writeBuffer, packet, this);
				return;
			}

			// обновляем время последней активности
			setLastActive(System.currentTimeMillis());

			// уменьшаем счетчик записи
			write -= 1;

			// переходим к записи следующего пакета
			writeNextPacket();
		}

		@Override
		public void failed(Throwable exc, S packet)
		{
			if(config.isVesibleWriteException())
				// записываем в лог сообщение об кривом пакете
				log.warning(this, new Exception("incorrect write packet " + packet, exc));

			if(isClosed())
				return;
			
			// уменьшаем счетчик записи
			write -= 1;

			// записываем след. пакет
			writeNextPacket();
		}
	};

	public AbstractAsynConnection(N network, AsynchronousSocketChannel channel, Class<S> sendableType)
	{
		this.lock = Locks.newLock();
		this.channel = channel;
		this.waitPackets = Arrays.toArray(sendableType);
		this.network = network;
		this.readBuffer = network.getReadByteBuffer();
		this.writeBuffer = network.getWriteByteBuffer();
		this.config = network.getConfig();
	}

	@Override
	public void close()
	{
		lock.lock();
		try
		{
			if(isClosed())
				return;

			// если конал открыт
			if(channel.isOpen())
				// закрываем канал
				channel.close();

			// ставим флаг закрытия
			setClosed(true);

			// слаживаем в пул читаемый буффер
			network.putReadByteBuffer(readBuffer);
			// слаживаем в пул записываемый буффер
			network.putWriteByteBuffer(writeBuffer);

			// очищаем от ожидающихся пакетов
			waitPackets.clear();
		}
		catch(IOException e)
		{
			log.warning(this, e);
		}
		finally
		{
			lock.unlock();
		}
	}

	/**
	 * Обработка остановки коннекта.
	 */
	protected abstract void finish();

	@Override
	public final long getLastActive()
	{
		return lastActive;
	}

	/**
	 * @return буффер для чтения пакета.
	 */
	protected final ByteBuffer getReadBuffer()
	{
		return readBuffer;
	}

	/**
	 * @return буффер для записи пакета.
	 */
	protected final ByteBuffer getWriteBuffer()
	{
		return writeBuffer;
	}

	@Override
	public final boolean isClosed()
	{
		return closed;
	}
	
	/**
	 * Проверка готовности пакета к обработке.
	 * 
	 * @param buffer прочтенный пакет.
	 * @return можно ли начинать обработку.
	 */
	protected boolean isReady(ByteBuffer buffer)
	{
		return true;
	}

	/**
	 * Перенос данных с пакета в буффер.
	 *
	 * @param packet отправляемый пакет.
	 * @param buffer буффер, в который надо перенести.
	 */
	protected abstract void movePacketToBuffer(S packet, ByteBuffer buffer);

	/**
	 * Обработка завершения отправки пакета.
	 * 
	 * @param packet отправленый пакет.
	 */
	protected abstract void onWrited(S packet);

	/**
	 * Чтение и обработка клиентского пакета.
	 *
	 * @param buffer буффер данных.
	 */
	protected abstract void readPacket(ByteBuffer buffer);

	@Override
	public final void sendPacket(S packet)
	{
		lock.lock();
		try
		{
			// добавлям пакет в список ожидающих
			waitPackets.add(packet);
		}
		finally
		{
			lock.unlock();
		}

		// пробуем записать пакет
		writeNextPacket();
	}

	/**
	 * @param closed закрыт ли конект.
	 */
	protected final void setClosed(boolean closed)
	{
		this.closed = closed;
	}
	
	@Override
	public final void setLastActive(long lastActive)
	{
		this.lastActive = lastActive;
	}
	
	@Override
	public final void startRead()
	{
		// очищаем на всякий буффер
		readBuffer.clear();
		// ложим на ожидание клиентских пакетов
		channel.read(readBuffer, this, readHandler);
	}
	
	/**
	 * Запись следующего ожидающего пакета.
	 */
	protected final void writeNextPacket()
	{
		lock.lock();
		try
		{
			if(isClosed() || write > 0)
				return;

			// пытаемся извлеч ожидающий записи пакет
			S waitPacket = waitPackets.poll();

			// если ожидающих пакетов нет, выходим
			if(waitPacket == null)
				return;

			// увеличиваем счетчик записи
			write += 1;

			// переносим данные с пакета в буффер
			movePacketToBuffer(waitPacket, writeBuffer);

			// отправляем на запись пакет
			channel.write(writeBuffer, waitPacket, writeHandler);

			// обрабатываем завершение отправки
			onWrited(waitPacket);
		}
		finally
		{
			lock.unlock();
		}
	}
}
