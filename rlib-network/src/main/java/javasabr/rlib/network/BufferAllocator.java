package javasabr.rlib.network;

import java.nio.ByteBuffer;

/**
 * The interface to implement a buffer allocator for network operations.
 *
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface BufferAllocator {

  /**
   * Gets a new read buffer to use for receiving data.
   *
   * @return a read buffer
   * @since 10.0.0
   */
  ByteBuffer takeReadBuffer();

  /**
   * Gets a new pending buffer to use for storing partial packet data.
   *
   * @return a pending buffer
   * @since 10.0.0
   */
  ByteBuffer takePendingBuffer();

  /**
   * Gets a new write buffer to use for sending data.
   *
   * @return a write buffer
   * @since 10.0.0
   */
  ByteBuffer takeWriteBuffer();

  /**
   * Gets a new buffer with the requested capacity.
   *
   * @param bufferSize the requested buffer size
   * @return a buffer with the specified capacity
   * @since 10.0.0
   */
  ByteBuffer takeBuffer(int bufferSize);

  /**
   * Stores an already used read buffer for reuse.
   *
   * @param buffer the read buffer to store
   * @return this allocator for method chaining
   * @since 10.0.0
   */
  BufferAllocator putReadBuffer(ByteBuffer buffer);

  /**
   * Stores an already used pending buffer for reuse.
   *
   * @param buffer the pending buffer to store
   * @return this allocator for method chaining
   * @since 10.0.0
   */
  BufferAllocator putPendingBuffer(ByteBuffer buffer);

  /**
   * Stores an already used write buffer for reuse.
   *
   * @param buffer the write buffer to store
   * @return this allocator for method chaining
   * @since 10.0.0
   */
  BufferAllocator putWriteBuffer(ByteBuffer buffer);

  /**
   * Stores an already used byte buffer for reuse.
   *
   * @param buffer the buffer to store
   * @return this allocator for method chaining
   * @since 10.0.0
   */
  BufferAllocator putBuffer(ByteBuffer buffer);
}
