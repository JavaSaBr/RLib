package com.ss.rlib.network.impl;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.NetworkConfig;
import org.jetbrains.annotations.NotNull;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.function.BiFunction;

/**
 * The base implementation of {@link Network}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractNetwork<C extends Connection<?, ?>> implements Network<C> {

    protected static final Logger LOGGER = LoggerManager.getLogger(AbstractNetwork.class);

    protected final @NotNull NetworkConfig config;
    protected final @NotNull BiFunction<Network<C>, AsynchronousSocketChannel, C> channelToConnection;

    protected AbstractNetwork(
        @NotNull NetworkConfig config,
        @NotNull BiFunction<Network<C>, AsynchronousSocketChannel, C> channelToConnection
    ) {
        this.config = config;
        this.channelToConnection = channelToConnection;
    }
}
