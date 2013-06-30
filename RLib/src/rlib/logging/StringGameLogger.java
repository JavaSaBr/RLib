package rlib.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.Locks;
import rlib.util.array.Array;
import rlib.util.array.Arrays;


/**
 * Модель строкового логера игровых событий.
 *
 * @author Ronn
 */
public class StringGameLogger implements GameLogger
{
	private static final Logger log = Loggers.getLogger(StringGameLogger.class);

	/** синхронизатор */
	private final Lock lock;
	/** кэш записей */
	private final Array<String> cache;
	/** форматтер времени */
	private final SimpleDateFormat timeFormat;
	/** текущая дата */
	private final Date date;
	/** средство записи в фаил */
	private final Writer out;

	/**
	 * @param outFile выходной фаил.
	 * @throws IOException
	 */
	protected StringGameLogger(File outFile) throws IOException
	{
		this.timeFormat  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		this.date = new Date();

		if(!outFile.exists())
			outFile.createNewFile();

		this.out = new FileWriter(outFile);
		this.lock = Locks.newLock();
		this.cache = Arrays.toArray(String.class);
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
	public void write(String text)
	{
		lock.lock();
		try
		{
			// если кеш переполнен
			if(cache.size() > 1000)
				// записываем кеш
				writeCache();

			// указываем текущее время
			date.setTime(System.currentTimeMillis());
			// вносим запись
			cache.add(timeFormat.format(date) + ": " + text + "\n");
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public void writeCache()
	{
		try
		{
			// получаем массив ожидающих записи строк
			String[] array = cache.array();

			// аписываем их в файл
			for(int i = 0, length = cache.size(); i < length; i++)
				out.write(array[i]);

			// очищаем кэш
			cache.clear();

			// обновляем файл
			out.flush();
		}
		catch(IOException e)
		{
			log.warning(e);
		}
	}
}
