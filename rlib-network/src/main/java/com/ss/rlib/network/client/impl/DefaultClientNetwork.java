package com.ss.rlib.network.client.impl;

import static com.ss.rlib.common.util.Utils.unchecked;
import static com.ss.rlib.common.util.Utils.uncheckedGet;
import com.ss.rlib.common.concurrent.GroupThreadFactory;
import com.ss.rlib.common.concurrent.atomic.AtomicReference;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.impl.AbstractNetwork;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;

/**
 * The default implementation of a client network.
 *
 * @author JavaSaBr
 */
public class DefaultClientNetwork<C extends Connection<?, ?>> extends AbstractNetwork<C> implements ClientNetwork<C> {

    protected final AsynchronousChannelGroup group;
    protected final AtomicReference<C> currentConnection;

    public DefaultClientNetwork(
        @NotNull NetworkConfig config,
        @NotNull BiFunction<Network<C>, AsynchronousSocketChannel, C> channelToConnection
    ) {
        super(config, channelToConnection);
        this.currentConnection = new AtomicReference<>();
        this.group = uncheckedGet(
            Executors.newSingleThreadExecutor(new GroupThreadFactory(config.getGroupName())),
            AsynchronousChannelGroup::withThreadPool
        );
    }

    @Override
    public @NotNull CompletableFuture<C> connect(@NotNull InetSocketAddress serverAddress) {

        C connection = getCurrentConnection();

        if (connection != null) {
            unchecked(connection, C::close);
        }

        var asyncResult = new CompletableFuture<C>();
        var channel = uncheckedGet(group, AsynchronousSocketChannel::open);

        var newConnection = channelToConnection.apply(this, channel);

        if (!currentConnection.compareAndSet(connection, newConnection)) {
            return CompletableFuture.failedFuture(new IllegalStateException());
        }

        channel.connect(serverAddress, null, new CompletionHandler<Void, Void>() {

            @Override
            public void completed(Void result, Void attachment) {
                asyncResult.complete(newConnection);
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                asyncResult.completeExceptionally(exc);
            }
        });

        return asyncResult;
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
    public @Nullable C getCurrentConnection() {
        return currentConnection.get();
    }

    @Override
    public void shutdown() {
        Optional
            .ofNullable(getCurrentConnection())
            .ifPresent(connection -> {
                unchecked(connection, C::close);
                unchecked(group, AsynchronousChannelGroup::shutdown);
            });
    }
}
