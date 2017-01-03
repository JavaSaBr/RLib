package rlib.network.client.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;

import rlib.concurrent.GroupThreadFactory;
import rlib.network.NetworkConfig;
import rlib.network.client.ClientNetwork;
import rlib.network.client.ConnectHandler;
import rlib.network.impl.AbstractAsynchronousNetwork;

/**
 * Базовая модель асинхронной клиентской сети.
 *
 * @author JavaSaBr
 */
public final class DefaultClientNetwork extends AbstractAsynchronousNetwork implements ClientNetwork {

    /**
     * Группа асинхронных каналов.
     */
    private final AsynchronousChannelGroup group;

    /**
     * Обработчик подключения к серверу.
     */
    private final ConnectHandler connectHandler;

    /**
     * Асинхронный клиентский канал.
     */
    private AsynchronousSocketChannel channel;

    public DefaultClientNetwork(final NetworkConfig config, final ConnectHandler connectHandler) throws IOException {
        super(config);

        this.group = AsynchronousChannelGroup.withFixedThreadPool(config.getGroupSize(), new GroupThreadFactory(config.getGroupName(), config.getThreadClass(), config.getThreadPriority()));
        this.connectHandler = connectHandler;
    }

    @Override
    public void connect(final InetSocketAddress serverAddress) {

        try {

            if (channel != null) {
                channel.close();
            }

            channel = AsynchronousSocketChannel.open(group);

        } catch (final IOException e) {
            LOGGER.warning(this, e);
        }

        channel.connect(serverAddress, channel, connectHandler);
    }
}
