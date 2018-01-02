package com.ss.rlib.network.server;

import org.jetbrains.annotations.NotNull;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * The interface to implement a handler of accepted connections.
 *
 * @author JavaSaBr
 */
public interface AcceptHandler extends CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    @Override
    default void completed(@NotNull final AsynchronousSocketChannel result,
                           @NotNull final AsynchronousServerSocketChannel serverChannel) {
        serverChannel.accept(serverChannel, this);
        onAccept(result);
    }

    @Override
    default void failed(@NotNull final Throwable exc, @NotNull final AsynchronousServerSocketChannel serverChannel) {
        serverChannel.accept(serverChannel, this);
        onFailed(exc);
    }

    /**
     * Handle the new client connection.
     *
     * @param channel the client channel.
     */
    void onAccept(@NotNull AsynchronousSocketChannel channel);

    /**
     * Handle the exception.
     *
     * @param exception the exception.
     */
    void onFailed(@NotNull Throwable exception);
}
