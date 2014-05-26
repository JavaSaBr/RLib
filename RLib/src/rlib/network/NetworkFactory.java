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
 * Фабрика реализаций сетей.
 * 
 * @author Ronn
 */
public final class NetworkFactory {

	/**
	 * Создание модели клиентской сети.
	 * 
	 * @param config конфигурация сети.
	 * @param connectHandler обработчик подключения к серверу.
	 * @return ссылка на новую модель.
	 */
	public static ClientNetwork newDefaultAsynchronousClientNetwork(final NetworkConfig config, final ConnectHandler connectHandler) {

		try {
			return new DefaultClientNetwork(config, connectHandler);
		} catch(final IOException e) {
			LOGGER.warning(e);
		}

		return null;
	}

	/**
	 * Создание модели серверной сети..
	 * 
	 * @param config конфигурация сети.
	 * @param acceptHandler обработчик новых подключений.
	 * @return ссылка на новую модель.
	 */
	public static ServerNetwork newDefaultAsynchronousServerNetwork(final NetworkConfig config, final AcceptHandler acceptHandler) {

		try {
			return new DefaultServerNetwork(config, acceptHandler);
		} catch(final IOException e) {
			LOGGER.warning(e);
		}

		return null;
	}

	private static final Logger LOGGER = LoggerManager.getLogger(NetworkFactory.class);

	private NetworkFactory() throws Exception {
		throw new Exception("КУДА ТЫ ЛЕЗЕШЬ");
	}
}
