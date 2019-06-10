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

import java.io.IOException;
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
            LOGGER.debug(channel, ch -> "Accepted new connection: " + ch);
            network.onAccept(network.channelToConnection.apply(DefaultServerNetwork.this, channel));
            network.acceptNext();
        }

        @Override
        public void failed(@NotNull Throwable exc, @NotNull DefaultServerNetwork<C> network) {

            LOGGER.error("Got exception during accepting new connection:");
            LOGGER.error(exc);

            if (channel.isOpen()) {
                network.acceptNext();
            }
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

        LOGGER.info("Executor configuration:");
        LOGGER.info(config, conf -> "Min threads: " + conf.getGroupSize());
        LOGGER.info(config, conf -> "Max threads: " + conf.getGroupMaxSize());

        this.group = uncheckedGet(executor, AsynchronousChannelGroup::withThreadPool);
        this.channel = uncheckedGet(group, AsynchronousServerSocketChannel::open);
        this.subscribers = ArrayFactory.newCopyOnModifyArray(Consumer.class);
        this.acceptStream = Flux.<C>create(fluxSink -> onAccept(fluxSink::next))
            .publish();
    }

    @Override
    public @NotNull InetSocketAddress start() {

        InetSocketAddress address = null;

        while (address == null) {

            address = new InetSocketAddress(Utils.getFreePort(1500));
            try {
                channel.bind(address);
            } catch (IOException e) {
                address = null;
            }
        }

        LOGGER.info(address, adr -> "Started server on address: " + adr);
        acceptNext();
        return address;
    }

    @Override
    public <S extends ServerNetwork<C>> @NotNull S start(@NotNull InetSocketAddress serverAddress) {
        Utils.unchecked(channel, serverAddress, AsynchronousServerSocketChannel::bind);
        LOGGER.info(serverAddress, adr -> "Started server on address: " + adr);
        acceptNext();
        return ClassUtils.unsafeCast(this);
    }

    protected void acceptNext() {
        if (channel.isOpen()) {
            channel.accept(this, acceptHandler);
        } else {
            LOGGER.warning("Cannot accept a next connection because server channel is already closed.");
        }
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
