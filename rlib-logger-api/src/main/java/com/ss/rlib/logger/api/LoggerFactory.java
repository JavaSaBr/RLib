package com.ss.rlib.logger.api;

import org.jetbrains.annotations.NotNull;

import java.io.Writer;

public interface LoggerFactory {

    /**
     * Make a new logger with the name.
     *
     * @param name the logger's name.
     * @return the new logger.
     */
    @NotNull Logger make(@NotNull String name);

    /**
     * Make a new logger for the type.
     *
     * @param type the logger's type.
     * @return the new logger.
     */
    @NotNull Logger make(@NotNull Class<?> type);

    /**
     * Get a default logger.
     *
     * @return he default logger.
     */
    @NotNull Logger getDefault();

    /**
     * Add the new listener.
     *
     * @param listener the new listener.
     */
    void addListener(@NotNull LoggerListener listener);

    /**
     * Add the new writer.
     *
     * @param writer the new writer.
     */
    void addWriter(@NotNull Writer writer);


    /**
     * Remove the listener.
     *
     * @param listener the listener.
     */
    void removeListener(@NotNull LoggerListener listener);

    /**
     * Remove the writer.
     *
     * @param writer the writer.
     */
    void removeWriter(@NotNull Writer writer);
}
