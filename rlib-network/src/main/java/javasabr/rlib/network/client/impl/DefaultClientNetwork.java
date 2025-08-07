package javasabr.rlib.network.client.impl;

import static javasabr.rlib.common.util.Utils.unchecked;
import static javasabr.rlib.common.util.Utils.uncheckedGet;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import javasabr.rlib.common.concurrent.util.ThreadUtils;
import javasabr.rlib.common.util.AsyncUtils;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.Network;
import javasabr.rlib.network.NetworkConfig;
import javasabr.rlib.network.client.ClientNetwork;
import javasabr.rlib.network.impl.AbstractNetwork;
import javasabr.rlib.network.util.NetworkUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

/**
 * The default implementation of a client network.
 *
 * @author JavaSaBr
 */
public class DefaultClientNetwork<C extends Connection<?, ?>> extends AbstractNetwork<C> implements ClientNetwork<C> {

  protected static final Logger LOGGER = LoggerManager.getLogger(DefaultClientNetwork.class);

  protected final @NotNull AtomicBoolean connecting;

  protected volatile @Nullable CompletableFuture<C> pendingConnection;
  protected volatile @Getter
  @Nullable C currentConnection;

  public DefaultClientNetwork(
      @NotNull NetworkConfig config,
      @NotNull BiFunction<Network<C>, AsynchronousSocketChannel, C> channelToConnection) {
    super(config, channelToConnection);
    this.connecting = new AtomicBoolean(false);

    LOGGER.info(
        config,
        conf -> "Client network configuration: {\n" + "  groupName: \"" + conf.getThreadGroupName() + "\",\n"
            + "  readBufferSize: " + conf.getReadBufferSize() + ",\n" + "  pendingBufferSize: "
            + conf.getPendingBufferSize() + ",\n" + "  writeBufferSize: " + conf.getWriteBufferSize() + "\n" + "}");
  }

  @Override
  public @NotNull CompletableFuture<C> connect(@NotNull InetSocketAddress serverAddress) {

    C currentConnection = getCurrentConnection();

    if (currentConnection != null) {
      unchecked(currentConnection, C::close);
    }

    // if we are trying connection now
    if (!connecting.compareAndSet(false, true)) {

      var asyncResult = this.pendingConnection;

      if (asyncResult != null) {
        return asyncResult;
      }

      ThreadUtils.sleep(100);

      return connect(serverAddress);
    }

    var asyncResult = new CompletableFuture<C>();

    var channel = uncheckedGet(AsynchronousSocketChannel::open);
    channel.connect(
        serverAddress, null, new CompletionHandler<Void, Void>() {

          @Override
          public void completed(@Nullable Void result, @Nullable Void attachment) {
            LOGGER.info(channel, ch -> "Connected to server: " + NetworkUtils.getRemoteAddress(ch));
            asyncResult.complete(channelToConnection.apply(DefaultClientNetwork.this, channel));
          }

          @Override
          public void failed(@NotNull Throwable exc, @Nullable Void attachment) {
            asyncResult.completeExceptionally(exc);
          }
        });

    pendingConnection = asyncResult;

    return asyncResult.handle((connection, throwable) -> {
      this.currentConnection = connection;
      this.connecting.set(false);
      return AsyncUtils.continueCompletableStage(connection, throwable);
    });
  }

  @Override
  public @NotNull Mono<C> connected(@NotNull InetSocketAddress serverAddress) {
    return Mono.create(monoSink -> connect(serverAddress).whenComplete((connection, ex) -> {
      if (ex != null) {
        monoSink.error(ex);
      } else {
        monoSink.success(connection);
      }
    }));
  }

  @Override
  public void shutdown() {
    Optional
        .ofNullable(getCurrentConnection())
        .ifPresent(connection -> unchecked(connection, C::close));
  }
}
