package com.ss.rlib.network.server.impl;

import static com.ss.rlib.common.util.Utils.uncheckedGet;
import com.ss.rlib.common.concurrent.GroupThreadFactory;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.Utils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.ServerNetworkConfig;
import com.ss.rlib.network.impl.AbstractNetwork;
import com.ss.rlib.network.server.ServerNetwork;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * The base implementation of {@link ServerNetwork}.
 *
 * @author JavaSaBr
 */
public final class DefaultServerNetwork<C extends Connection<?, ?>> extends AbstractNetwork<C> implements
    ServerNetwork<C> {

    private static final Logger LOGGER = LoggerManager.getLogger(DefaultServerNetwork.class);

    private final CompletionHandler<AsynchronousSocketChannel, DefaultServerNetwork<C>> acceptHandler = new CompletionHandler<>() {

        @Override
        public void completed(@NotNull AsynchronousSocketChannel channel, @NotNull DefaultServerNetwork<C> network) {
            network.onAccept(network.channelToConnection.apply(DefaultServerNetwork.this, channel));
            network.acceptNext();
        }

        @Override
        public void failed(@NotNull Throwable exc, @NotNull DefaultServerNetwork<C> network) {
            LOGGER.error(exc);
            network.acceptNext();
        }
    };

    protected final AsynchronousChannelGroup group;
    protected final AsynchronousServerSocketChannel channel;
    protected final Array<Consumer<? super C>> subscribers;
    protected final ConnectableFlux<C> acceptStream;

    public DefaultServerNetwork(
        @NotNull ServerNetworkConfig config,
        @NotNull BiFunction<Network<C>, AsynchronousSocketChannel, C> channelToConnection
    ) {

        super(config, channelToConnection);

        var threadFactory = new GroupThreadFactory(
            config.getGroupName(),
            config.getThreadConstructor(),
            config.getThreadPriority()
        );

        var executor = config.getGroupSize() < config.getGroupMaxSize() ? new ThreadPoolExecutor(
            config.getGroupSize(),
            config.getGroupMaxSize(),
            120,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy()
        ) : Executors.newFixedThreadPool(config.getGroupSize(), threadFactory);

        this.group = uncheckedGet(executor, AsynchronousChannelGroup::withThreadPool);
        this.channel = uncheckedGet(group, AsynchronousServerSocketChannel::open);
        this.subscribers = ArrayFactory.newCopyOnModifyArray(Consumer.class);
        this.acceptStream = Flux.<C>create(fluxSink -> onAccept(fluxSink::next))
            .publish();
    }

    @Override
    public <S extends ServerNetwork<C>> @NotNull S start(@NotNull InetSocketAddress serverAddress) {
        Utils.unchecked(channel, serverAddress, AsynchronousServerSocketChannel::bind);
        acceptNext();
        return ClassUtils.unsafeCast(this);
    }

    protected void acceptNext() {
        channel.accept(this, acceptHandler);
    }

    protected void onAccept(@NotNull C connection) {
        subscribers.forEach(connection, Consumer::accept);
    }

    @Override
    public void onAccept(@NotNull Consumer<? super C> consumer) {
        subscribers.add(consumer);
    }

    @Override
    public @NotNull ConnectableFlux<C> accept() {
        return acceptStream;
    }

    @Override
    public void shutdown() {
        Utils.unchecked(channel, AsynchronousChannel::close);
        group.shutdown();
    }
}
