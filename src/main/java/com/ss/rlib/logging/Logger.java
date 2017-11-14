package com.ss.rlib.logging;

import com.ss.rlib.function.TripleFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The interface to implement a logger.
 *
 * @author JavaSaBr
 */
public interface Logger {

    /**
     * Print the debug message.
     *
     * @param owner   the owner of the message.
     * @param message the message.
     */
    default void debug(@NotNull Object owner, @NotNull String message) {
        print(LoggerLevel.DEBUG, owner, message);
    }

    /**
     * Print the debug message.
     *
     * @param owner          the owner of the message.
     * @param arg            the arg for the message factory.
     * @param messageFactory the message factory.
     */
    default <T> void debug(@NotNull Object owner, @NotNull final T arg,
                           @NotNull final Function<@NotNull T, String> messageFactory) {
        print(LoggerLevel.DEBUG, owner, arg, messageFactory);
    }

    /**
     * Print the debug message.
     *
     * @param owner          the owner of the message.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param messageFactory the message factory.
     */
    default <F, S> void debug(@NotNull Object owner, @NotNull final F first, @NotNull final S second,
                              @NotNull final BiFunction<@NotNull F, @NotNull S, String> messageFactory) {
        print(LoggerLevel.DEBUG, owner, first, second, messageFactory);
    }


    /**
     * Print the debug message.
     *
     * @param owner          the owner of the message.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param third          the third arg for the message factory.
     * @param messageFactory the message factory.
     */
    default <F, S, T> void debug(@NotNull Object owner, @NotNull final F first, @NotNull final S second,
                                 @NotNull final T third,
                                 @NotNull final TripleFunction<@NotNull F, @NotNull S, @NotNull T, String> messageFactory) {
        print(LoggerLevel.DEBUG, owner, first, second, third, messageFactory);
    }

    /**
     * Print the debug message.
     *
     * @param message the message.
     */
    default void debug(@NotNull String message) {
        print(LoggerLevel.DEBUG, message);
    }

    /**
     * Print the debug message.
     *
     * @param arg            the arg for the message factory.
     * @param messageFactory the message factory.
     */
    default <T> void debug(@NotNull final T arg, @NotNull final Function<@NotNull T, String> messageFactory) {
        print(LoggerLevel.DEBUG, arg, messageFactory);
    }

    /**
     * Print the debug message.
     *
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param messageFactory the message factory.
     */
    default <F, S> void debug(@NotNull final F first, @NotNull final S second,
                              @NotNull final BiFunction<@NotNull F, @NotNull S, String> messageFactory) {
        print(LoggerLevel.DEBUG, first, second, messageFactory);
    }


    /**
     * Print the debug message.
     *
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param third          the third arg for the message factory.
     * @param messageFactory the message factory.
     */
    default <F, S, T> void debug(@NotNull final F first, @NotNull final S second, @NotNull final T third,
                                 @NotNull final TripleFunction<@NotNull F, @NotNull S, @NotNull T, String> messageFactory) {
        print(LoggerLevel.DEBUG, first, second, third, messageFactory);
    }

    /**
     * Print the error message.
     *
     * @param owner   the owner of the message.
     * @param message the message.
     */
    default void error(@NotNull Object owner, @NotNull String message) {
        print(LoggerLevel.ERROR, owner, message);
    }

    /**
     * Print the error message.
     *
     * @param owner     the owner of the message.
     * @param exception the exception.
     */
    default void error(@NotNull Object owner, @NotNull Throwable exception) {
        print(LoggerLevel.ERROR, owner, exception);
    }

    /**
     * Print the error message.
     *
     * @param message the message.
     */
    default void error(@NotNull String message) {
        print(LoggerLevel.ERROR, message);
    }

    /**
     * Print the error message.
     *
     * @param exception the exception.
     */
    default void error(@NotNull Throwable exception) {
        print(LoggerLevel.ERROR, exception);
    }

    /**
     * Get the name of this logger.
     *
     * @return the logger name.
     */
    @NotNull String getName();

    /**
     * Set the name of this logger.
     *
     * @param name the logger name.
     */
    void setName(@NotNull String name);

    /**
     * Print the information message.
     *
     * @param owner   the owner of the message.
     * @param message the message.
     */
    default void info(@NotNull Object owner, @NotNull String message) {
        print(LoggerLevel.ERROR, owner, message);
    }

    /**
     * Print the information message.
     *
     * @param owner          the owner of the message.
     * @param arg            the arg for the message factory.
     * @param messageFactory the message factory.
     */
    default <T> void info(@NotNull Object owner, @NotNull final T arg,
                          @NotNull final Function<@NotNull T, String> messageFactory) {
        print(LoggerLevel.INFO, owner, arg, messageFactory);
    }

    /**
     * Print the information message.
     *
     * @param owner          the owner of the message.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param messageFactory the message factory.
     */
    default <F, S> void info(@NotNull Object owner, @NotNull final F first, @NotNull final S second,
                             @NotNull final BiFunction<@NotNull F, @NotNull S, String> messageFactory) {
        print(LoggerLevel.INFO, owner, first, second, messageFactory);
    }


    /**
     * Print the information message.
     *
     * @param owner          the owner of the message.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param third          the third arg for the message factory.
     * @param messageFactory the message factory.
     */
    default <F, S, T> void info(@NotNull Object owner, @NotNull final F first, @NotNull final S second,
                                @NotNull final T third,
                                @NotNull final TripleFunction<@NotNull F, @NotNull S, @NotNull T, String> messageFactory) {
        print(LoggerLevel.INFO, owner, first, second, third, messageFactory);
    }

