package javasabr.rlib.network.client;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.Network;
import org.jspecify.annotations.Nullable;
import reactor.core.publisher.Mono;

/**
 * Interface to implement a client network for connecting to servers.
 *
 * @param <C> the connection type
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface ClientNetwork<C extends Connection<C>> extends Network<C> {

  /**
   * Connects to a server synchronously by the address.
   *
   * @param serverAddress the server address
   * @return the established connection
   * @since 10.0.0
   */
  C connect(InetSocketAddress serverAddress);

  /**
   * Connects to a server asynchronously by the address.
   *
   * @param serverAddress the server address
   * @return a future containing the established connection
   * @since 10.0.0
   */
  CompletableFuture<C> connectAsync(InetSocketAddress serverAddress);

  /**
   * Connects to a server reactively by the address.
   *
   * @param serverAddress the server address
   * @return a mono containing the established connection
   * @since 10.0.0
   */
  Mono<C> connectReactive(InetSocketAddress serverAddress);

  /**
   * Gets the current connection to a server.
   *
   * @return the current connection or null if not connected
   * @since 10.0.0
   */
  @Nullable
  C currentConnection();

  /**
   * Gets the current connection to a server as an optional.
   *
   * @return an optional containing the current connection
   * @since 10.0.0
   */
  Optional<C> currentConnectionOptional();
}
