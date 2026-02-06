package javasabr.rlib.network.packet;

import javasabr.rlib.network.Connection;
import javasabr.rlib.reusable.Reusable;
import javasabr.rlib.reusable.pool.Pool;

/**
 * Interface for reusable writable packets that can be pooled.
 *
 * @param <C> the connection type
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface ReusableWritablePacket<C extends Connection<C>> extends WritableNetworkPacket<C>, Reusable {

  /**
   * Handles completion of packet sending.
   *
   * @since 10.0.0
   */
  void complete();

  /**
   * Forces completion of this packet.
   *
   * @since 10.0.0
   */
  void forceComplete();

  /**
   * Decreases the sending count by one.
   *
   * @since 10.0.0
   */
  void decreaseSends();

  /**
   * Decreases the sending count by the specified amount.
   *
   * @param count the count to decrease by
   * @since 10.0.0
   */
  void decreaseSends(int count);

  /**
   * Increases the sending count by one.
   *
   * @since 10.0.0
   */
  void increaseSends();

  /**
   * Increases the sending count by the specified amount.
   *
   * @param count the count to increase by
   * @since 10.0.0
   */
  void increaseSends(int count);

  /**
   * Sets the pool to store this packet when done.
   *
   * @param pool the pool to store used packets
   * @since 10.0.0
   */
  void setPool(Pool<ReusableWritablePacket<C>> pool);

  /**
   * Called when this packet is added to the send queue.
   *
   * @since 10.0.0
   */
  default void notifyAddedToSend() {
    increaseSends();
  }
}
