package com.ss.rlib.network;

import com.ss.rlib.network.client.ClientNetwork;
import com.ss.rlib.network.client.ConnectHandler;
import com.ss.rlib.network.client.impl.DefaultClientNetwork;
import com.ss.rlib.network.packet.ReadablePacketRegistry;
import com.ss.rlib.network.server.AcceptHandler;
import com.ss.rlib.network.server.ServerNetwork;
import com.ss.rlib.network.server.impl.DefaultServerNetwork;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * The network factory.
 *
 * @author JavaSaBr
 */
public final class NetworkFactory {

    /**
     * Create a default asynchronous client network.
     *
     * @param registry the readable packet registry.
     * @return the client network.
     */
    public static @NotNull ClientNetwork newDefaultAsyncClientNetwork(@NotNull final ReadablePacketRegistry registry) {
        return newDefaultAsyncClientNetwork(NetworkConfig.DEFAULT_CLIENT, registry, ConnectHandler.newDefault());
    }

    /**
     * Create a default asynchronous client network.
     *
     * @param config         the network config.
     * @param registry       the readable packet registry.
     * @param connectHandler the connect handler.
     * @return the client network.
     */
    public static @NotNull ClientNetwork newDefaultAsyncClientNetwork(@NotNull final NetworkConfig config,
                                                                      @NotNull final ReadablePacketRegistry registry,
                                                                      @NotNull final ConnectHandler connectHandler) {
        try {
            return new DefaultClientNetwork(config, registry, connectHandler);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a default asynchronous server network.
     *
     * @param registry the readable packet registry.
     * @return the client network.
     */
    public static @NotNull ServerNetwork newDefaultAsyncServerNetwork(@NotNull final ReadablePacketRegistry registry) {
        return newDefaultAsyncServerNetwork(NetworkConfig.DEFAULT_SERVER, registry, AcceptHandler.newDefault());
    }

    /**
     * Create a default asynchronous server network.
     *
     * @param config        the network config.
     * @param registry      the readable packet registry.
     * @param acceptHandler the accept handler.
     * @return the client network.
     */
    public static @NotNull ServerNetwork newDefaultAsyncServerNetwork(@NotNull final NetworkConfig config,
                                                                      @NotNull final ReadablePacketRegistry registry,
                                                                      @NotNull final AcceptHandler acceptHandler) {
        try {
            return new DefaultServerNetwork(config, registry, acceptHandler);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private NetworkFactory() throws Exception {
        throw new Exception("no permission");
    }
}
