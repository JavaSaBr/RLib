package rlib.network.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;

import rlib.concurrent.GroupThreadFactory;
import rlib.network.AbstractAsynchronousNetwork;
import rlib.network.NetworkConfig;
import rlib.network.client.ConnectHandler;


/**
 * Базовая модель асинхронной сети.
 *
 * @author Ronn
 */
public final class DefaultClientNetwork extends AbstractAsynchronousNetwork implements ClientNetwork
{
	/** группа асинхронных каналов */
	private final AsynchronousChannelGroup channelGroup;

	/** обработчик подключения к серверу */
	private final ConnectHandler connectHandler;
	
	/** асинронный клиентский канал */
	private AsynchronousSocketChannel clientChannel;
	
	public DefaultClientNetwork(NetworkConfig config, ConnectHandler connectHandler) throws IOException
	{
		super(config);
		
		// создаем группу каналов
		this.channelGroup = AsynchronousChannelGroup.withFixedThreadPool(config.getGroupSize(), new GroupThreadFactory(config.getGroupName(), config.getThreadClass(), config.getThreadPriority()));
		// запоминаем менеджера по принятию конектов
		this.connectHandler = connectHandler;
	}

	@Override
	public void connect(InetSocketAddress serverAddress)
	{
		try
		{
			// если еще весит прошлый конект
			if(clientChannel != null)
				// закрываем его
				clientChannel.close();
			
			// создаем новый канал
			clientChannel = AsynchronousSocketChannel.open(channelGroup);
		}
		catch(IOException e)
		{
			log.warning(this, e);
		}

		// производим попытку подключения к серверу
		clientChannel.connect(serverAddress, clientChannel, connectHandler);
	}
}
