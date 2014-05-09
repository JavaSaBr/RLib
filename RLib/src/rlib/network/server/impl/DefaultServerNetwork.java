package rlib.network.server.impl;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import rlib.concurrent.GroupThreadFactory;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
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

	protected static final Logger LOGGER = LoggerManager.getLogger(ServerNetwork.class);

	/** группа асинхронных каналов */
	private final AsynchronousChannelGroup group;
	/** асинронный серверый канал */
	private final AsynchronousServerSocketChannel channel;
	/** обработчик новых подключений */
	private final AcceptHandler acceptHandler;

	public DefaultServerNetwork(final NetworkConfig config, final AcceptHandler acceptHandler) throws IOException {
		super(config);
		this.group = AsynchronousChannelGroup.withFixedThreadPool(config.getGroupSize(), new GroupThreadFactory(config.getGroupName(), config.getThreadClass(), config.getThreadPriority()));
		this.channel = AsynchronousServerSocketChannel.open(group);
		this.acceptHandler = acceptHandler;
	}

	@Override
	public <A> void accept(final A attachment, final CompletionHandler<AsynchronousSocketChannel, ? super A> handler) {
		channel.accept(attachment, handler);
	}

	@Override
	public void bind(final SocketAddress address) throws IOException {
		channel.bind(address);
		channel.accept(channel, acceptHandler);
	}

	@Override
	public String toString() {
		return "DefaultServerNetwork [group=" + group + ", channel=" + channel + ", acceptHandler=" + acceptHandler + ", config=" + config + "]";
	}
}
