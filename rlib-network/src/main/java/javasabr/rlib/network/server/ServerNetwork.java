package javasabr.rlib.network.server;

import java.net.InetSocketAddress;
import java.util.function.Consumer;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.Network;
import reactor.core.publisher.Flux;

/**
 * The interface to implement a server network for accepting client connections.
 *
 * @param <C> the connection type
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface ServerNetwork<C extends Connection<C>> extends Network<C> {

  /**
   * Starts the server using any available address.
   *
   * @return the bound server address
   * @since 10.0.0
   */
  InetSocketAddress start();

  /**
   * Starts the server on the specified address.
   *
   * @param <S> the server network type
   * @param serverAddress the address to bind to
   * @return this server network
   * @since 10.0.0
   */
  <S extends ServerNetwork<C>> S start(InetSocketAddress serverAddress);

  /**
   * Registers a consumer to handle new connections.
   *
   * @param consumer the consumer to handle accepted connections
   * @since 10.0.0
   */
  void onAccept(Consumer<? super C> consumer);

  /**
   * Gets a reactive stream of new accepted connections.
   *
   * @return a flux of accepted connections
   * @since 10.0.0
   */
  Flux<? extends C> accepted();
}
