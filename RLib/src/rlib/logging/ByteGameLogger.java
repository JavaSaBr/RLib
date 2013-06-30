package rlib.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.Locks;
import rlib.util.Synchronized;


/**
 * Модель логгера игровых событий.
 *
 * @author Ronn
 */
public class ByteGameLogger implements GameLogger, Synchronized
{
	private static final Logger log = Loggers.getLogger(ByteGameLogger.class);

	/** синхронизатор */
	private final Lock lock;
	/** кэш записей */
	private final ByteBuffer cache;
	/** средство записи в фаил */
	private final FileOutputStream out;
	/** канал записи в фаил */
	private final FileChannel channel;

	/**
	 * @param outFile выходной фаил.
	 * @throws IOException
	 */
	protected ByteGameLogger(File outFile) throws IOException
	{
		if(!outFile.exists())
			outFile.createNewFile();

		this.out = new FileOutputStream(outFile);
		this.channel = out.getChannel();
		this.lock = Locks.newLock();
		this.cache = ByteBuffer.allocate(1024 * 1024).order(ByteOrder.LITTLE_ENDIAN);
	}

	@Override
	public void finish()
	{
		lock.lock();
		try
		{
			writeCache();
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public void lock()
	{
		lock.lock();
	}

	@Override
	public void unlock()
	{
		lock.unlock();
	}

	@Override
	public void write(String text)
	{
		lock.lock();
		try
		{
			if(cache.remaining() < text.length() * 2)
				writeCache();

			for(int i = 0, length = text.length(); i < length; i++)
				cache.putChar(text.charAt(i));
		}
		finally
		{
			lock.unlock();
		}
	}

	/**
	 * Запись байта в лог.
	 */
	public void writeByte(int value)
	{
		if(cache.remaining() < 1)
			writeCache();

		cache.put((byte) value);
	}

	@Override
	public void writeCache()
	{
		try
		{
			// подготавливаем кэш
			cache.flip();
			// записываем его
			channel.write(cache);
			// очищаем его
			cache.clear();
			// обновляем файл
			out.flush();
		}
		catch(IOException e)
		{
			log.warning(e);
		}
	}

	/**
	 * Запись флоат числа в лог.
	 */
	public void writeFloat(float value)
	{
		if(cache.remaining() < 4)
			writeCache();

		cache.putFloat(value);
	}

	/**
	 * Запись инт числа в лог.
	 */
	public void writeInt(int value)
	{
		if(cache.remaining() < 4)
			writeCache();

		cache.putInt(value);
	}
}
