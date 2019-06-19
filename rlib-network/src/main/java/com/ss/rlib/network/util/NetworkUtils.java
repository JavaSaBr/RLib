package com.ss.rlib.network.util;

import com.ss.rlib.common.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.stream.IntStream;

/**
 * @author JavaSaBr
 */
public class NetworkUtils {

    public static @NotNull SocketAddress getSocketAddress(@NotNull AsynchronousSocketChannel socketChannel) {
        return Utils.uncheckedGet(socketChannel, AsynchronousSocketChannel::getRemoteAddress);
    }

    /**
     * Prepare a string like 'HEX DUMP' by passed array.
     *
     * @param array the bytes array.
     * @param size  the size.
     * @return the hex dump string.
     */
    public static @NotNull String hexDump(@NotNull byte[] array, int size) {
        return hexDump(array, 0, size);
    }

    /**
     * Prepare a string like 'HEX DUMP' by passed array.
     *
     * @param array  the byte array.
     * @param offset the offset.
     * @param size   the size.
     * @return the hex dump string.
     */
    public static @NotNull String hexDump(@NotNull byte[] array, int offset, int size) {

        var builder = new StringBuilder();

        var count = 0;
        var end = size - 1;
        var chars = new char[16];

        for (int g = 0; g < 16; g++) {
            chars[g] = '.';
        }

        for (int i = offset; i < size; i++) {

            int val = array[i];

            if (val < 0) {
                val += 256;
            }

            var text = Integer.toHexString(val)
                .toUpperCase();

            if (text.length() == 1) {
                text = "0" + text;
            }

            char ch = (char) val;

            if (ch < 33) {
                ch = '.';
            }

            if (i == end) {

                chars[count] = ch;

                builder
                    .append(text)
                    .append("   ".repeat(15 - count))
                    .append("    ")
                    .append(chars)
                    .append('\n');

            } else if (count < 15) {
                chars[count++] = ch;
                builder.append(text).append(' ');
            } else {

                chars[15] = ch;

                builder
                    .append(text)
                    .append("    ")
                    .append(chars)
                    .append('\n');

                count = 0;

                for (int g = 0; g < 16; g++) {
                    chars[g] = 0x2E;
                }
            }
        }

        return builder.toString();
    }

    /**
     * Check a network port to know is available it or not to open a new socket.
     *
     * @param port the port.
     * @return true if the port is available.
     */
    public static boolean isPortAvailable(int port) {
        return isPortAvailable("*", port);
    }

    /**
     * Check a network port to know is available it or not to open a new socket.
     *
     * @param host the host.
     * @param port the port.
     * @return true if the port is available.
     */
    public static boolean isPortAvailable(@NotNull String host, int port) {
        try (var ignored = "*".equals(host) ? new ServerSocket(port) :
            new ServerSocket(port, 50, InetAddress.getByName(host))) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Get a nearest available network port from a start port.
     *
     * @param port the start port.
     * @return the nearest available network port or -1.
     */
    public static int getAvailablePort(int port) {
        return IntStream.range(port, Short.MAX_VALUE * 2)
            .filter(NetworkUtils::isPortAvailable)
            .findFirst()
            .orElse(-1);
    }
}
