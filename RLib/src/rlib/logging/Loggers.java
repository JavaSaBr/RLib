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
import rlib.util.array.ArrayUtils;
import rlib.util.table.Table;
import rlib.util.table.Tables;

/**
 * Менеджер логгеров, служит для самого процесса вывода и сохранения сообщений,
 * а так же и для создания индивидуальных логгеров
 * 
 * @author Ronn
 */
public abstract class Loggers {

	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	/** таблица всех логгерров */
	private static final Table<String, Logger> LOGGERS = Tables.newObjectTable();
	/** список дополнительных записчиков лога */
	private static final Array<Writer> WRITERS = ArrayUtils.toArray(Writer.class);

	/** синхронизатор записи лога */
	private static final Lock SYNC = Locks.newLock();
	/** главный логгер */
	private static final Logger LOGGER = new Logger();

	/** список слушателей логгера */
	private static LoggerListener[] listeners;

	/**
	 * Добавление слушателя к логгерам.
	 * 
	 * @param listener слушатель.
	 */
	public static void addListener(LoggerListener listener) {
		listeners = ArrayUtils.addToArray(listeners, listener, LoggerListener.class);
	}

	/**
	 * Добавление дополнительного вывода для лога.
	 * 
	 * @param writer записчик лога.
	 */
	public static void addWriter(Writer writer) {
		WRITERS.add(writer);
		WRITERS.trimToSize();
	}

	/**
	 * @return массив слушателей логгера.
	 */
	public static LoggerListener[] getListeners() {
		return listeners;
	}

	/**
	 * Создает индивидуальный логгер с указаным именем.
	 * 
	 * @param cs класс, который запрашивает логгер
	 * @return новый индивидуальный логгер
	 */
	public static final Logger getLogger(Class<?> cs) {
		Logger logger = new Logger(cs.getSimpleName());
		LOGGERS.put(cs.getSimpleName(), logger);
		return logger;
	}

	/**
	 * Создает индивидуальный логгер с указаным именем.
	 * 
	 * @param name имя объекта который запрашивает логгер
	 * @return новый индивидуальный логгер
	 */
	public static final Logger getLogger(String name) {
		Logger logger = new Logger(name);
		LOGGERS.put(name, logger);
		return logger;
	}

	/**
	 * @return список дополнительных записчиков лога.
	 */
	public static Array<Writer> getWriters() {
		return WRITERS;
	}

	/**
	 * Вывод информативного сообщение.
	 * 
	 * @param class класс объекта, посылающего сообщение
	 * @param message содержание сообщения
	 */
	public static final void info(Class<?> cs, String message) {
		LOGGER.info(cs, message);
	}

	/**
	 * Вывод информативного сообщение.
	 * 
	 * @param owner объект, посылающий сообщение
	 * @param message содержание сообщения
	 */
	public static final void info(Object owner, String message) {
		LOGGER.info(owner, message);
	}

	/**
	 * Вывод информативного сообщение.
	 * 
	 * @param name имя объекта, посылающего сообщение
	 * @param message содержание сообщения
	 */
	public static final void info(String name, String message) {
		LOGGER.info(name, message);
	}

	/**
	 * Процесс вывода сообщения в консоль и по возможности в фаил
	 * 
	 * @param text содержание сообщения
	 */
	public static final void println(String text) {

		LoggerListener[] listeners = getListeners();

		if(listeners != null) {
			for(int i = 0, length = listeners.length; i < length; i++) {
				listeners[i].println(text);
			}
		}

		Array<Writer> writers = getWriters();

		if(!writers.isEmpty()) {

			String writeMessage = text + "\n";

			SYNC.lock();
			try {

				for(Writer writer : writers.array()) {
					writer.write(writeMessage);
					writer.flush();
				}

			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				SYNC.unlock();
			}
		}

		System.err.println(text);
	}

	/**
	 * Указание путь к дериктории, в которой будет создаваться папка
	 * логирования.
	 * 
	 * @param path путь к папке, где разместить папку для логирования.
	 * @param использовать ли запись в файл лога.
	 */
	public static final void setDirectory(String path, boolean val) {

		if(!val) {
			return;
		}

		File directory = new File(path, "log");

		if(!directory.exists()) {
			directory.mkdirs();
		}

		try {

			File file = new File(directory.getAbsolutePath(), TIME_FORMAT.format(new Date()) + ".log");

			if(!file.exists()) {
				file.createNewFile();
			}

			addWriter(new FileWriter(file, true));

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Вывод важного эксепшена.
	 * 
	 * @param class класс объекта, посылающего сообщение
	 * @param exception сам эксепшен.
	 */
	public static void warning(Class<?> cs, Exception exception) {
		LOGGER.warning(cs, exception);
	}

	/**
	 * Вывод важного сообщение.
	 * 
	 * @param class класс объекта, посылающего сообщение
	 * @param message содержание сообщения
	 */
	public static final void warning(Class<?> cs, String message) {
		LOGGER.warning(cs, message);
	}

	/**
	 * Вывод важного эксепшена.
	 * 
	 * @param owner объект, посылающий эксепшен.
	 * @param exception сам эксепшен.
	 */
	public static void warning(Object owner, Exception exception) {
		LOGGER.warning(owner, exception);
	}

	/**
	 * Вывод важного сообщение.
	 * 
	 * @param owner объект, посылающий сообщение
	 * @param message содержание сообщения
	 */
	public static final void warning(Object owner, String message) {
		LOGGER.warning(owner, message);
	}

	/**
	 * Вывод важного эксепшена.
	 * 
	 * @param name имя объекта, посылающего сообщение
	 * @param exception сам эксепшен.
	 */
	public static void warning(String name, Exception exception) {
		LOGGER.warning(name, exception);
	}

	/**
	 * Вывод важного сообщение.
	 * 
	 * @param name имя объекта, посылающего сообщение
	 * @param message содержание сообщения
	 */
	public static final void warning(String name, String message) {
		LOGGER.warning(name, message);
	}
}
