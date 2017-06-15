package com.ss.rlib.network.client.impl;

import com.ss.rlib.network.client.ClientNetwork;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;

import com.ss.rlib.concurrent.GroupThreadFactory;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.client.ConnectHandler;
import com.ss.rlib.network.impl.AbstractAsynchronousNetwork;

/**
 * The base implementation of a async client network.
 *
 * @author JavaSaBr
 */
public final class DefaultClientNetwork extends AbstractAsynchronousNetwork implements ClientNetwork {

    /**
     * The asynchronous channel group.
     */
    @NotNull
    private final AsynchronousChannelGroup group;

    /**
     * The connection handler.
     */
    @NotNull
    private final ConnectHandler connectHandler;

    /**
     * The asynchronous channel.
     */
    @Nullable
    private AsynchronousSocketChannel channel;

    /**
     * Instantiates a new Default client network.
     *
     * @param config         the config
     * @param connectHandler the connect handler
     * @throws IOException the io exception
     */
    public DefaultClientNetwork(@NotNull final NetworkConfig config, @NotNull final ConnectHandler connectHandler)
            throws IOException {
        super(config);

        this.group = AsynchronousChannelGroup.withFixedThreadPool(config.getGroupSize(),
                new GroupThreadFactory(config.getGroupName(), config.getThreadClass(), config.getThreadPriority()));

        this.connectHandler = connectHandler;
    }

    @Override
    public void connect(@NotNull final InetSocketAddress serverAddress) {

        try {
            if (channel != null) channel.close();
            channel = AsynchronousSocketChannel.open(group);
        } catch (final IOException e) {
            LOGGER.warning(this, e);
        }

        channel.connect(serverAddress, channel, connectHandler);
    }
}
