package rlib.logging;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.lock.LockFactory;
import rlib.logging.impl.LoggerImpl;
import rlib.util.ClassUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.table.Table;
import rlib.util.table.TableFactory;

/**
 * Менеджер логгирования, служит для получения, конфигурирования и записывания
 * лога.
 * 
 * @author Ronn
 */
public class LoggerManager {

	/** таблица всех логгерров */
	private static final Table<String, Logger> LOGGERS = TableFactory.newObjectTable();
	/** список слушателей логирования */
	private static final Array<LoggerListener> LISTENERS = ArrayFactory.newArray(LoggerListener.class);
	/** список записчиков лога */
	private static final Array<Writer> WRITERS = ArrayFactory.newArray(Writer.class);
	/** синхронизатор записи лога */
	private static final Lock SYNC = LockFactory.newPrimitiveAtomicLoc();

	/** формат записи времени */
	private static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
	/** класс реализации логгера */
	private static Class<? extends Logger> implementedClass = LoggerImpl.class;

	/** главный логгер */
	private static final Logger LOGGER = getLogger(LoggerManager.class);

	/**
	 * Добавление слушателя логирования.
	 * 
	 * @param listener добавляемый слушатель.
	 */
	public static void addListener(LoggerListener listener) {

		if(listener == null) {
			throw new IllegalArgumentException("ilistener is null.");
		}

		LISTENERS.add(listener);
	}

	/**
	 * Добавление записчика лога.
	 * 
	 * @param writer записчик лога.
	 */
	public static void addWriter(Writer writer) {

		if(writer == null) {
			throw new IllegalArgumentException("writer is null.");
		}

		WRITERS.add(writer);
	}

	/**
	 * @return стандартный логгер.
	 */
	public static Logger getDefaultLogger() {
		return LOGGER;
	}

	/**
	 * @return список слушателей логирования.
	 */
	private static Array<LoggerListener> getListeners() {
		return LISTENERS;
	}

	/**
	 * Получение логера для указанного класса.
	 * 
	 * @param cs класс, который запрашивает логгер
	 * @return индивидуальный логер для указанного класса.
	 */
	public static final Logger getLogger(Class<?> cs) {

		Logger logger = LOGGERS.get(cs.getName());

		if(logger != null) {
			return logger;
		}

		logger = ClassUtils.newInstance(implementedClass);
		logger.setName(cs.getSimpleName());

		LOGGERS.put(cs.getSimpleName(), logger);
		return logger;
	}

	/**
	 * @return список дополнительных записчиков лога.
	 */
	private static Array<Writer> getWriters() {
		return WRITERS;
	}

	/**
	 * Удаления слушателя логирования.
	 * 
	 * @param listener удаляемый слушатель.
	 */
	public static void removeListener(LoggerListener listener) {
		LISTENERS.slowRemove(listener);
	}

	/**
	 * Удаление записчика лога.
	 * 
	 * @param writer записчик лога.
	 */
	public static void removeWriter(Writer writer) {
		WRITERS.slowRemove(writer);
	}

	/**
	 * Указание новой реализации логера.
	 * 
	 * @param implementedClass класс реализации логера.
	 */
	public static void setImplementedClass(Class<? extends Logger> implementedClass) {

		if(implementedClass == null) {
			throw new IllegalArgumentException("implemented class is null.");
		}

		LoggerManager.implementedClass = implementedClass;
	}

	/**
	 * Процесс вывода сообщения в консоль и по возможности в фаил
	 * 
	 * @param message содержание сообщения
	 */
	public static final void write(LoggerLevel level, String name, String message) {

		if(!level.isEnabled()) {
			return;
		}

		SYNC.lock();
		try {

			StringBuilder builder = new StringBuilder(level.getTitle());
			builder.append(' ').append(timeFormat.format(LocalTime.now()));
			builder.append(' ').append(name).append(": ").append(message);

			String result = builder.toString();

			Array<LoggerListener> listeners = getListeners();

			if(!listeners.isEmpty()) {

				for(LoggerListener listener : listeners.array()) {

					if(listener == null) {
						break;
					}

					listener.println(result);
				}
			}

			Array<Writer> writers = getWriters();

			if(!writers.isEmpty()) {
				for(Writer writer : writers.array()) {

					if(writer == null) {
						continue;
					}

					try {
						writer.append(result);
						writer.append('\n');
						writer.flush();
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}

			System.err.println(result);

		} finally {
			SYNC.unlock();
		}
	}
}
