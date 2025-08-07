package javasabr.rlib.network.impl;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.function.BiFunction;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.Network;
import javasabr.rlib.network.NetworkConfig;
import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of {@link Network}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractNetwork<C extends Connection<?, ?>> implements Network<C> {

  protected static final Logger LOGGER = LoggerManager.getLogger(AbstractNetwork.class);

  protected final @NotNull NetworkConfig config;
  protected final @NotNull BiFunction<Network<C>, AsynchronousSocketChannel, C> channelToConnection;

  protected AbstractNetwork(
      @NotNull NetworkConfig config,
      @NotNull BiFunction<Network<C>, AsynchronousSocketChannel, C> channelToConnection) {
    this.config = config;
    this.channelToConnection = channelToConnection;
  }
}
