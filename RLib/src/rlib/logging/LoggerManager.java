package rlib.logging;

import org.jetbrains.annotations.NotNull;

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
import rlib.util.array.ConcurrentArray;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.ObjectDictionary;

/**
 * The class for managing loggers.
 *
 * @author JavaSaBr
 */
public class LoggerManager {

    /**
     * The dictionary with all created loggers.
     */
    @NotNull
    private static final ObjectDictionary<String, Logger> LOGGERS = DictionaryFactory.newObjectDictionary();

    /**
     * The list of listeners.
     */
    @NotNull
    private static final ConcurrentArray<LoggerListener> LISTENERS = ArrayFactory.newConcurrentAtomicARSWLockArray(LoggerListener.class);

    /**
     * The list of writers.
     */
    @NotNull
    private static final ConcurrentArray<Writer> WRITERS = ArrayFactory.newConcurrentAtomicARSWLockArray(Writer.class);

    /**
     * The synchronizator.
     */
    @NotNull
    private static final Lock SYNC = LockFactory.newAtomicLock();

    /**
     * The date time formatter.
     */
    @NotNull
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    /**
     * The implementation of a logger.
     */
    @NotNull
    private static Class<? extends Logger> implementedClass = LoggerImpl.class;

    /**
     * The main logger.
     */
    @NotNull
    private static final Logger LOGGER;

    static {

        final String className = System.getProperty(LoggerManager.class.getName() + "_impl");

        if (!StringUtils.isEmpty(className)) {
            try {
                implementedClass = ClassUtils.unsafeCast(Class.forName(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                implementedClass = LoggerImpl.class;
            }
        }

        LOGGER = getLogger(LoggerManager.class);
    }

    /**
     * Add a new listener,
     *
     * @param listener the new listener.
     */
    public static void addListener(@NotNull final LoggerListener listener) {
        ArrayUtils.runInWriteLock(LISTENERS, listener, Array::add);
    }

    /**
     * Add a new writer.
     *
     * @param writer the new writer.
     */
    public static void addWriter(@NotNull final Writer writer) {
        ArrayUtils.runInWriteLock(WRITERS, writer, Array::add);
    }

    /**
     * @return the main logger.
     */
    @NotNull
    public static Logger getDefaultLogger() {
        return LOGGER;
    }

    /**
     * @return the list of listeners.
     */
    @NotNull
    private static ConcurrentArray<LoggerListener> getListeners() {
        return LISTENERS;
    }

    /**
     * Get or create a logger for the class.
     *
     * @param cs the class.
     * @return the logger for the class.
     */
    @NotNull
    public static Logger getLogger(@NotNull final Class<?> cs) {

        Logger logger = LOGGERS.get(cs.getName());
        if (logger != null) return logger;

        logger = ClassUtils.newInstance(implementedClass);
        logger.setName(cs.getSimpleName());

        LOGGERS.put(cs.getSimpleName(), logger);
        return logger;
    }

    /**
     * @return the list of writers.
     */
    private static ConcurrentArray<Writer> getWriters() {
        return WRITERS;
    }

    /**
     * Remove a listener.
     *
     * @param listener the listener.
     */
    public static void removeListener(final LoggerListener listener) {
        ArrayUtils.runInWriteLock(LISTENERS, listener, Array::slowRemove);
    }

    /**
     * Remove a writer.
     *
     * @param writer the writer.
     */
    public static void removeWriter(final Writer writer) {
        ArrayUtils.runInWriteLock(WRITERS, writer, Array::slowRemove);
    }

    /**
     * Process to writing message to a console and writers.
     *
     * @param level   the level of the message.
     * @param name    the name of owner.
     * @param message the message.
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
                    (writers, string) -> writers.forEach(string, LoggerManager::append));

            System.err.println(result);

            if (level.isForceFlush()) {
                ArrayUtils.runInReadLock(getListeners(), result,
                        (listeners, string) -> listeners.forEach(string, (listener, s) -> listener.flush()));
            }

        } finally {
            SYNC.unlock();
        }
    }

    private static void append(final Writer writer, final String toWrite) {
        try {
            writer.append(toWrite);
            writer.append('\n');
            writer.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
