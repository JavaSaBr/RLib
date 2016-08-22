package rlib.logging;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.lock.LockFactory;
import rlib.logging.impl.LoggerImpl;
import rlib.util.ArrayUtils;
import rlib.util.ClassUtils;
import rlib.util.StringUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.ObjectDictionary;

/**
 * Менеджер логгирования, служит для получения, конфигурирования и записывания лога.
 *
 * @author JavaSaBr
 */
public class LoggerManager {

    /**
     * таблица всех логгерров
     */
    private static final ObjectDictionary<String, Logger> LOGGERS = DictionaryFactory.newObjectDictionary();

    /**
     * список слушателей логирования
     */
    private static final Array<LoggerListener> LISTENERS = ArrayFactory.newConcurrentPrimitiveAtomicARSWLockArray(LoggerListener.class);

    /**
     * список записчиков лога
     */
    private static final Array<Writer> WRITERS = ArrayFactory.newConcurrentPrimitiveAtomicARSWLockArray(Writer.class);

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
                implementedClass = ClassUtils.unsafeCast(Class.forName(className));
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

        ArrayUtils.runInWriteLock(LISTENERS, listener, Array::add);
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

        ArrayUtils.runInWriteLock(WRITERS, writer, Array::add);
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
    public static Logger getLogger(final Class<?> cs) {

        Logger logger = LOGGERS.get(cs.getName());
        if (logger != null) return logger;

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
    public static void write(final LoggerLevel level, final String name, final String message) {
        if (!level.isEnabled()) return;

        SYNC.lock();
        try {

            final String timeStump = TIME_FORMATTER.format(LocalTime.now());
            final String result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " + message;

            ArrayUtils.runInReadLock(getListeners(), result,
                    (listeners, string) -> listeners.forEach(string, LoggerListener::println));

            ArrayUtils.runInReadLock(getWriters(), result,
                    (writers, string) -> writers.forEach(string, (writer, toWrite) -> {
                        try {
                            writer.append(toWrite);
                            writer.append('\n');
                            writer.flush();
                        } catch (final IOException e) {
                            e.printStackTrace();
                        }
                    }));

            System.err.println(result);

        } finally {
            SYNC.unlock();
        }
    }
}
