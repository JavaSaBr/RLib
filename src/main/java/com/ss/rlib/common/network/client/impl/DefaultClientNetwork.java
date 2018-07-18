package com.ss.rlib.common.network.client.impl;

import com.ss.rlib.common.concurrent.GroupThreadFactory;
import com.ss.rlib.common.network.NetworkConfig;
import com.ss.rlib.common.network.client.ClientNetwork;
import com.ss.rlib.common.network.client.ConnectHandler;
import com.ss.rlib.common.network.client.server.Server;
import com.ss.rlib.common.network.impl.AbstractAsyncNetwork;
import com.ss.rlib.common.network.packet.ReadablePacketRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The base implementation of a async client network.
 *
 * @author JavaSaBr
 */
public final class DefaultClientNetwork extends AbstractAsyncNetwork implements ClientNetwork {

    /**
     * The asynchronous channel group.
     */
    @NotNull
    protected final AsynchronousChannelGroup group;

    /**
     * The connection handler.
     */
    @NotNull
    protected final ConnectHandler connectHandler;

    /**
     * The asynchronous channel.
     */
    @Nullable
    protected AsynchronousSocketChannel channel;

    /**
     * The current server.
     */
    @Nullable
    protected volatile Server currentServer;

    public DefaultClientNetwork(
            @NotNull NetworkConfig config,
            @NotNull ReadablePacketRegistry packetRegistry,
            @NotNull ConnectHandler connectHandler
    ) throws IOException {

        super(config, packetRegistry);

        this.group = AsynchronousChannelGroup.withFixedThreadPool(config.getGroupSize(),
                new GroupThreadFactory(config.getGroupName(), config.getThreadClass(), config.getThreadPriority()));

        this.connectHandler = connectHandler;
    }

    /**
     * Prepare the channel to connect.
     *
     * @return the prepared socket channel.
     */
    protected @NotNull AsynchronousSocketChannel prepareChannel() {
        try {
            if (channel != null) channel.close();
            channel = AsynchronousSocketChannel.open(group);
            return channel;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public synchronized void asyncConnect(@NotNull InetSocketAddress serverAddress) {
        prepareChannel().connect(serverAddress, this, connectHandler);
    }

    @Override
    public synchronized Server connect(@NotNull InetSocketAddress serverAddress) {

        Future<Void> future = prepareChannel().connect(serverAddress);
        try {
            future.get();
        } catch (final InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        connectHandler.completed(null, this);

        return getCurrentServer();
    }

    @Override
    public synchronized void shutdown() {

        Server currentServer = getCurrentServer();
        if (currentServer != null) {
            currentServer.destroy();
        }

        group.shutdown();
    }

    @Override
    public @Nullable AsynchronousSocketChannel getChannel() {
        return channel;
    }

    @Override
    public @Nullable Server getCurrentServer() {
        return currentServer;
    }

    @Override
    public void setCurrentServer(@Nullable Server currentServer) {
        this.currentServer = currentServer;
    }
}
