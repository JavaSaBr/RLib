package com.ss.rlib.network.client.impl;

import com.ss.rlib.concurrent.GroupThreadFactory;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.client.ConnectHandler;
import com.ss.rlib.network.client.server.Server;
import com.ss.rlib.network.impl.AbstractAsyncNetwork;
import com.ss.rlib.network.packet.ReadablePacketRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
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

    public DefaultClientNetwork(@NotNull final NetworkConfig config,
                                @NotNull final ReadablePacketRegistry packetRegistry,
                                @NotNull final ConnectHandler connectHandler) throws IOException {
        super(config, packetRegistry);

        this.group = AsynchronousChannelGroup.withFixedThreadPool(config.getGroupSize(),
                new GroupThreadFactory(config.getGroupName(), config.getThreadClass(), config.getThreadPriority()));

        this.connectHandler = connectHandler;
    }

    /**
     * Prepare the channel to connect.
     */
    protected @NotNull AsynchronousSocketChannel prepareChannel() {
        try {
            if (channel != null) channel.close();
            channel = AsynchronousSocketChannel.open(group);
            return channel;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void asyncConnect(@NotNull final InetSocketAddress serverAddress) {
        prepareChannel().connect(serverAddress, this, connectHandler);
    }

    @Override
    public synchronized Server connect(@NotNull final InetSocketAddress serverAddress) {
        final Future<Void> future = prepareChannel().connect(serverAddress);
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

        final Server currentServer = getCurrentServer();
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
    public void setCurrentServer(@Nullable final Server currentServer) {
        this.currentServer = currentServer;
    }
}
