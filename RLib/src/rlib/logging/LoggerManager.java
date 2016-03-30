package rlib.logging;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.lock.LockFactory;
import rlib.logging.impl.LoggerImpl;
import rlib.util.ClassUtils;
import rlib.util.StringUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.ObjectDictionary;

/**
 * Менеджер логгирования, служит для получения, конфигурирования и записывания лога.
 *
 * @author Ronn
 */
public class LoggerManager {

    /**
     * таблица всех логгерров
     */
    private static final ObjectDictionary<String, Logger> LOGGERS = DictionaryFactory.newObjectDictionary();

    /**
     * список слушателей логирования
     */
    private static final Array<LoggerListener> LISTENERS = ArrayFactory.newArray(LoggerListener.class);

    /**
     * список записчиков лога
     */
    private static final Array<Writer> WRITERS = ArrayFactory.newArray(Writer.class);

    /**
     * синхронизатор записи лога
     */
    private static final Lock SYNC = LockFactory.newPrimitiveAtomicLock();

    /**
     * формат записи времени
     */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    /**
     * класс реализации логгера
     */
    private static Class<? extends Logger> implementedClass = LoggerImpl.class;

    /**
     * главный логгер
     */
    private static final Logger LOGGER;

    static {

        final String className = System.getProperty(LoggerManager.class.getName() + "_impl");

        if (!StringUtils.isEmpty(className)) {
            try {
                implementedClass = (Class<? extends Logger>) Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (implementedClass == null) {
            implementedClass = LoggerImpl.class;
        }

        LOGGER = getLogger(LoggerManager.class);
    }

    /**
     * Добавление слушателя логирования.
     *
     * @param listener добавляемый слушатель.
     */
    public static void addListener(final LoggerListener listener) {

        if (listener == null) {
            throw new IllegalArgumentException("listener is null.");
        }

        LISTENERS.add(listener);
    }

    /**
     * Добавление записчика лога.
     *
     * @param writer записчик лога.
     */
    public static void addWriter(final Writer writer) {

        if (writer == null) {
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
    public static final Logger getLogger(final Class<?> cs) {

        Logger logger = LOGGERS.get(cs.getName());

        if (logger != null) {
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
    public static void removeListener(final LoggerListener listener) {
        LISTENERS.slowRemove(listener);
    }

    /**
     * Удаление записчика лога.
     *
     * @param writer записчик лога.
     */
    public static void removeWriter(final Writer writer) {
        WRITERS.slowRemove(writer);
    }

    /**
     * Процесс вывода сообщения в консоль и по возможности в фаил
     *
     * @param message содержание сообщения
     */
    public static final void write(final LoggerLevel level, final String name, final String message) {

        if (!level.isEnabled()) {
            return;
        }

        SYNC.lock();
        try {

            final StringBuilder builder = new StringBuilder(level.getTitle());
            builder.append(' ').append(TIME_FORMATTER.format(LocalTime.now()));
            builder.append(' ').append(name).append(": ").append(message);

            final String result = builder.toString();

            final Array<LoggerListener> listeners = getListeners();

            if (!listeners.isEmpty()) {

                for (final LoggerListener listener : listeners.array()) {

                    if (listener == null) {
                        break;
                    }

                    listener.println(result);
                }
            }

            final Array<Writer> writers = getWriters();

            if (!writers.isEmpty()) {
                for (final Writer writer : writers.array()) {

                    if (writer == null) {
                        continue;
                    }

                    try {
                        writer.append(result);
                        writer.append('\n');
                        writer.flush();
                    } catch (final IOException e) {
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
