package javasabr.rlib.network.client;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.Network;
import org.jspecify.annotations.Nullable;
import reactor.core.publisher.Mono;

/**
 * Interface to implement a client network.
 *
 * @author JavaSaBr
 */
public interface ClientNetwork<C extends Connection<?, ?>> extends Network<C> {

  /**
   * Connect to a server by the address.
   *
   * @param serverAddress the sever address.
   * @return the future with result connection.
   */
  CompletableFuture<C> connect(InetSocketAddress serverAddress);

  /**
   * Connect to a server by the address.
   *
   * @param serverAddress the sever address.
   * @return the future with result connection.
   */
  Mono<C> connected(InetSocketAddress serverAddress);

  /**
   * Get a current connection to a server or null.
   *
   * @return the current connection or null.
   */
  @Nullable
  C getCurrentConnection();
}
