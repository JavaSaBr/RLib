package com.ss.rlib.common.util;

import static java.lang.ThreadLocal.withInitial;
import com.ss.rlib.common.function.*;
import com.ss.rlib.common.logging.LoggerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The utility class.
 *
 * @author JavaSaBr
 */
public final class Utils {

    private static final ThreadLocal<SimpleDateFormat> LOCAL_DATE_FORMAT = withInitial(() ->
            new SimpleDateFormat("HH:mm:ss:SSS"));

    private static final ThreadLocal<Date> LOCAL_DATE = withInitial(Date::new);

    /**
     * Check a port.
     *
     * @param host the host.
     * @param port the port.
     * @return true if the port is free.
     */
    public static boolean checkFreePort(@NotNull String host, int port) {

        try {

            ServerSocket serverSocket = host.equalsIgnoreCase("*") ?
                    new ServerSocket(port) : new ServerSocket(port, 50, InetAddress.getByName(host));

            serverSocket.close();

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Check ports.
     *
     * @param host  the host.
     * @param ports the ports.
     * @return true if all ports are free.
     * @throws InterruptedException the interrupted exception
     */
    public static boolean checkFreePorts(@NotNull final String host, @NotNull final int... ports)
            throws InterruptedException {

        for (final int port : ports) {
            try {

                final ServerSocket serverSocket = host.equalsIgnoreCase("*") ?
                        new ServerSocket(port) : new ServerSocket(port, 50, InetAddress.getByName(host));
                serverSocket.close();

            } catch (final IOException e) {
                return false;
            }
        }

        return true;
    }

    /**
     * Format a time to a string date using a format days/hours/minutes/second.
     *
     * @param time the timestamp.
     * @return the string
     */
    public static @NotNull String formatTime(final long time) {

        final Date date = LOCAL_DATE.get();
        date.setTime(time);

        final SimpleDateFormat format = LOCAL_DATE_FORMAT.get();
        return format.format(date);
    }

    /**
     * Convert the string from HEX to a plain string.
     *
     * @param string the HEX string.
     * @return the plain string.
     */
    @Deprecated(forRemoval = true)
    public static @NotNull String fromHEX(@NotNull String string) {
        return fromHex(string);
    }

    /**
     * Convert the string from HEX to a plain string.
     *
     * @param string the HEX string.
     * @return the plain string.
     * @since 8.1.0
     */
    public static @NotNull String fromHex(@NotNull String string) {

        var array = string.toCharArray();
        var builder = new StringBuilder(string.length() / 4);

        for (int i = 0, length = array.length - 3; i < length; i += 4) {
            var element = String.valueOf(array, i, 4);
            builder.append((char) Integer.parseInt(element, 16));
        }

        return builder.toString();
    }

    /**
     * Get a nearest free port from a port.
     *
     * @param port the start port.
     * @return the free port or -1.
     */
    public static int getFreePort(final int port) {

        final int limit = Short.MAX_VALUE * 2;

        for (int i = port; i < limit; i++) {
            if (checkFreePort("*", i)) return i;
        }

        return -1;
    }

    /**
     * Get a folder of a class.
     *
     * @param cs the class.
     * @return the path to the folder.
     */
    public static @NotNull Path getRootFolderFromClass(@NotNull final Class<?> cs) {

        String className = cs.getName();

        final StringBuilder builder = new StringBuilder(className.length());
        builder.append('/');

        for (int i = 0, length = className.length(); i < length; i++) {

            char ch = className.charAt(i);
            if (ch == '.') ch = '/';

            builder.append(ch);
        }

        builder.append(".class");

        className = builder.toString();

        try {

            final URL url = Utils.class.getResource(className);

            String path = url.getPath();
            path = path.substring(0, path.length() - className.length());
            path = path.substring(0, path.lastIndexOf('/'));

            final URI uri = new URI(path);
            path = uri.getPath();
            path = path.replaceAll("%20", " ");

            // замена сепараторов
            if (File.separatorChar != '/') {

                final StringBuilder pathBuilder = new StringBuilder();

                for (int i = 0, length = path.length(); i < length; i++) {

                    char ch = path.charAt(i);
                    if (ch == '/' && i == 0) continue;
                    if (ch == '/') ch = File.separatorChar;

                    pathBuilder.append(ch);
                }

                path = pathBuilder.toString();
            }

            Path file = Paths.get(path);

            while (path.lastIndexOf(File.separatorChar) != -1 && !Files.exists(file)) {
                path = path.substring(0, path.lastIndexOf(File.separatorChar));
                file = Paths.get(uri);
            }

            return file;

        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a short value from a byte array.
     *
     * @param bytes  the byte array.
     * @param offset the offset.
     * @return the short value.
     */
    public static short getShort(final byte[] bytes, final int offset) {
        return (short) (bytes[offset + 1] << 8 | bytes[offset] & 0xff);
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
     * Формирования дампа байтов в хексе.
     *
     * @param array массив байтов.
     * @param size  the size
     * @return строка с дампом.
     */
    public static @NotNull String hexdump(final byte[] array, final int size) {
        return hexdump(array, 0, size);
    }

    /**
     * Prepare a hexdump for a byte array.
     *
     * @param array  the byte array.
     * @param offset the offset
     * @param size   the size
     * @return the string dump.
     */
    public static @NotNull String hexdump(@NotNull final byte[] array, final int offset, final int size) {

        final StringBuilder builder = new StringBuilder();

        int count = 0;
        final int end = size - 1;

        final char[] chars = new char[16];

        for (int g = 0; g < 16; g++) {
            chars[g] = '.';
        }

        for (int i = offset; i < size; i++) {

            int val = array[i];

            if (val < 0) {
                val += 256;
            }

            String text = Integer.toHexString(val).toUpperCase();

            if (text.length() == 1) {
                text = "0" + text;
            }

            char ch = (char) val;

            if (ch < 33) {
                ch = '.';
            }

            if (i == end) {

                chars[count] = ch;

                builder.append(text);

                for (int j = 0; j < 15 - count; j++) {
                    builder.append("   ");
                }

                builder.append("    ").append(chars).append('\n');
            } else if (count < 15) {
                chars[count++] = ch;
                builder.append(text).append(' ');
            } else {

                chars[15] = ch;

                builder.append(text).append("    ").append(chars).append('\n');

                count = 0;

                for (int g = 0; g < 16; g++) {
                    chars[g] = 0x2E;
                }
            }
        }

        return builder.toString();
    }

    /**
     * Execute the function with auto-converting checked exception to runtime.
     *
     * @param function the function.
     */
    public static void run(@NotNull SafeRunnable function) {
        try {
            function.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute the function with handling an exception.
     *
     * @param function     the function.
     * @param errorHandler the handler.
     */
    public static void run(@NotNull SafeRunnable function, @NotNull Consumer<Exception> errorHandler) {
        try {
            function.run();
        } catch (Exception e) {
            errorHandler.accept(e);
        }
    }

    /**
     * Execute the function with auto-converting checked exception to runtime.
     *
     * @param <R>      the type parameter
     * @param function the function.
     * @return the result.
     */
    public static <R> @NotNull R get(@NotNull SafeFactory<R> function) {
        try {
            return function.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute the function with auto-converting checked exception to runtime.
     *
     * @param <R>      the type parameter
     * @param function the function.
     * @return the result.
     */
    public static <R> @Nullable R getNullable(@NotNull SafeFactory<R> function) {
        try {
            return function.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute the function with handling an exception.
     *
     * @param <R>          the result's type.
     * @param function     the function.
     * @param errorHandler the handler.
     * @return the result or null.
     */
    public static <R> @Nullable R get(
            @NotNull SafeFactory<R> function,
            @NotNull Consumer<Exception> errorHandler
    ) {

        try {
            return function.get();
        } catch (Exception e) {
            errorHandler.accept(e);
        }

        return null;
    }

    /**
     * Execute the function with auto-converting checked exception to runtime.
     *
     * @param <F>      the argument's type.
     * @param first    the first argument.
     * @param function the function.
     */
    public static <F> void run(@Nullable F first, @NotNull SafeConsumer<F> function) {
        try {
            function.accept(first);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute the function with auto-converting checked exception to runtime.
     *
     * @param first    the first argument.
     * @param function the function.
     * @param <F> the first argument's type.
     * @param <R> the result's type.
     * @return the result.
     */
    public static <F, R> @NotNull R get(
            @NotNull F first,
            @NotNull SafeFunction<F, R> function
    ) {
        try {
            return function.apply(first);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute the function with skipping any exception.
     *
     * @param first    the first argument.
     * @param function the function.
     * @param <F> the first argument's type.
     * @param <R> the result's type.
     * @return the result or null.
     */
    public static <F, R> @Nullable R safeGet(
            @NotNull F first,
            @NotNull SafeFunction<F, R> function
    ) {
        try {
            return function.apply(first);
        } catch (Exception e) {
            // can be ignored
            return null;
        }
    }

    /**
     * Execute the function with skipping any exception.
     *
     * @param first    the first argument.
     * @param function the function.
     * @param <F> the first argument's type.
     * @param <R> the result's type.
     * @return the optional result.
     */
    public static <F, R> @NotNull Optional<R> safeGetOpt(
            @NotNull F first,
            @NotNull SafeFunction<F, R> function
    ) {
        try {
            return Optional.of(function.apply(first));
        } catch (Exception e) {
            // can be ignored
            return Optional.empty();
        }
    }

    /**
     * Execute the function with auto-converting checked exception to runtime.
     *
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     * @param <F> the first argument's type.
     * @param <S> the second argument's type.
     * @param <R> the result's type.
     * @return the result.
     */
    public static <F, S, R> @NotNull R get(
            @NotNull F first,
            @NotNull S second,
            @NotNull SafeBiFunction<F, S, R> function
    ) {
        try {
            return function.apply(first, second);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute the function with skipping any exception.
     *
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     * @param <F> the first argument's type.
     * @param <S> the second argument's type.
     * @param <R> the result's type.
     * @return the result or null.
     */
    public static <F, S, R> @Nullable R safeGet(
            @NotNull F first,
            @NotNull S second,
            @NotNull SafeBiFunction<F, S, R> function
    ) {
        try {
            return function.apply(first, second);
        } catch (Exception e) {
            // can be ignored
            return null;
        }
    }

    /**
     * Execute the function with skipping any exception.
     *
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     * @param <F> the first argument's type.
     * @param <S> the second argument's type.
     * @param <R> the result's type.
     * @return the optional result.
     */
    public static <F, S, R> @NotNull Optional<R> safeGetOpt(
            @NotNull F first,
            @NotNull S second,
            @NotNull SafeBiFunction<F, S, R> function
    ) {
        try {
            return Optional.of(function.apply(first, second));
        } catch (Exception e) {
            // can be ignored
            return Optional.empty();
        }
    }

    /**
     * Execute the function with auto-converting checked exception to runtime.
     *
     * @param <F>      the type parameter
     * @param <R>      the type parameter
     * @param first    the first argument.
     * @param function the function.
     * @return the result or null.
     */
    public static <F, R> @Nullable R getNullable(
            @Nullable F first,
            @NotNull SafeFunction<F, R> function
    ) {
        try {
            return function.apply(first);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute the function with auto-converting checked exception to runtime.
     *
     * @param <F>          the argument's type.
     * @param first        the first argument.
     * @param function     the function.
     * @param errorHandler the handler.
     */
    public static <F> void run(
            @Nullable F first,
            @NotNull SafeConsumer<F> function,
            @NotNull Consumer<Exception> errorHandler
    ) {
        try {
            function.accept(first);
        } catch (Exception e) {
            errorHandler.accept(e);
        }
    }

    /**
     * Execute the function with auto-converting checked exception to runtime.
     *
     * @param <F>          the argument's type.
     * @param <R>          the result's type.
     * @param first        the first argument.
     * @param function     the function.
     * @param errorHandler the handler.
     * @return the result or null.
     */
    public static <F, R> @Nullable R get(
            @Nullable F first,
            @NotNull SafeFunction<F, R> function,
            @NotNull Consumer<Exception> errorHandler
    ) {

        try {
            return function.apply(first);
        } catch (Exception e) {
            errorHandler.accept(e);
        }

        return null;
    }

    /**
     * Execute the function with auto-converting checked exception to runtime.
     *
     * @param <F>      the first argument's type.
     * @param <S>      the second argument's type.
     * @param first    the first argument.
     * @param second   the second argument.
     * @param consumer the function,
     */
    public static <F, S> void run(@Nullable F first, @Nullable S second, @NotNull SafeBiConsumer<F, S> consumer) {
        try {
            consumer.accept(first, second);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute a function with auto-converting checked exception to runtime.
     *
     * @param <F>          the first argument's type.
     * @param <S>          the second argument's type.
     * @param first        the first argument.
     * @param second       the second argument.
     * @param function     the function.
     * @param errorHandler the handler.
     */
    public static <F, S> void run(
            @Nullable F first,
            @Nullable S second,
            @NotNull SafeBiConsumer<F, S> function,
            @NotNull Consumer<Exception> errorHandler
    ) {
        try {
            function.accept(first, second);
        } catch (Exception e) {
            errorHandler.accept(e);
        }
    }

    /**
     * Convert a string to HEX string.
     *
     * @param string the original string.
     * @return the hex string.
     * @see #toHex(String)
     */
    @Deprecated(forRemoval = true)
    public static @NotNull String toHEX(@NotNull String string) {
        return toHex(string);
    }

    /**
     * Convert the plain string to a HEX string.
     *
     * @param string the plain string.
     * @return the hex string.
     * @since 8.1.0
     */
    public static @NotNull String toHex(@NotNull String string) {

        var builder = new StringBuilder(string.length() * 2);

        for (int i = 0, length = string.length(); i < length; i++) {

            var charAt = string.charAt(i);
            var element = Integer.toHexString(charAt);

            if (element.length() == 1) {
                element = "000" + element;
            } else if (element.length() == 2) {
                element = "00" + element;
            } else if (element.length() == 3) {
                element = "0" + element;
            }

            builder.append(element);
        }

        return builder.toString();
    }

    /**
     * Print.
     *
     * @param owner the owner
     * @param e     the e
     */
    public static void print(@NotNull final Class<?> owner, @NotNull final Exception e) {
        LoggerManager.getDefaultLogger().warning(owner, e);
    }

    /**
     * Print.
     *
     * @param owner   the owner
     * @param message the message
     */
    public static void print(@NotNull final Class<?> owner, @NotNull final String message) {
        LoggerManager.getDefaultLogger().warning(owner, message);
    }

    private Utils() {
        throw new RuntimeException();
    }
}