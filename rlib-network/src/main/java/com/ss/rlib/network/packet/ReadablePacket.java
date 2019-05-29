package com.ss.rlib.network.packet;

import com.ss.rlib.network.Connection;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The interface to implement a readable network packet.
 *
 * @author JavaSaBr
 */
public interface ReadablePacket extends Packet {

    /**
     * Read this packet.
     *
     * @param connection the network connection.
     * @param buffer     the buffer with received.
     * @param length     the data length.
     * @return true if reading was success.
     */
    boolean read(@NotNull Connection<?, ?> connection, @NotNull ByteBuffer buffer, int length);
}
