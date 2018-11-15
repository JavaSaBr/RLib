package com.ss.rlib.network.client.impl;

import com.ss.rlib.common.concurrent.GroupThreadFactory;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.client.ConnectHandler;
import com.ss.rlib.network.client.server.Server;
import com.ss.rlib.network.impl.AbstractAsyncNetwork;
import com.ss.rlib.network.packet.ReadablePacketRegistry;
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
    protected volatile AsynchronousSocketChannel channel;

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

    public DefaultClientNetwork(
        @NotNull NetworkConfig config,
        @NotNull AsynchronousChannelGroup channelGroup,
        @NotNull ReadablePacketRegistry packetRegistry,
        @NotNull ConnectHandler connectHandler
    ) throws IOException {

        super(config, packetRegistry);

        this.group = channelGroup;
        this.connectHandler = connectHandler;
    }

    /**
     * Prepare the channel to connect.
     *
     * @return the prepared socket channel.
     */
    protected @NotNull AsynchronousSocketChannel prepareChannel() {
        try {

            var currentChannel = getChannel();

            if (currentChannel != null) {
                currentChannel.close();
            }

            currentChannel = AsynchronousSocketChannel.open(group);

            this.channel = currentChannel;

            return currentChannel;

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public synchronized void asyncConnect(@NotNull InetSocketAddress serverAddress) {
        prepareChannel().connect(serverAddress, this, connectHandler);
    }

    @Override
    public synchronized @NotNull Server connect(@NotNull InetSocketAddress serverAddress) {

        Future<Void> future = prepareChannel().connect(serverAddress);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        connectHandler.completed(null, this);

        return ObjectUtils.notNull(getCurrentServer());
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
