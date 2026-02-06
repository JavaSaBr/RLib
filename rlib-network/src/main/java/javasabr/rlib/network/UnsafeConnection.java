package javasabr.rlib.network;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * An unsafe connection interface providing direct access to internal network components.
 * Use with caution as modifications may affect connection stability.
 *
 * @param <C> the connection type
 * @since 10.0.0
 */
public interface UnsafeConnection<C extends UnsafeConnection<C>> extends Connection<C> {

  /**
   * Gets the network this connection belongs to.
   *
   * @return the network
   * @since 10.0.0
   */
  Network<?> network();

  /**
   * Gets the buffer allocator used by this connection.
   *
   * @return the buffer allocator
   * @since 10.0.0
   */
  BufferAllocator bufferAllocator();

  /**
   * Gets the underlying asynchronous socket channel.
   *
   * @return the socket channel
   * @since 10.0.0
   */
  AsynchronousSocketChannel channel();

  /**
   * Called when the connection is established.
   *
   * @since 10.0.0
   */
  void onConnected();
}
