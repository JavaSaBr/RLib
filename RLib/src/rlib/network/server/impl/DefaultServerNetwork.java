package rlib.network.server.impl;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import rlib.concurrent.GroupThreadFactory;
import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.network.NetworkConfig;
import rlib.network.impl.AbstractAsynchronousNetwork;
import rlib.network.server.AcceptHandler;
import rlib.network.server.ServerNetwork;

/**
 * Базовая модель асинхронной сети.
 * 
 * @author Ronn
 */
public final class DefaultServerNetwork extends AbstractAsynchronousNetwork implements ServerNetwork {

	protected static final Logger LOGGER = Loggers.getLogger(ServerNetwork.class);

	/** группа асинхронных каналов */
	private final AsynchronousChannelGroup group;
	/** асинронный серверый канал */
	private final AsynchronousServerSocketChannel channel;
	/** обработчик новых подключений */
	private final AcceptHandler acceptHandler;

	public DefaultServerNetwork(NetworkConfig config, AcceptHandler acceptHandler) throws IOException {
		super(config);
		this.group = AsynchronousChannelGroup.withFixedThreadPool(config.getGroupSize(), new GroupThreadFactory(config.getGroupName(), config.getThreadClass(), config.getThreadPriority()));
		this.channel = AsynchronousServerSocketChannel.open(group);
		this.acceptHandler = acceptHandler;
	}

	@Override
	public <A> void accept(A attachment, CompletionHandler<AsynchronousSocketChannel, ? super A> handler) {
		channel.accept(attachment, handler);
	}

	@Override
	public void bind(SocketAddress address) throws IOException {
		channel.bind(address);
		channel.accept(channel, acceptHandler);
	}
}
