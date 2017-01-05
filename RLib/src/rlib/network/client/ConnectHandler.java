package rlib.network.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * The interface to implement a connection handler.
 *
 * @author JavaSaBr
 */
public interface ConnectHandler extends CompletionHandler<Void, AsynchronousSocketChannel> {

    @Override
    default void completed(@Nullable final Void result, @NotNull final AsynchronousSocketChannel attachment) {
        onConnect(attachment);
    }

    @Override
    default void failed(@NotNull final Throwable exc, @NotNull final AsynchronousSocketChannel attachment) {
        onFailed(exc);
    }

    /**
     * Handle a new connection.
     *
     * @param channel the channel.
     */
    void onConnect(@NotNull final AsynchronousSocketChannel channel);

    /**
     * Handle an exception.
     *
     * @param exc the exception.
     */
    void onFailed(@NotNull final Throwable exc);
}