    /**
     * Print the information message.
     *
     * @param message the message.
     */
    default void info(@NotNull String message) {
        print(LoggerLevel.INFO, message);
    }

    /**
     * Print the information message.
     *
     * @param arg            the arg for the message factory.
     * @param messageFactory the message factory.
     */
    default <T> void info(@NotNull final T arg, @NotNull final Function<@NotNull T, String> messageFactory) {
        print(LoggerLevel.INFO, arg, messageFactory);
    }

    /**
     * Print the information message.
     *
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param messageFactory the message factory.
     */
    default <F, S> void info(@NotNull final F first, @NotNull final S second,
                             @NotNull final BiFunction<@NotNull F, @NotNull S, String> messageFactory) {
        print(LoggerLevel.INFO, first, second, messageFactory);
    }


    /**
     * Print the information message.
     *
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param third          the third arg for the message factory.
     * @param messageFactory the message factory.
     */
    default <F, S, T> void info(@NotNull final F first, @NotNull final S second, @NotNull final T third,
                                @NotNull final TripleFunction<@NotNull F, @NotNull S, @NotNull T, String> messageFactory) {
        print(LoggerLevel.INFO, first, second, third, messageFactory);
    }

    /**
     * @return true if debug is enabled.
     */
    default boolean isEnabledDebug() {
        return LoggerLevel.DEBUG.isEnabled();
    }

    /**
     * @return true if errors is enabled.
     */
    default boolean isEnabledError() {
        return LoggerLevel.ERROR.isEnabled();
    }

    /**
     * @return true if information is enabled.
     */
    default boolean isEnabledInfo() {
        return LoggerLevel.INFO.isEnabled();
    }

    /**
     * @return true if warnings is enabled.
     */
    default boolean isEnabledWarning() {
        return LoggerLevel.WARNING.isEnabled();
    }

    /**
     * Print the warning message.
     *
     * @param owner   the owner of the message.
     * @param message the message.
     */
    default void warning(@NotNull Object owner, @NotNull String message) {
        print(LoggerLevel.WARNING, owner, message);
    }

    /**
     * Print the warning message.
     *
     * @param owner     the owner of the message,
     * @param exception the exception.
     */
    default void warning(@NotNull Object owner, @NotNull Throwable exception) {
        print(LoggerLevel.WARNING, owner, exception);
    }

    /**
     * Print the warning message.
     *
     * @param message the message.
     */
    default void warning(@NotNull String message) {
        print(LoggerLevel.WARNING, message);
    }

    /**
     * Print the warning message.
     *
     * @param exception the exception.
     */
    default void warning(@NotNull Throwable exception) {
        print(LoggerLevel.WARNING, exception);
    }

    /**
     * Print the message.
     *
     * @param level   the level of the message.
     * @param owner   the owner of the message.
     * @param message the message.
     */
    void print(@NotNull LoggerLevel level, @NotNull Object owner, @NotNull String message);

    /**
     * Print the message.
     *
     * @param level   the level of the message.
     * @param message the message.
     */
    void print(@NotNull LoggerLevel level, @NotNull String message);

    /**
     * Print the message.
     *
     * @param level     the level of the message.
     * @param owner     the owner of the message.
     * @param exception the exception.
     */
    void print(@NotNull LoggerLevel level, @NotNull Object owner, @NotNull Throwable exception);

    /**
     * Print the message.
     *
     * @param level     the level of the message.
     * @param exception the exception.
     */
    void print(@NotNull LoggerLevel level, @NotNull Throwable exception);

    /**
     * Print the message.
     *
     * @param level          the level of the message.
     * @param owner          the owner of the message.
     * @param arg            the arg for the message factory.
     * @param messageFactory the message factory.
     */
    <T> void print(@NotNull LoggerLevel level, @NotNull Object owner, @NotNull final T arg,
                   @NotNull final Function<@NotNull T, String> messageFactory);

    /**
     * Print the message.
     *
     * @param level          the level of the message.
     * @param owner          the owner of the message.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param messageFactory the message factory.
     */
    <F, S> void print(@NotNull LoggerLevel level, @NotNull Object owner, @NotNull final F first,
                      @NotNull final S second,
                      @NotNull final BiFunction<@NotNull F, @NotNull S, String> messageFactory);


    /**
     * Print the message.
     *
     * @param level          the level of the message.
     * @param owner          the owner of the message.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param third          the third arg for the message factory.
     * @param messageFactory the message factory.
     */
    <F, S, T> void print(@NotNull LoggerLevel level, @NotNull Object owner, @NotNull final F first,
                         @NotNull final S second, @NotNull final T third,
                         @NotNull final TripleFunction<@NotNull F, @NotNull S, @NotNull T, String> messageFactory);

    /**
     * Print the message.
     *
     * @param level          the level of the message.
     * @param arg            the arg for the message factory.
     * @param messageFactory the message factory.
     */
    <T> void print(@NotNull LoggerLevel level, @NotNull final T arg,
                   @NotNull final Function<@NotNull T, String> messageFactory);

    /**
     * Print the message.
     *
     * @param level          the level of the message.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param messageFactory the message factory.
     */
    <F, S> void print(@NotNull LoggerLevel level, @NotNull final F first, @NotNull final S second,
                      @NotNull final BiFunction<@NotNull F, @NotNull S, String> messageFactory);


    /**
     * Print the message.
     *
     * @param level          the level of the message.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param third          the third arg for the message factory.
     * @param messageFactory the message factory.
     */
    <F, S, T> void print(@NotNull LoggerLevel level, @NotNull final F first, @NotNull final S second,
                         @NotNull final T third,
                         @NotNull final TripleFunction<@NotNull F, @NotNull S, @NotNull T, String> messageFactory);
}
