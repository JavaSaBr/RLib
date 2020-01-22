package com.ss.rlib.logger.api;

import com.ss.rlib.logger.api.impl.NullLoggerFactory;
import org.jetbrains.annotations.NotNull;

import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ServiceLoader;

/**
 * The class to manage loggers.
 *
 * @author JavaSaBr
 */
public class LoggerManager {

    private static final LoggerFactory LOGGER_FACTORY;

    static {

        String className = System.getProperty("com.ss.rlib.logger.factory", "");

        Class<? extends LoggerFactory> implementation = null;

        if (!className.isEmpty()) {
            try {
                implementation = (Class<? extends LoggerFactory>) Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (implementation == null) {

            var impls = ServiceLoader.load(LoggerFactory.class)
                .iterator();

            if (impls.hasNext()) {
                implementation = impls.next().getClass();
            }
        }

        if (implementation == null) {
            System.err.println("ERROR: No any exist implementation of Rlib Logger Factory, will use null logger");
            LOGGER_FACTORY = new NullLoggerFactory();
        } else {
            try {
                LOGGER_FACTORY = implementation.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Add the new listener.
     *
     * @param listener the new listener.
     */
    public static void addListener(@NotNull LoggerListener listener) {
        LOGGER_FACTORY.addListener(listener);
    }

    /**
     * Add the new writer.
     *
     * @param writer the new writer.
     */
    public static void addWriter(@NotNull Writer writer) {
        LOGGER_FACTORY.addWriter(writer);
    }

    /**
     * Get the main logger.
     *
     * @return the main logger.
     */
    public static @NotNull Logger getDefaultLogger() {
        return LOGGER_FACTORY.getDefault();
    }

    /**
     * Get or create a logger for the class.
     *
     * @param cs the class.
     * @return the logger for the class.
     */
    public static @NotNull Logger getLogger(@NotNull Class<?> cs) {
        return LOGGER_FACTORY.make(cs);
    }

    /**
     * Get or create a logger for the id.
     *
     * @param id the id.
     * @return the logger for the class.
     */
    public static @NotNull Logger getLogger(@NotNull String id) {
        return LOGGER_FACTORY.make(id);
    }

    /**
     * Remove the listener.
     *
     * @param listener the listener.
     */
    public static void removeListener(@NotNull LoggerListener listener) {
        LOGGER_FACTORY.removeListener(listener);
    }

    /**
     * Remove the writer.
     *
     * @param writer the writer.
     */
    public static void removeWriter(@NotNull Writer writer) {
        LOGGER_FACTORY.removeWriter(writer);
    }

    /**
     * Enable passed logger level for some logger.
     *
     * @param cs    the class which use its own logger.
     * @param level the logger level to enable.
     */
    public static void enable(@NotNull Class<?> cs, @NotNull LoggerLevel level) {
        getLogger(cs).setEnabled(level, true);
    }

    /**
     * Disable passed logger level for some logger.
     *
     * @param cs    the class which use its own logger.
     * @param level the logger level to disable.
     */
    public static void disable(@NotNull Class<?> cs, @NotNull LoggerLevel level) {
        getLogger(cs).setEnabled(level, false);
    }
}
