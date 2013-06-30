package rlib.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.Locks;
import rlib.util.array.Arrays;
import rlib.util.table.Table;
import rlib.util.table.Tables;


/**
 * Менеджер логгеров, служит для самого процесса вывода и сохранения сообщений,
 * а так же и для создания индивидуальных логгеров
 *
 * @author Ronn
 */
public abstract class Loggers
{
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	/** таблица всех логгерров */
	private static final Table<String, Logger> loggers = Tables.newObjectTable();

	/** блокировщик */
	private static final Lock lock = Locks.newLock();

	/** главный логгер */
	private static final Logger log = new Logger();

	/** массив слушателей логгера */
	private static LoggerListener[] listeners;

	/** записывальщик сообщений в фаил */
	private static Writer out;

	/**
	 * Добавление слушателя к логгерам.
	 *
	 * @param listener слушатель.
	 */
	public static void addListener(LoggerListener listener)
	{
		listeners = Arrays.addToArray(listeners, listener, LoggerListener.class);
	}

	/**
	 * Создает индивидуальный логгер с указаным именем.
	 *
	 * @param cs класс, который запрашивает логгер
	 * @return новый индивидуальный логгер
	 */
	public static final Logger getLogger(Class<?> cs)
	{
		Logger logger = new Logger(cs.getSimpleName());

		loggers.put(cs.getSimpleName(), logger);

		return logger;
	}

	/**
	 * Создает индивидуальный логгер с указаным именем.
	 *
	 * @param name имя объекта который запрашивает логгер
	 * @return новый индивидуальный логгер
	 */
	public static final Logger getLogger(String name)
	{
		Logger logger = new Logger(name);

		loggers.put(name, logger);

		return logger;
	}

	/**
	 * Вывод информативного сообщение.
	 *
	 * @param class класс объекта, посылающего сообщение
	 * @param message содержание сообщения
	 */
	public static final void info(Class<?> cs, String message)
	{
		log.info(cs, message);
	}

	/**
	 * Вывод информативного сообщение.
	 *
	 * @param owner объект, посылающий сообщение
	 * @param message содержание сообщения
	 */
	public static final void info(Object owner, String message)
	{
		log.info(owner, message);
	}

	/**
	 * Вывод информативного сообщение.
	 *
	 * @param name имя объекта, посылающего сообщение
	 * @param message содержание сообщения
	 */
	public static final void info(String name, String message)
	{
		log.info(name, message);
	}

	/**
	 * Процесс вывода сообщения в консоль и по возможности в фаил
	 *
	 * @param text содержание сообщения
	 */
	public synchronized static final void println(String text)
	{
		if(listeners != null)
			for(int i = 0, length = listeners.length; i < length; i++)
				listeners[i].println(text);

		if(out != null)
		{
			lock.lock();
			try
			{
				out.write(text + "\n");
				out.flush();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				lock.unlock();
			}
		}

		System.err.println(text);
	}

	/**
	 * Установка файла для сохранения сообщений.
	 * При существования указанного файла, будет создан новый с изминенным именем.
	 *
	 * @param projectPath путь к папке log.
	 * @param использовать ли запись в файл лога.
	 */
	public static final void setFile(String projectPath, boolean val)
	{
		if(!val)
			return;

		// создаем ссылку на дерикторию логов
		File directory = new File(projectPath + "/log/");

		// если дериктории нету
		if(!directory.exists())
			// создаем
			directory.mkdir();

		try
		{
			// создаем ссылку на будущий файл лога
			File file = new File(directory.getAbsolutePath() + "/" + timeFormat.format(new Date()) + ".log");

			// если такого нет
			if(!file.exists())
				// создаем
				file.createNewFile();

			// создаем записчик лога в нужный нам файл
			out = new FileWriter(new File(projectPath + "/log/" + timeFormat.format(new Date()) + ".log"), true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			out = null;
		}
	}

	/**
	 * Вывод важного эксепшена.
	 *
	 * @param class класс объекта, посылающего сообщение
	 * @param exception сам эксепшен.
	 */
	public static void warning(Class<?> cs, Exception exception)
	{
		log.warning(cs, exception);
	}

	/**
	 * Вывод важного сообщение.
	 *
	 * @param class класс объекта, посылающего сообщение
	 * @param message содержание сообщения
	 */
	public static final void warning(Class<?> cs, String message)
	{
		log.warning(cs, message);
	}

	/**
	 * Вывод важного эксепшена.
	 *
	 * @param owner объект, посылающий эксепшен.
	 * @param exception сам эксепшен.
	 */
	public static void warning(Object owner, Exception exception)
	{
		log.warning(owner, exception);
	}

	/**
	 * Вывод важного сообщение.
	 *
	 * @param owner объект, посылающий сообщение
	 * @param message содержание сообщения
	 */
	public static final void warning(Object owner, String message)
	{
		log.warning(owner, message);
	}

	/**
	 * Вывод важного эксепшена.
	 *
	 * @param name имя объекта, посылающего сообщение
	 * @param exception сам эксепшен.
	 */
	public static void warning(String name, Exception exception)
	{
		log.warning(name, exception);
	}

	/**
	 * Вывод важного сообщение.
	 *
	 * @param name имя объекта, посылающего сообщение
	 * @param message содержание сообщения
	 */
	public static final void warning(String name, String message)
	{
		log.warning(name, message);
	}
}
