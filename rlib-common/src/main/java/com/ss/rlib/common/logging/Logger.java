package com.ss.rlib.common.logging;

import com.ss.rlib.common.function.TripleFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * @param <T>            the argument's type.
     */
    default <T> void debug(
            @NotNull Object owner,
            @Nullable T arg,
            @NotNull Function<T, @NotNull String> messageFactory
    ) {
        print(LoggerLevel.DEBUG, owner, arg, messageFactory);
    }

    /**
     * Print the debug message.
     *
     * @param owner          the owner of the message.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param messageFactory the message factory.
     * @param <F>            the first argument's type.
     * @param <S>            the second argument's type.
     */
    default <F, S> void debug(
            @NotNull Object owner,
            @Nullable F first,
            @Nullable S second,
            @NotNull BiFunction<F, S, @NotNull String> messageFactory
    ) {
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
     * @param <F>            the first argument's type.
     * @param <S>            the second argument's type.
     * @param <T>            the third argument's type.
     */
    default <F, S, T> void debug(
            @NotNull Object owner,
            @Nullable F first,
            @Nullable S second,
            @Nullable T third,
            @NotNull TripleFunction<F, S, T, @NotNull String> messageFactory
    ) {
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
     * @param <T>            the argument's type.
     */
    default <T> void debug(@Nullable T arg, @NotNull Function<T, @NotNull String> messageFactory) {
        print(LoggerLevel.DEBUG, arg, messageFactory);
    }

    /**
     * Print the debug message.
     *
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param messageFactory the message factory.
     * @param <F>            the first argument's type.
     * @param <S>            the second argument's type.
     */
    default <F, S> void debug(
            @Nullable F first,
            @Nullable S second,
            @NotNull BiFunction<F, S, @NotNull String> messageFactory
    ) {
        print(LoggerLevel.DEBUG, first, second, messageFactory);
    }

    /**
     * Print the debug message.
     *
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param third          the third arg for the message factory.
     * @param messageFactory the message factory.
     * @param <F>            the first argument's type.
     * @param <S>            the second argument's type.
     * @param <T>            the third argument's type.
     */
    default <F, S, T> void debug(
            @NotNull F first,
            @Nullable S second,
            @Nullable T third,
            @NotNull TripleFunction<F, S, T, @NotNull String> messageFactory
    ) {
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
        print(LoggerLevel.INFO, owner, message);
    }

    /**
     * Print the information message.
     *
     * @param owner          the owner of the message.
     * @param arg            the arg for the message factory.
     * @param messageFactory the message factory.
     * @param <T>            the argument's type.
     */
    default <T> void info(
            @NotNull Object owner,
            @Nullable T arg,
            @NotNull Function<T, @NotNull String> messageFactory
    ) {
        print(LoggerLevel.INFO, owner, arg, messageFactory);
    }

    /**
     * Print the information message.
     *
     * @param owner          the owner of the message.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param messageFactory the message factory.
     * @param <F>            the first argument's type.
     * @param <S>            the second argument's type.
     */
    default <F, S> void info(
            @NotNull Object owner,
            @Nullable F first,
            @Nullable S second,
            @NotNull BiFunction<F, S, @NotNull String> messageFactory
    ) {
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
     * @param <F>            the first argument's type.
     * @param <S>            the second argument's type.
     * @param <T>            the third argument's type.
     */
    default <F, S, T> void info(
            @NotNull Object owner,
            @Nullable F first,
            @Nullable S second,
            @Nullable T third,
            @NotNull TripleFunction<F, S, T, @NotNull String> messageFactory
    ) {
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
     * @param <T>            the argument's type.
     */
    default <T> void info(@NotNull T arg, @NotNull Function<T, @NotNull String> messageFactory) {
        print(LoggerLevel.INFO, arg, messageFactory);
    }

    /**
     * Print the information message.
     *
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param messageFactory the message factory.
     * @param <F>            the first argument's type.
     * @param <S>            the second argument's type.
     */
    default <F, S> void info(
            @Nullable F first,
            @Nullable S second,
            @NotNull BiFunction<F, S, @NotNull String> messageFactory
    ) {
        print(LoggerLevel.INFO, first, second, messageFactory);
    }


    /**
     * Print the information message.
     *
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param third          the third arg for the message factory.
     * @param messageFactory the message factory.
     * @param <F>            the first argument's type.
     * @param <S>            the second argument's type.
     * @param <T>            the third argument's type.
     */
    default <F, S, T> void info(
            @Nullable F first,
            @Nullable S second,
            @Nullable T third,
            @NotNull TripleFunction<F, S, T, @NotNull String> messageFactory
    ) {
        print(LoggerLevel.INFO, first, second, third, messageFactory);
    }

    /**
     * Check of enabling the logger level.
     *
     * @param level the logger level.
     * @return true if the level is enabled.
     */
    default boolean isEnabled(@NotNull LoggerLevel level) {
        return level.isEnabled();
    }

    /**
     * Override the enabling status of the logger level.
     *
     * @param level   the logger level.
     * @param enabled true if need to be enabled.
     * @return true if the status was changed.
     */
    default boolean setEnabled(@NotNull LoggerLevel level, boolean enabled) {
        return false;
    }

    /**
     * Remove overriding of enabling status if the logger level.
     *
     * @param level the logger level.
     * @return true if the status was changed.
     */
    default boolean applyDefault(@NotNull LoggerLevel level) {
        return false;
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
     * @param <T>            the argument's type.
     */
    <T> void print(
            @NotNull LoggerLevel level,
            @NotNull Object owner,
            @Nullable T arg,
            @NotNull Function<T, @NotNull String> messageFactory
    );

    /**
     * Print the message.
     *
     * @param level          the level of the message.
     * @param owner          the owner of the message.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param messageFactory the message factory.
     * @param <F>            the first argument's type.
     * @param <S>            the second argument's type.
     */
    <F, S> void print(
            @NotNull LoggerLevel level,
            @NotNull Object owner,
            @Nullable F first,
            @Nullable S second,
            @NotNull BiFunction<F, S, @NotNull String> messageFactory
    );

    /**
     * Print the message.
     *
     * @param level          the level of the message.
     * @param owner          the owner of the message.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param third          the third arg for the message factory.
     * @param messageFactory the message factory.
     * @param <F>            the first argument's type.
     * @param <S>            the second argument's type.
     * @param <T>            the third argument's type.
     */
    <F, S, T> void print(
            @NotNull LoggerLevel level,
            @NotNull Object owner,
            @Nullable F first,
            @Nullable S second,
            @Nullable T third,
            @NotNull TripleFunction<F, S, T, @NotNull String> messageFactory
    );

    /**
     * Print the message.
     *
     * @param level          the level of the message.
     * @param arg            the arg for the message factory.
     * @param messageFactory the message factory.
     * @param <T>            the argument's type.
     */
    <T> void print(@NotNull LoggerLevel level, @NotNull T arg, @NotNull Function<T, @NotNull String> messageFactory);

    /**
     * Print the message.
     *
     * @param level          the level of the message.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param messageFactory the message factory.
     * @param <F>            the first argument's type.
     * @param <S>            the second argument's type.
     */
    <F, S> void print(
            @NotNull LoggerLevel level,
            @Nullable F first,
            @Nullable S second,
            @NotNull BiFunction<F, S, @NotNull String> messageFactory
    );


    /**
     * Print the message.
     *
     * @param level          the level of the message.
     * @param first          the first arg for the message factory.
     * @param second         the second arg for the message factory.
     * @param third          the third arg for the message factory.
     * @param messageFactory the message factory.
     * @param <F>            the first argument's type.
     * @param <S>            the second argument's type.
     * @param <T>            the third argument's type.
     */
    <F, S, T> void print(
            @NotNull LoggerLevel level,
            @Nullable F first,
            @Nullable S second,
            @Nullable T third,
            @NotNull TripleFunction<F, S, T, @NotNull String> messageFactory
    );
}
