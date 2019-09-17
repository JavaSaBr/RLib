package com.ss.rlib.common.util;

import com.ss.rlib.common.function.*;
import com.ss.rlib.logger.api.LoggerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The utility class.
 *
 * @author JavaSaBr
 */
public final class Utils {

    /**
     * Get a folder of a class.
     *
     * @param cs the class.
     * @return the path to the folder.
     */
    public static @NotNull Path getRootFolderFromClass(@NotNull final Class<?> cs) {

        var className = cs.getName();
        var builder = new StringBuilder(className.length())
            .append('/');

        for (int i = 0, length = className.length(); i < length; i++) {

            var ch = className.charAt(i);

            if (ch == '.') {
                ch = '/';
            }

            builder.append(ch);
        }

        builder.append(".class");

        className = builder.toString();

        try {

            var url = Utils.class.getResource(className);

            var path = url.getPath();
            path = path.substring(0, path.length() - className.length());
            path = path.substring(0, path.lastIndexOf('/'));

            var uri = new URI(path);

            path = uri.getPath();
            path = path.replaceAll("%20", " ");

            if (File.separatorChar != '/') {

                var pathBuilder = new StringBuilder();

                for (int i = 0, length = path.length(); i < length; i++) {

                    var ch = path.charAt(i);

                    if (ch == '/' && i == 0) {
                        continue;
                    }

                    if (ch == '/') {
                        ch = File.separatorChar;
                    }

                    pathBuilder.append(ch);
                }

                path = pathBuilder.toString();
            }

            var file = Paths.get(path);

            while (path.lastIndexOf(File.separatorChar) != -1 && !Files.exists(file)) {
                path = path.substring(0, path.lastIndexOf(File.separatorChar));
                file = Paths.get(uri);
            }

            return file;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a username of a computer user.
     *
     * @return the username.
     */
    public static @NotNull String getUserName() {
        return System.getProperty("user.name");
    }

    /**
     * Execute the function with auto-converting a checked exception to an unchecked.
     *
     * @param function the function.
     */
    public static void unchecked(@NotNull SafeRunnable function) {
        try {
            function.run();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute the function with auto-converting a checked exception to an unchecked.
     *
     * @param <F>      the argument's type.
     * @param first    the argument.
     * @param function the function.
     */
    public static <F> void unchecked(@NotNull F first, @NotNull SafeConsumer<@NotNull F> function) {
        try {
            function.accept(first);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute the function with auto-converting a checked exception to an unchecked.
     *
     * @param <F>      the first argument's type.
     * @param <S>      the second argument's type.
     * @param first    the first argument.
     * @param second   the second argument.
     * @param consumer the function.
     */
    public static <F, S> void unchecked(
        @NotNull F first,
        @NotNull S second,
        @NotNull SafeBiConsumer<@NotNull F, @NotNull S> consumer
    ) {
        try {
            consumer.accept(first, second);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute the function with auto-converting a checked exception to an unchecked.
     *
     * @param <R>      the result type.
     * @param function the function.
     * @return the result.
     */
    public static <R> @NotNull R uncheckedGet(@NotNull SafeFactory<@NotNull R> function) {
        try {
            return function.get();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute the function with auto-converting a checked exception to an unchecked.
     *
     * @param <F>      the argument's type.
     * @param <R>      the result's type.
     * @param argument the argument.
     * @param function the function.
     * @return the result.
     */
    public static <F, R> @NotNull R uncheckedGet(
        @NotNull F argument,
        @NotNull SafeFunction<F, R> function
    ) {
        try {
            return function.apply(argument);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute the function with returning default value if got an exception.
     *
     * @param <F>      the argument's type.
     * @param <R>      the result's type.
     * @param argument the argument.
     * @param function the function.
     * @param def      the default value.
     * @return the result.
     */
    public static <F, R> @NotNull R uncheckedGet(
        @NotNull F argument,
        @NotNull SafeFunction<F, R> function,
        @NotNull R def
    ) {
        try {
            return function.apply(argument);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Execute the function with auto-converting a checked exception to an unchecked.
     *
     * @param <F>      the first's type.
     * @param <S>      the second's type.
     * @param <R>      the result's type.
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     * @return the result.
     * @since 9.2.1
     */
    public static <F, S, R> @NotNull R uncheckedGet(
        @NotNull F first,
        @NotNull S second,
        @NotNull SafeBiFunction<F, S, R> function
    ) {
        try {
            return function.apply(first, second);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Try to execute a function with some result.
     *
     * @param <F>      the argument's type.
     * @param <R>      the result's type.
     * @param argument the argument.
     * @param function the function.
     * @return the result or null.
     */
    public static <F, R> @Nullable R tryGet(
        @NotNull F argument,
        @NotNull SafeFunction<@NotNull F, @NotNull R> function
    ) {
        try {
            return function.apply(argument);
        } catch (Exception e) {
            // can be ignored
            return null;
        }
    }

    /**
     * Try to execute a function with some result.
     *
     * @param <F>      the argument's type.
     * @param <R>      the result's type.
     * @param argument the argument.
     * @param function the function.
     * @return the result or null.
     */
    public static <F, R> @NotNull R tryGet(
        @NotNull F argument,
        @NotNull SafeFunction<@NotNull F, @NotNull R> function,
        @NotNull R def
    ) {
        try {
            return function.apply(argument);
        } catch (Exception e) {
            // can be ignored
            return def;
        }
    }

    /**
     * Try to execute a function with some result and convert this result to another.
     *
     * @param <F>             the argument's type.
     * @param <R>             the result's type.
     * @param argument        the argument.
     * @param function        the function.
     * @param resultConverter the result converter.
     * @return the final result or null.
     */
    public static <F, R, FR> @Nullable FR tryGetAndConvert(
        @NotNull F argument,
        @NotNull SafeFunction<@NotNull F, @NotNull R> function,
        @NotNull SafeFunction<@NotNull R, @NotNull FR> resultConverter
    ) {
        try {
            return resultConverter.apply(function.apply(argument));
        } catch (Exception e) {
            // can be ignored
            return null;
        }
    }

    /**
     * Print an exception to log.
     *
     * @param owner     the owner.
     * @param exception the exception.
     */
    public static void print(@NotNull Class<?> owner, @NotNull Exception exception) {
        LoggerManager.getDefaultLogger().warning(owner, exception);
    }

    /**
     * Print a message to log.
     *
     * @param owner   the owner.
     * @param message the message.
     */
    public static void print(@NotNull Class<?> owner, @NotNull String message) {
        LoggerManager.getDefaultLogger().warning(owner, message);
    }

    private Utils() {
        throw new RuntimeException();
    }
}
