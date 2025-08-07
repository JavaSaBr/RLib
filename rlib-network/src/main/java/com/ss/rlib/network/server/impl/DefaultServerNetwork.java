package com.ss.rlib.network.server.impl;

import static javasabr.rlib.common.util.Utils.uncheckedGet;
import javasabr.rlib.common.concurrent.GroupThreadFactory;
import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.common.util.Utils;
import javasabr.rlib.common.util.array.Array;
import javasabr.rlib.common.util.array.ArrayFactory;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.ServerNetworkConfig;
import com.ss.rlib.network.UnsafeConnection;
import com.ss.rlib.network.impl.AbstractNetwork;
import com.ss.rlib.network.server.ServerNetwork;
import com.ss.rlib.network.util.NetworkUtils;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

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
public final class DefaultServerNetwork<C extends UnsafeConnection<?, ?>> extends AbstractNetwork<C> implements
    ServerNetwork<C> {

    protected static final Logger LOGGER = LoggerManager.getLogger(DefaultServerNetwork.class);

    private interface ServerCompletionHandler<C extends UnsafeConnection<?, ?>> extends
        CompletionHandler<AsynchronousSocketChannel, DefaultServerNetwork<C>> {}

    private final ServerCompletionHandler<C> acceptHandler = new ServerCompletionHandler<>() {

        @Override
        public void completed(@NotNull AsynchronousSocketChannel channel, @NotNull DefaultServerNetwork<C> network) {
            var connection = network.channelToConnection.apply(DefaultServerNetwork.this, channel);
            LOGGER.debug(connection, conn -> "Accepted new connection: " + conn.getRemoteAddress());
            network.onAccept(connection);
            network.acceptNext();
        }

        @Override
        public void failed(@NotNull Throwable exc, @NotNull DefaultServerNetwork<C> network) {
            if (exc instanceof AsynchronousCloseException) {
                LOGGER.warning("Server network was closed");
            } else {
                LOGGER.error("Got exception during accepting new connection:");
                LOGGER.error(exc);

                if (channel.isOpen()) {
                    network.acceptNext();
                }
            }
        }
    };

    protected final AsynchronousChannelGroup group;
    protected final AsynchronousServerSocketChannel channel;
    protected final Array<Consumer<? super C>> subscribers;

    public DefaultServerNetwork(
        @NotNull ServerNetworkConfig config,
        @NotNull BiFunction<Network<C>, AsynchronousSocketChannel, C> channelToConnection
    ) {

        super(config, channelToConnection);

        var threadFactory = new GroupThreadFactory(
            config.getThreadGroupName(),
            config.getThreadConstructor(),
            config.getThreadPriority(),
            false
        );

        var executor = config.getThreadGroupMinSize() < config.getThreadGroupMaxSize() ? new ThreadPoolExecutor(
            config.getThreadGroupMinSize(),
            config.getThreadGroupMaxSize(),
            120,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy()
        ) : Executors.newFixedThreadPool(config.getThreadGroupMinSize(), threadFactory);

        // activate the executor
        executor.submit(() -> {});

        LOGGER.info(config, conf -> "Server network configuration: {\n" +
            "  minThreads: " + conf.getThreadGroupMinSize() + ",\n" +
            "  maxThreads: " + conf.getThreadGroupMaxSize() + ",\n" +
            "  priority: " + conf.getThreadPriority() + ",\n" +
            "  groupName: \"" + conf.getThreadGroupName() + "\",\n" +
            "  readBufferSize: " + conf.getReadBufferSize() + ",\n" +
            "  pendingBufferSize: " + conf.getPendingBufferSize() + ",\n" +
            "  writeBufferSize: " + conf.getWriteBufferSize() + "\n" +
            "}");

        this.group = uncheckedGet(executor, AsynchronousChannelGroup::withThreadPool);
        this.channel = uncheckedGet(group, AsynchronousServerSocketChannel::open);
        this.subscribers = ArrayFactory.newCopyOnModifyArray(Consumer.class);
    }

    @Override
    public @NotNull InetSocketAddress start() {

        InetSocketAddress address = null;

        while (address == null) {

            address = new InetSocketAddress(NetworkUtils.getAvailablePort(1500));
            try {
                channel.bind(address);
            } catch (IOException e) {
                address = null;
            }
        }

        LOGGER.info(address, adr -> "Started server socket on address: " + adr);

        if (!subscribers.isEmpty()) {
            acceptNext();
        }

        return address;
    }

    @Override
    public <S extends ServerNetwork<C>> @NotNull S start(@NotNull InetSocketAddress serverAddress) {
        Utils.unchecked(channel, serverAddress, AsynchronousServerSocketChannel::bind);

        LOGGER.info(serverAddress, addr -> "Started server socket on address: " + addr);

        if (!subscribers.isEmpty()) {
            acceptNext();
        }

        return ClassUtils.unsafeNNCast(this);
    }

    protected void acceptNext() {
        if (channel.isOpen()) {
            try { channel.accept(this, acceptHandler); }
            catch (AcceptPendingException ignored) {}
        } else {
            LOGGER.warning("Cannot accept a next connection because server channel is already closed");
        }
    }

    protected void onAccept(@NotNull C connection) {
        connection.onConnected();
        subscribers.forEachR(connection, Consumer::accept);
    }

    @Override
    public void onAccept(@NotNull Consumer<? super C> consumer) {
        subscribers.add(consumer);
        acceptNext();
    }

    @Override
    public @NotNull Flux<C> accepted() {
        return Flux.create(this::registerFluxOnAccepted);
    }

    protected void registerFluxOnAccepted(@NotNull FluxSink<C> sink) {
        Consumer<? super C> listener = sink::next;
        onAccept(listener);
        sink.onDispose(() -> subscribers.remove(listener));
    }

    @Override
    public void shutdown() {
        Utils.unchecked(channel, AsynchronousChannel::close);
        group.shutdown();
    }
}
