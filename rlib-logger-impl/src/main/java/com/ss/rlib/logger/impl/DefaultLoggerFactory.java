package com.ss.rlib.logger.impl;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ConcurrentArray;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.Logger.Factory;
import com.ss.rlib.logger.api.LoggerFactory;
import com.ss.rlib.logger.api.LoggerLevel;
import com.ss.rlib.logger.api.LoggerListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private final ConcurrentMap<String, Logger> loggers;

    /**
     * The main logger.
     */
    private final Logger logger;

    /**
     * The list of listeners.
     */
    private final ConcurrentArray<LoggerListener> listeners;

    /**
     * The list of writers.
     */
    private final ConcurrentArray<Writer> writers;

    /**
     * The date time formatter.
     */
    private final DateTimeFormatter timeFormatter;

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
        listeners.runInWriteLock(listener, Array::slowRemove);
    }

    @Override
    public void removeWriter(@NotNull Writer writer) {
        writers.runInWriteLock(writer, Array::slowRemove);
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
     * Process of writing message to a console and writers.
     *
     * @param level          the level of the message.
     * @param name           the name of owner.
     * @param messageFactory the message factory.
     */
    void write(@NotNull LoggerLevel level, @NotNull String name, @NotNull Factory messageFactory) {

        var timeStump = timeFormatter.format(LocalTime.now());
        var result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " + messageFactory.make();

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
    <T> void write(
        @NotNull LoggerLevel level,
        @NotNull String name,
        @Nullable T arg,
        @NotNull Logger.SinFactory<T> messageFactory
    ) {

        var timeStump = timeFormatter.format(LocalTime.now());
        var result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " + messageFactory.make(arg);

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
    <F, S> void write(
        @NotNull LoggerLevel level,
        @NotNull String name,
        @Nullable F first,
        @Nullable S second,
        @NotNull Logger.BiFactory<F, S> messageFactory
    ) {

        var timeStump = timeFormatter.format(LocalTime.now());
        var result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " +
                messageFactory.make(first, second);

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
    <F, S, T> void write(
            @NotNull LoggerLevel level,
            @NotNull String name,
            @Nullable F first,
            @Nullable S second,
            @Nullable T third,
            @NotNull Logger.TriFactory<F, S, T> messageFactory
    ) {

        var timeStump = timeFormatter.format(LocalTime.now());
        var result = level.getTitle() + ' ' + timeStump + ' ' + name + ": " +
                messageFactory.make(first, second, third);

        write(level, result);
    }

    /**
     * Process of writing the result message.
     *
     * @param level   the level of the result message.
     * @param resultMessage the result message.
     */
    private void write(@NotNull LoggerLevel level, @NotNull String resultMessage) {

        listeners.forEachInReadLock(resultMessage, LoggerListener::println);
        writers.forEachInReadLock(resultMessage, DefaultLoggerFactory::append);

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
