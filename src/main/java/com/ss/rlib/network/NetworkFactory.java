package com.ss.rlib.network;

import com.ss.rlib.logging.Logger;
import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.client.ConnectHandler;
import com.ss.rlib.network.client.impl.DefaultClientNetwork;
import com.ss.rlib.network.server.AcceptHandler;
import com.ss.rlib.network.server.ServerNetwork;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.ss.rlib.logging.LoggerManager;
import com.ss.rlib.network.server.impl.DefaultServerNetwork;

import java.io.IOException;

/**
 * The network factory.
 *
 * @author JavaSaBr
 */
public final class NetworkFactory {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(NetworkFactory.class);

    /**
     * Create a default asynchronous client network.
     *
     * @param config         the network config.
     * @param connectHandler the connect handler.
     * @return the client network or null.
     */
    @Nullable
    public static ClientNetwork newDefaultAsynchronousClientNetwork(@NotNull final NetworkConfig config,
                                                                    @NotNull final ConnectHandler connectHandler) {
        try {
            return new DefaultClientNetwork(config, connectHandler);
        } catch (final IOException e) {
            LOGGER.warning(e);
        }

        return null;
    }

    /**
     * Create a default asynchronous server network.
     *
     * @param config        the network config.
     * @param acceptHandler the accept handler.
     * @return the client network or null.
     */
    @Nullable
    public static ServerNetwork newDefaultAsynchronousServerNetwork(@NotNull final NetworkConfig config,
                                                                    @NotNull final AcceptHandler acceptHandler) {
        try {
            return new DefaultServerNetwork(config, acceptHandler);
        } catch (final IOException e) {
            LOGGER.warning(e);
        }

        return null;
    }

    private NetworkFactory() throws Exception {
        throw new Exception("no permission");
    }
}
