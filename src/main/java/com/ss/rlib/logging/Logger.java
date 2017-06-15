package com.ss.rlib.logging;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a logger.
 *
 * @author JavaSaBr
 */
public interface Logger {

    /**
     * Print a debug message.
     *
     * @param cs      the class of a sender.
     * @param message the message.
     */
    void debug(@NotNull Class<?> cs, @NotNull String message);

    /**
     * Print a debug message.
     *
     * @param owner   the owner of the message.
     * @param message the message.
     */
    void debug(@NotNull Object owner, @NotNull String message);

    /**
     * Print a debug message.
     *
     * @param message the message.
     */
    void debug(@NotNull String message);

    /**
     * Print a debug message.
     *
     * @param name    the name of a sender.
     * @param message the message.
     */
    void debug(@NotNull String name, @NotNull String message);

    /**
     * Print an error message.
     *
     * @param cs      the class of a sender.
     * @param message the message.
     */
    void error(@NotNull Class<?> cs, @NotNull String message);

    /**
     * Print an error message.
     *
     * @param cs        the class of a sender.
     * @param exception the exception.
     */
    void error(@NotNull Class<?> cs, @NotNull Throwable exception);

    /**
     * Print an error message.
     *
     * @param owner   the owner of the message.
     * @param message the message.
     */
    void error(@NotNull Object owner, @NotNull String message);

    /**
     * Print an error message.
     *
     * @param owner     the owner of the message.
     * @param exception the exception.
     */
    void error(@NotNull Object owner, @NotNull Throwable exception);

    /**
     * Print an error message.
     *
     * @param message the message.
     */
    void error(@NotNull String message);

    /**
     * Print an error message.
     *
     * @param name    the name of a sender.
     * @param message the message.
     */
    void error(@NotNull String name, @NotNull String message);

    /**
     * Print an error message.
     *
     * @param name      the name of a sender.
     * @param exception the exception.
     */
    void error(@NotNull String name, @NotNull Throwable exception);

    /**
     * Print an error message.
     *
     * @param exception the exception.
     */
    void error(@NotNull Throwable exception);

    /**
     * Gets name.
     *
     * @return the logger name.
     */
    @NotNull
    String getName();

    /**
     * Sets name.
     *
     * @param name the logger name.
     */
    void setName(@NotNull String name);

    /**
     * Print an information message.
     *
     * @param cs      the class of a sender.
     * @param message the message.
     */
    void info(@NotNull Class<?> cs, @NotNull String message);

    /**
     * Print an information message.
     *
     * @param owner   the owner of the message.
     * @param message the message.
     */
    void info(@NotNull Object owner, @NotNull String message);

    /**
     * Print an information message.
     *
     * @param message the message.
     */
    void info(@NotNull String message);

    /**
     * Print an information message.
     *
     * @param name    the name of a sender.
     * @param message the message.
     */
    void info(@NotNull String name, @NotNull String message);

    /**
     * Is enabled debug boolean.
     *
     * @return true if debug is enabled.
     */
    boolean isEnabledDebug();

    /**
     * Is enabled error boolean.
     *
     * @return true if errors is enabled.
     */
    boolean isEnabledError();

    /**
     * Is enabled info boolean.
     *
     * @return true if information is enabled.
     */
    boolean isEnabledInfo();

    /**
     * Is enabled warning boolean.
     *
     * @return true if warnings is enabled.
     */
    boolean isEnabledWarning();

    /**
     * Print a warning message.
     *
     * @param cs      the class of a sender.
     * @param message the message.
     */
    void warning(@NotNull Class<?> cs, @NotNull String message);

    /**
     * Print a warning message.
     *
     * @param cs        the class of a sender.
     * @param exception the exception.
     */
    void warning(@NotNull Class<?> cs, @NotNull Throwable exception);

    /**
     * Print a warning message.
     *
     * @param owner   the owner of the message.
     * @param message the message.
     */
    void warning(@NotNull Object owner, @NotNull String message);

    /**
     * Print a warning message.
     *
     * @param owner     the owner of the message,
     * @param exception the exception.
     */
    void warning(@NotNull Object owner, @NotNull Throwable exception);

    /**
     * Print a warning message.
     *
     * @param message the message.
     */
    void warning(@NotNull String message);

    /**
     * Print a warning message.
     *
     * @param name    the name of a sender.
     * @param message the message.
     */
    void warning(@NotNull String name, @NotNull String message);

    /**
     * Print a warning message.
     *
     * @param name      the name of a sender.
     * @param exception the exception.
     */
    void warning(@NotNull String name, @NotNull Throwable exception);

    /**
     * Print a warning message.
     *
     * @param exception the exception.
     */
    void warning(@NotNull Throwable exception);
}
