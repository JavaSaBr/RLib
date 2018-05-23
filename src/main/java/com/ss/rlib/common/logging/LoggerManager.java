package com.ss.rlib.common.logging;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.rlib.common.function.TripleFunction;
import com.ss.rlib.common.logging.impl.LoggerImpl;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The class for managing loggers.
 *
 * @author JavaSaBr
 */
public class LoggerManager {

    /**
     * The dictionary of all created loggers.
     */
    @NotNull
    private static final ConcurrentMap<String, Logger> LOGGERS = new ConcurrentHashMap<>();

    /**
     * The current implementation of loggers.
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
                implementedClass = (Class<? extends Logger>) Class.forName(className);
            } catch (final ClassNotFoundException e) {
                e.printStackTrace();
                implementedClass = LoggerImpl.class;
            }
        } else {
            final Iterator<Logger> impls = ServiceLoader.load(Logger.class).iterator();
            if (impls.hasNext()) {
                implementedClass = impls.next().getClass();
            }
        }

        LOGGER = getLogger(LoggerManager.class);
    }

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
     * The date time formatter.
     */
    @NotNull
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    /**
     * Add the new listener.
     *
     * @param listener the new listener.
     */
    public static void addListener(@NotNull final LoggerListener listener) {
        ArrayUtils.runInWriteLock(LISTENERS, listener, Array::add);
    }

    /**
     * Add the new writer.
     *
     * @param writer the new writer.
     */
    public static void addWriter(@NotNull final Writer writer) {
        ArrayUtils.runInWriteLock(WRITERS, writer, Array::add);
    }

    /**
     * Get the main logger.
     *
     * @return the main logger.
     */
    public static @NotNull Logger getDefaultLogger() {
        return LOGGER;
    }

    /**
     * Get the list of listeners.
     *
     * @return the list of listeners.
     */
    private static @NotNull ConcurrentArray<LoggerListener> getListeners() {
        return LISTENERS;
    }

    /**
     * Get or create a logger for the class.
     *
     * @param cs the class.
     * @return the logger for the class.
     */
    public static @NotNull Logger getLogger(@NotNull final Class<?> cs) {
        return notNull(LOGGERS.computeIfAbsent(cs.getSimpleName(), LoggerManager::createLogger));
    }

    /**
     * Get or create a logger for the name.
     *
     * @param name the name.
     * @return the logger for the class.
     */
    public static @NotNull Logger getLogger(@NotNull final String name) {
        return notNull(LOGGERS.computeIfAbsent(name, LoggerManager::createLogger));
    }

    /**
     * Get the list of writers.
     *
     * @return the list of writers.
     */
    private static @NotNull ConcurrentArray<Writer> getWriters() {
        return WRITERS;
    }

    /**
     * Remove the listener.
     *
     * @param listener the listener.
     */
    public static void removeListener(@NotNull final LoggerListener listener) {
        ArrayUtils.runInWriteLock(LISTENERS, listener, Array::slowRemove);
    }

    /**
     * Remove the writer.
     *
     * @param writer the writer.
     */
    public static void removeWriter(@NotNull final Writer writer) {
        ArrayUtils.runInWriteLock(WRITERS, writer, Array::slowRemove);
    }

    /**
     * Process of writing message to a console and writers.
     *
     * @param level   the level of the message.
     * @param name    the name of owner.
     * @param message the message.
     */
    public static void write(@NotNull final LoggerLevel level, @NotNull final String name, @NotNull final String message) {

        final String timeStump = TIME_FORMATTER.format(LocalTime.now());
        final String result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " + message;

        write(level, result);
    }

    /**
     * Process of writing message to a console and writers.
     *
     * @param level          the level of the message.
     * @param name           the name of owner.
     * @param messageFactory the message factory.
     */
    public static void write(@NotNull final LoggerLevel level, @NotNull final String name,
                             @NotNull final Supplier<String> messageFactory) {

        final String timeStump = TIME_FORMATTER.format(LocalTime.now());
        final String result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " + messageFactory.get();

        write(level, result);
    }

    /**
     * Process of writing message to a console and writers.
     *
     * @param level          the level of the message.
     * @param name           the name of owner.
     * @param arg            the arg for the message factory.
     * @param messageFactory the message factory.
     * @param <T>            the argument's type.
     */
    public static <T> void write(
            @NotNull LoggerLevel level,
            @NotNull String name,
            @NotNull T arg,
            @NotNull Function<T, String> messageFactory
    ) {

        String timeStump = TIME_FORMATTER.format(LocalTime.now());
        String result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " + messageFactory.apply(arg);

        write(level, result);
    }

    /**
     * Process of writing message to a console and writers.
     *
     * @param level          the level of the message.
     * @param name           the name of owner.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param messageFactory the message factory.
     * @param <F>            the first argument's type.
     * @param <S>            the second argument's type.
     */
    public static <F, S> void write(
            @NotNull LoggerLevel level,
            @NotNull String name,
            @NotNull F first,
            @NotNull S second,
            @NotNull BiFunction<F, S, String> messageFactory
    ) {

        String timeStump = TIME_FORMATTER.format(LocalTime.now());
        String result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " +
                messageFactory.apply(first, second);

        write(level, result);
    }

    /**
     * Process of writing message to a console and writers.
     *
     * @param level          the level of the message.
     * @param name           the name of owner.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param third          the third arg for the message factory.
     * @param messageFactory the message factory.
     * @param <F>            the first argument's type.
     * @param <S>            the second argument's type.
     * @param <T>            the third argument's type.
     */
    public static <F, S, T> void write(
            @NotNull LoggerLevel level,
            @NotNull String name,
            @NotNull F first,
            @NotNull S second,
            @NotNull T third,
            @NotNull TripleFunction<F, S, T, String> messageFactory
    ) {

        String timeStump = TIME_FORMATTER.format(LocalTime.now());
        String result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " +
                messageFactory.apply(first, second, third);

        write(level, result);
    }

    /**
     * Process of writing the result message.
     *
     * @param level   the level of the result message.
     * @param resultMessage the result message.
     */
    private static void write(@NotNull final LoggerLevel level, @NotNull final String resultMessage) {

        final ConcurrentArray<LoggerListener> listeners = getListeners();
        final ConcurrentArray<Writer> writers = getWriters();

        ArrayUtils.runInReadLock(listeners, resultMessage,
                (array, string) -> array.forEach(string, LoggerListener::println));

        ArrayUtils.runInReadLock(writers, resultMessage,
                (array, string) -> array.forEach(string, LoggerManager::append));

        System.err.println(resultMessage);

        if (!level.isForceFlush()) {
            return;
        }

        ArrayUtils.runInReadLock(listeners,
                array -> array.forEach(LoggerListener::flush));

        ArrayUtils.runInReadLock(writers,
                array -> array.forEach(LoggerManager::flush));
    }

    private static @NotNull Logger createLogger(@NotNull final String name) {

        final Logger newLogger;
        try {
            newLogger = implementedClass.newInstance();
        } catch (final InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        newLogger.setName(name);

        return newLogger;
    }

    private static void append(@NotNull final Writer writer, @NotNull final String toWrite) {
        try {
            writer.append(toWrite);
            writer.append('\n');
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static void flush(@NotNull final Writer writer) {
        try {
            writer.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
