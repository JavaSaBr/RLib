package rlib.network.server;

import org.jetbrains.annotations.NotNull;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * The base implementation of an accept handler.
 *
 * @author JavaSaBr
 */
public abstract class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    protected static final Logger LOGGER = LoggerManager.getLogger(AcceptHandler.class);

    @Override
    public void completed(@NotNull final AsynchronousSocketChannel result,
                          @NotNull final AsynchronousServerSocketChannel serverChannel) {
        serverChannel.accept(serverChannel, this);
        onAccept(result);
    }

    @Override
    public void failed(@NotNull final Throwable exc,
                       @NotNull final AsynchronousServerSocketChannel serverChannel) {
        serverChannel.accept(serverChannel, this);
        onFailed(exc);
    }

    /**
     * Handle a new client connection.
     *
     * @param channel the channel.
     */
    protected abstract void onAccept(@NotNull AsynchronousSocketChannel channel);

    /**
     * Handle an exception.
     *
     * @param e the exception.
     */
    protected abstract void onFailed(@NotNull Throwable e);
}
