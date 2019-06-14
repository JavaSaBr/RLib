package com.ss.rlib.network.util;

import com.ss.rlib.common.util.Utils;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.stream.IntStream;

public class NetworkUtils {

    public static @NotNull SocketAddress getSocketAddress(@NotNull AsynchronousSocketChannel socketChannel) {
        return Utils.uncheckedGet(socketChannel, AsynchronousSocketChannel::getRemoteAddress);
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
