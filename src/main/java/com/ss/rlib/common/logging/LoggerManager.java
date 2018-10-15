package com.ss.rlib.common.logging;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.rlib.common.function.TripleFunction;
import com.ss.rlib.common.logging.impl.LoggerImpl;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ConcurrentArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    private static final ConcurrentMap<String, Logger> LOGGERS = new ConcurrentHashMap<>();

    /**
     * The current implementation of loggers.
     */
    private static Class<? extends Logger> implementedClass = LoggerImpl.class;

    /**
     * The main logger.
     */
    private static final Logger LOGGER;

    static {

        String className = System.getProperty(LoggerManager.class.getName() + "_impl");

        if (!StringUtils.isEmpty(className)) {

            try {
                implementedClass = (Class<? extends Logger>) Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                implementedClass = LoggerImpl.class;
            }

        } else {

            var impls = ServiceLoader.load(Logger.class)
                    .iterator();

            if (impls.hasNext()) {
                implementedClass = impls.next().getClass();
            }
        }

        LOGGER = getLogger(LoggerManager.class);
    }

    /**
     * The list of listeners.
     */
    private static final ConcurrentArray<LoggerListener> LISTENERS =
            ConcurrentArray.ofType(LoggerListener.class);

    /**
     * The list of writers.
     */
    private static final ConcurrentArray<Writer> WRITERS =
            ConcurrentArray.ofType(Writer.class);

    /**
     * The date time formatter.
     */
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    /**
     * Add the new listener.
     *
     * @param listener the new listener.
     */
    public static void addListener(@NotNull LoggerListener listener) {
        LISTENERS.runInWriteLock(listener, Array::add);
    }

    /**
     * Add the new writer.
     *
     * @param writer the new writer.
     */
    public static void addWriter(@NotNull Writer writer) {
        WRITERS.runInWriteLock(writer, Array::add);
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
     * Get or create a logger for the class.
     *
     * @param cs the class.
     * @return the logger for the class.
     */
    public static @NotNull Logger getLogger(@NotNull Class<?> cs) {
        return notNull(LOGGERS.computeIfAbsent(cs.getSimpleName(), LoggerManager::createLogger));
    }

    /**
     * Get or create a logger for the name.
     *
     * @param name the name.
     * @return the logger for the class.
     */
    public static @NotNull Logger getLogger(@NotNull String name) {
        return notNull(LOGGERS.computeIfAbsent(name, LoggerManager::createLogger));
    }

    /**
     * Remove the listener.
     *
     * @param listener the listener.
     */
    public static void removeListener(@NotNull LoggerListener listener) {
        LISTENERS.runInWriteLock(listener, Array::slowRemove);
    }

    /**
     * Remove the writer.
     *
     * @param writer the writer.
     */
    public static void removeWriter(@NotNull Writer writer) {
        WRITERS.runInWriteLock(writer, Array::slowRemove);
    }

    /**
     * Process of writing message to a console and writers.
     *
     * @param level   the level of the message.
     * @param name    the name of owner.
     * @param message the message.
     */
    public static void write(@NotNull LoggerLevel level, @NotNull String name, @NotNull String message) {

        var timeStump = TIME_FORMATTER.format(LocalTime.now());
        var result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " + message;

        write(level, result);
    }

    /**
     * Process of writing message to a console and writers.
     *
     * @param level          the level of the message.
     * @param name           the name of owner.
     * @param messageFactory the message factory.
     */
    public static void write(
            @NotNull LoggerLevel level,
            @NotNull String name,
            @NotNull Supplier<@NotNull String> messageFactory
    ) {

        var timeStump = TIME_FORMATTER.format(LocalTime.now());
        var result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " + messageFactory.get();

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
            @Nullable T arg,
            @NotNull Function<T, @NotNull String> messageFactory
    ) {

        var timeStump = TIME_FORMATTER.format(LocalTime.now());
        var result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " + messageFactory.apply(arg);

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
            @Nullable F first,
            @Nullable S second,
            @NotNull BiFunction<F, S, @NotNull String> messageFactory
    ) {

        var timeStump = TIME_FORMATTER.format(LocalTime.now());
        var result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " +
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
            @Nullable F first,
            @Nullable S second,
            @Nullable T third,
            @NotNull TripleFunction<F, S, T, String> messageFactory
    ) {

        var timeStump = TIME_FORMATTER.format(LocalTime.now());
        var result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " +
                messageFactory.apply(first, second, third);

        write(level, result);
    }

    /**
     * Process of writing the result message.
     *
     * @param level   the level of the result message.
     * @param resultMessage the result message.
     */
    private static void write(@NotNull LoggerLevel level, @NotNull String resultMessage) {

        LISTENERS.forEachInReadLock(resultMessage, LoggerListener::println);
        WRITERS.forEachInReadLock(resultMessage, LoggerManager::append);

        System.err.println(resultMessage);

        if (!level.isForceFlush()) {
            return;
        }

        LISTENERS.forEachInReadLock(LoggerListener::flush);
        WRITERS.forEachInReadLock(LoggerManager::flush);
    }

    private static @NotNull Logger createLogger(@NotNull String name) {

        Logger newLogger;
        try {
            newLogger = implementedClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        newLogger.setName(name);

        return newLogger;
    }

    private static void append(@NotNull Writer writer, @NotNull String toWrite) {
        try {
            writer.append(toWrite);
            writer.append('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void flush(@NotNull Writer writer) {
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
