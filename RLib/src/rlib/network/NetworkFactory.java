package rlib.network;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.client.ClientNetwork;
import rlib.network.client.ConnectHandler;
import rlib.network.client.impl.DefaultClientNetwork;
import rlib.network.server.AcceptHandler;
import rlib.network.server.ServerNetwork;
import rlib.network.server.impl.DefaultServerNetwork;

/**
 * The network factory.
 *
 * @author JavaSaBr
 */
public final class NetworkFactory {

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
