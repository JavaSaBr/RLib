package com.ss.rlib.network;

import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.WritablePacket;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The interface to implement an async connection.
 *
 * @author JavaSaBr
 */
public interface Connection<R extends ReadablePacket, W extends WritablePacket> {

    /**
     * Get a remote address or 'unkonwn'.
     *
     * @return the remote address or 'unkonwn'.
     */
    @NotNull String getRemoteAddress();

    /**
     * Get a timestamp of last write/read activity.
     *
     * @return the timestamp of last write/read activity.
     */
    long getLastActivity();

    /**
     * Close this connection if this connection is still opened.
     */
    void close();

    /**
     * Check a closed state of this connection.
     *
     * @return true if this connection is already closed.
     */
    boolean isClosed();

    /**
     * Send a packet to connection's owner.
     *
     * @param packet the writable packet.
     */
    void send(@NotNull W packet);

    /**
     * Register a consumer to handle received packets.
     *
     * @param consumer the consumer.
     */
    void onReceive(@NotNull Consumer<? super R> consumer);

    /**
     * Get a stream of received packets.
     *
     * @return the stream of received packets.
     */
    @NotNull Flux<? extends R> receive();

    /**
     * Get a used implementation of a data crypt.
     *
     * @return the used implementation of a data crypt.
     */
    @NotNull NetworkCryptor getCrypt();
}
