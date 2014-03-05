package rlib.network.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;

import rlib.concurrent.GroupThreadFactory;
import rlib.network.AbstractAsynchronousNetwork;
import rlib.network.NetworkConfig;

/**
 * Базовая модель асинхронной сети.
 *
 * @author Ronn
 */
public final class DefaultClientNetwork extends AbstractAsynchronousNetwork implements ClientNetwork {

	/** группа асинхронных каналов */
	private final AsynchronousChannelGroup group;
	/** обработчик подключения к серверу */
	private final ConnectHandler connectHandler;

	/** асинронный клиентский канал */
	private AsynchronousSocketChannel channel;

	public DefaultClientNetwork(NetworkConfig config, ConnectHandler connectHandler) throws IOException {
		super(config);

		this.group = AsynchronousChannelGroup.withFixedThreadPool(config.getGroupSize(), new GroupThreadFactory(config.getGroupName(), config.getThreadClass(), config.getThreadPriority()));
		this.connectHandler = connectHandler;
	}

	@Override
	public void connect(InetSocketAddress serverAddress) {

		try {

			if(channel != null) {
				channel.close();
			}

			channel = AsynchronousSocketChannel.open(group);

		} catch(IOException e) {
			LOGGER.warning(this, e);
		}

		channel.connect(serverAddress, channel, connectHandler);
	}
}
