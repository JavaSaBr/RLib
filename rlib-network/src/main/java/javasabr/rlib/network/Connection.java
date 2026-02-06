package javasabr.rlib.network;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import javasabr.rlib.network.packet.ReadableNetworkPacket;
import javasabr.rlib.network.packet.WritableNetworkPacket;
import reactor.core.publisher.Flux;

/**
 * The interface to implement an asynchronous network connection.
 *
 * @param <C> the connection type
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface Connection<C extends Connection<C>> {

  /**
   * Represents a received packet event containing the connection, packet, and validity status.
   *
   * @param <C> the connection type
   * @param <R> the packet type
   * @param connection the connection that received the packet
   * @param packet the received packet
   * @param valid whether the packet is valid
   * @since 10.0.0
   */
  record ReceivedPacketEvent<C, R>(C connection, R packet, boolean valid) {
    @Override
    public String toString() {
      return "[" + connection + '|' + packet + '|' + valid + ']';
    }
  }

  /**
   * Gets the remote address of this connection.
   *
   * @return the remote address as a string
   * @since 10.0.0
   */
  String remoteAddress();

  /**
   * Gets the timestamp of last write/read activity.
   *
   * @return the last activity timestamp in milliseconds
   * @since 10.0.0
   */
  long lastActivity();

  /**
   * Closes this connection if it is still open.
   *
   * @since 10.0.0
   */
  void close();

  /**
   * Checks if this connection is already closed.
   *
   * @return true if this connection is closed
   * @since 10.0.0
   */
  boolean closed();

  /**
   * Sends a packet to the connection's owner in the background.
   *
   * @param packet the packet to send
   * @since 10.0.0
   */
  void sendInBackground(WritableNetworkPacket<C> packet);

  /**
   * Sends a packet to the connection's owner with async feedback.
   *
   * @param packet the packet to send
   * @return a future with true if sent successfully, false otherwise
   * @since 10.0.0
   */
  CompletableFuture<Boolean> sendAsync(WritableNetworkPacket<C> packet);

  /**
   * Registers a consumer to handle received valid packets.
   *
   * @param consumer the consumer to handle valid packets
   * @since 10.0.0
   */
  void onReceiveValidPacket(BiConsumer<C, ? super ReadableNetworkPacket<C>> consumer);

  /**
   * Registers a consumer to handle received invalid packets.
   *
   * @param consumer the consumer to handle invalid packets
   * @since 10.0.0
   */
  void onReceiveInvalidPacket(BiConsumer<C, ? super ReadableNetworkPacket<C>> consumer);

  /**
   * Gets a stream of received packet events.
   *
   * @return a reactive stream of packet events
   * @since 10.0.0
   */
  Flux<ReceivedPacketEvent<C, ? extends ReadableNetworkPacket<C>>> receivedEvents();

  /**
   * Gets a stream of received packet events filtered by expected packet type.
   *
   * @param <R> the expected packet type
   * @param packetType the packet type class
   * @return a reactive stream of packet events
   * @since 10.0.0
   */
  default <R extends ReadableNetworkPacket<C>> Flux<ReceivedPacketEvent<C, R>> receivedEvents(Class<R> packetType) {
    return receivedEvents()
        .filter(event -> packetType.isInstance(event.packet()))
        .map(event -> (ReceivedPacketEvent<C, R>) event);
  }

  /**
   * Gets a stream of received valid packets.
   *
   * @return a reactive stream of valid packets
   * @since 10.0.0
   */
  Flux<? extends ReadableNetworkPacket<C>> receivedValidPackets();

  /**
   * Gets a stream of received invalid packets.
   *
   * @return a reactive stream of invalid packets
   * @since 10.0.0
   */
  Flux<? extends ReadableNetworkPacket<C>> receivedInvalidPackets();

  /**
   * Gets a stream of received valid packets filtered by expected type.
   *
   * @param <R> the expected packet type
   * @param packetType the packet type class
   * @return a reactive stream of valid packets
   * @since 10.0.0
   */
  default <R extends ReadableNetworkPacket<C>> Flux<R> receivedValidPackets(Class<R> packetType) {
    return receivedValidPackets()
        .filter(packetType::isInstance)
        .map(networkPacket -> (R) networkPacket);
  }

  /**
   * Gets a stream of received invalid packets filtered by expected type.
   *
   * @param <R> the expected packet type
   * @param packetType the packet type class
   * @return a reactive stream of invalid packets
   * @since 10.0.0
   */
  default <R extends ReadableNetworkPacket<C>> Flux<R> receivedInvalidPackets(Class<R> packetType) {
    return receivedInvalidPackets()
        .filter(packetType::isInstance)
        .map(networkPacket -> (R) networkPacket);
  }
}
