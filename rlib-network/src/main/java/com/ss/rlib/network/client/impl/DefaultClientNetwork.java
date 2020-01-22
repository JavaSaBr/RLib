package com.ss.rlib.network.client.impl;

import static com.ss.rlib.common.util.Utils.unchecked;
import static com.ss.rlib.common.util.Utils.uncheckedGet;
import com.ss.rlib.common.concurrent.util.ThreadUtils;
import com.ss.rlib.common.util.AsyncUtils;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.impl.AbstractNetwork;
import com.ss.rlib.network.util.NetworkUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

/**
 * The default implementation of a client network.
 *
 * @author JavaSaBr
 */
public class DefaultClientNetwork<C extends Connection<?, ?>> extends AbstractNetwork<C> implements ClientNetwork<C> {

    protected static final Logger LOGGER = LoggerManager.getLogger(DefaultClientNetwork.class);

    protected final @NotNull AtomicBoolean connecting;

    protected volatile @Nullable CompletableFuture<C> pendingConnection;
    protected volatile @Getter @Nullable C currentConnection;

    public DefaultClientNetwork(
        @NotNull NetworkConfig config,
        @NotNull BiFunction<Network<C>, AsynchronousSocketChannel, C> channelToConnection
    ) {
        super(config, channelToConnection);
        this.connecting = new AtomicBoolean(false);

        LOGGER.info(config, conf -> "Client network configuration: {\n" +
            "  groupName: \"" + conf.getThreadGroupName() + "\",\n" +
            "  readBufferSize: " + conf.getReadBufferSize() + ",\n" +
            "  pendingBufferSize: " + conf.getPendingBufferSize() + ",\n" +
            "  writeBufferSize: " + conf.getWriteBufferSize() + "\n" +
            "}");
    }

    @Override
    public @NotNull CompletableFuture<C> connect(@NotNull InetSocketAddress serverAddress) {

        C currentConnection = getCurrentConnection();

        if (currentConnection != null) {
            unchecked(currentConnection, C::close);
        }

        // if we are trying connection now
        if (!connecting.compareAndSet(false, true)) {

            var asyncResult = this.pendingConnection;

            if (asyncResult != null) {
                return asyncResult;
            }

            ThreadUtils.sleep(100);

            return connect(serverAddress);
        }

        var asyncResult = new CompletableFuture<C>();

        var channel = uncheckedGet(AsynchronousSocketChannel::open);
        channel.connect(serverAddress, null, new CompletionHandler<Void, Void>() {

            @Override
            public void completed(@Nullable Void result, @Nullable Void attachment) {
                LOGGER.info(channel, ch -> "Connected to server: " + NetworkUtils.getRemoteAddress(ch));
                asyncResult.complete(channelToConnection.apply(DefaultClientNetwork.this, channel));
            }

            @Override
            public void failed(@NotNull Throwable exc, @Nullable Void attachment) {
                asyncResult.completeExceptionally(exc);
            }
        });

        pendingConnection = asyncResult;

        return asyncResult.handle((connection, throwable) -> {
            this.currentConnection = connection;
            this.connecting.set(false);
            return AsyncUtils.continueCompletableStage(connection, throwable);
        });
    }

    @Override
    public @NotNull Mono<C> connected(@NotNull InetSocketAddress serverAddress) {
        return Mono.create(monoSink -> connect(serverAddress)
            .whenComplete((connection, ex) -> {
                if(ex != null) {
                    monoSink.error(ex);
                } else {
                    monoSink.success(connection);
                }
            }));
    }

    @Override
    public void shutdown() {
        Optional
            .ofNullable(getCurrentConnection())
            .ifPresent(connection -> unchecked(connection, C::close));
    }
}
