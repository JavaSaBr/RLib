package rlib.network;

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
 * Фабрика для создания серверной и клиетнской сети.
 *
 * @author Ronn
 */
public final class NetworkFactory {

    private static final Logger LOGGER = LoggerManager.getLogger(NetworkFactory.class);

    /**
     * Создание стандартной реализации клиентской асинхронной сети.
     *
     * @param config         конфигурация сети.
     * @param connectHandler обработчик подключения к серверу.
     * @return клиентская сеть либо <code>null</code> если произошла ошибка.
     */
    public static ClientNetwork newDefaultAsynchronousClientNetwork(final NetworkConfig config, final ConnectHandler connectHandler) {

        try {
            return new DefaultClientNetwork(config, connectHandler);
        } catch (final IOException e) {
            LOGGER.warning(e);
        }

        return null;
    }

    /**
     * Создание стандартной реализации серверной асинхронной серверной сети.
     *
     * @param config        конфигурация сети.
     * @param acceptHandler обработчик новых подключений.
     * @return серверная сеть либо <code>null</code> если произошла ошибка.
     */
    public static ServerNetwork newDefaultAsynchronousServerNetwork(final NetworkConfig config, final AcceptHandler acceptHandler) {

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
