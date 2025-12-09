package javasabr.rlib.network;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import javasabr.rlib.network.packet.ReadableNetworkPacket;
import javasabr.rlib.network.packet.WritableNetworkPacket;
import reactor.core.publisher.Flux;

/**
 * The interface to implement an async connection.
 *
 * @author JavaSaBr
 */
public interface Connection<C extends Connection<C>> {

  record ReceivedPacketEvent<C, R>(C connection, R packet, boolean valid) {
    @Override
    public String toString() {
      return "[" + connection + '|' + packet + '|' + valid + ']';
    }
  }

  /**
   * Get a remote address of this connection.
   */
  String remoteAddress();

  /**
   * Get a timestamp of last write/read activity.
   */
  long lastActivity();

  /**
   * Close this connection if this connection is still opened.
   */
  void close();

  /**
   * @return true if this connection is already closed.
   */
  boolean closed();

  /**
   * Send a packet to connection's owner in background.
   */
  void sendInBackground(WritableNetworkPacket<C> packet);

  /**
   * Send a packet to connection's owner with async feedback of this action.
   *
   * @return the async result with true if the packet was sent or false if sending was failed.
   */
  CompletableFuture<Boolean> sendAsync(WritableNetworkPacket<C> packet);

  /**
   * Register a consumer to handle received valid packets.
   */
  void onReceiveValidPacket(BiConsumer<C, ? super ReadableNetworkPacket<C>> consumer);

  /**
   * Register a consumer to handle received invalid packets.
   */
  void onReceiveInvalidPacket(BiConsumer<C, ? super ReadableNetworkPacket<C>> consumer);

  /**
   * Get a stream of received packet events.
   */
  Flux<ReceivedPacketEvent<C, ? extends ReadableNetworkPacket<C>>> receivedEvents();

  /**
   * Get a stream of received packet events with expected packet type.
   */
  default <R extends ReadableNetworkPacket<C>> Flux<ReceivedPacketEvent<C, R>> receivedEvents(Class<R> packetType) {
    return receivedEvents()
        .filter(event -> packetType.isInstance(event.packet()))
        .map(event -> (ReceivedPacketEvent<C, R>) event);
  }

  /**
   * Get a stream of received valid packets.
   */
  Flux<? extends ReadableNetworkPacket<C>> receivedValidPackets();

  /**
   * Get a stream of received invalid packets.
   */
  Flux<? extends ReadableNetworkPacket<C>> receivedInvalidPackets();

  /**
   * Get a stream of received valid packets with expected type.
   */
  default <R extends ReadableNetworkPacket<C>> Flux<R> receivedValidPackets(Class<R> packetType) {
    return receivedValidPackets()
        .filter(packetType::isInstance)
        .map(networkPacket -> (R) networkPacket);
  }

  /**
   * Get a stream of received invalid packets with expected type.
   */
  default <R extends ReadableNetworkPacket<C>> Flux<R> receivedInvalidPackets(Class<R> packetType) {
    return receivedInvalidPackets()
        .filter(packetType::isInstance)
        .map(networkPacket -> (R) networkPacket);
  }
}
