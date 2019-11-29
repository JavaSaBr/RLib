package com.ss.rlib.logger.impl;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ConcurrentArray;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerFactory;
import com.ss.rlib.logger.api.LoggerLevel;
import com.ss.rlib.logger.api.LoggerListener;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The class for managing loggers.
 *
 * @author JavaSaBr
 */
public class DefaultLoggerFactory implements LoggerFactory {

    /**
     * The dictionary of all created loggers.
     */
    private final @NotNull ConcurrentMap<String, Logger> loggers;

    /**
     * The main logger.
     */
    private final @NotNull Logger logger;

    /**
     * The list of listeners.
     */
    private final @NotNull ConcurrentArray<LoggerListener> listeners;

    /**
     * The list of writers.
     */
    private final @NotNull ConcurrentArray<Writer> writers;

    /**
     * The date time formatter.
     */
    private final @NotNull DateTimeFormatter timeFormatter;

    public DefaultLoggerFactory() {
        this.loggers = new ConcurrentHashMap<>();
        this.logger = new DefaultLogger("", this);
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
        this.listeners = ConcurrentArray.ofType(LoggerListener.class);
        this.writers = ConcurrentArray.ofType(Writer.class);
    }

    @Override
    public void addListener(@NotNull LoggerListener listener) {
        listeners.runInWriteLock(listener, Array::add);
    }

    @Override
    public void addWriter(@NotNull Writer writer) {
        writers.runInWriteLock(writer, Array::add);
    }

    @Override
    public @NotNull Logger getDefault() {
        return logger;
    }

    @Override
    public @NotNull Logger make(@NotNull Class<?> type) {
        return notNull(loggers.computeIfAbsent(type.getSimpleName(), name -> new DefaultLogger(name, this)));
    }

    @Override
    public @NotNull Logger make(@NotNull String name) {
        return notNull(loggers.computeIfAbsent(name, str -> new DefaultLogger(str, this)));
    }

    @Override
    public void removeListener(@NotNull LoggerListener listener) {
        listeners.runInWriteLock(listener, Array::remove);
    }

    @Override
    public void removeWriter(@NotNull Writer writer) {
        writers.runInWriteLock(writer, Array::remove);
    }

    /**
     * Process of writing message to a console and writers.
     *
     * @param level   the level of the message.
     * @param name    the name of owner.
     * @param message the message.
     */
    void write(@NotNull LoggerLevel level, @NotNull String name, @NotNull String message) {

        var timeStump = timeFormatter.format(LocalTime.now());
        var result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " + message;

        write(level, result);
    }

    /**
     * Process of writing the result message.
     *
     * @param level   the level of the result message.
     * @param resultMessage the result message.
     */
    private void write(@NotNull LoggerLevel level, @NotNull String resultMessage) {

        listeners.forEachInReadLockR(resultMessage, LoggerListener::println);
        writers.forEachInReadLockR(resultMessage, DefaultLoggerFactory::append);

        System.err.println(resultMessage);

        if (!level.isForceFlush()) {
            return;
        }

        listeners.forEachInReadLock(LoggerListener::flush);
        writers.forEachInReadLock(DefaultLoggerFactory::flush);
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
