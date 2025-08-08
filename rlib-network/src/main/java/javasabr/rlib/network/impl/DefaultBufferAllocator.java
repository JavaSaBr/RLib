package javasabr.rlib.network.impl;

import java.nio.ByteBuffer;
import javasabr.rlib.common.util.pools.Pool;
import javasabr.rlib.common.util.pools.PoolFactory;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.NetworkConfig;
import lombok.ToString;

/**
 * The default byte buffer allocator.
 *
 * @author JavaSaBr
 */
@ToString
public class DefaultBufferAllocator implements BufferAllocator {

  private static final Logger LOGGER = LoggerManager.getLogger(DefaultBufferAllocator.class);

  protected final Pool<ByteBuffer> readBufferPool;
  protected final Pool<ByteBuffer> pendingBufferPool;
  protected final Pool<ByteBuffer> writeBufferPool;

  protected final NetworkConfig config;

  public DefaultBufferAllocator(NetworkConfig config) {
    this.config = config;
    this.readBufferPool = PoolFactory.newConcurrentStampedLockPool(ByteBuffer.class);
    this.pendingBufferPool = PoolFactory.newConcurrentStampedLockPool(ByteBuffer.class);
    this.writeBufferPool = PoolFactory.newConcurrentStampedLockPool(ByteBuffer.class);
  }

  @Override
  public ByteBuffer takeReadBuffer() {
    var bufferSize = config.getReadBufferSize();
    LOGGER.debug(bufferSize, size -> "Allocate a new read buffer with size: " + size);
    return config.isDirectByteBuffer()
           ? ByteBuffer.allocateDirect(bufferSize)
           : ByteBuffer
               .allocate(bufferSize)
               .order(config.getByteOrder())
               .clear();
  }

  @Override
  public ByteBuffer takePendingBuffer() {
    var bufferSize = config.getPendingBufferSize();
    LOGGER.debug(bufferSize, size -> "Allocate a new pending buffer with size: " + size);
    return config.isDirectByteBuffer()
           ? ByteBuffer.allocateDirect(bufferSize)
           : ByteBuffer
               .allocate(bufferSize)
               .order(config.getByteOrder())
               .clear();
  }

  @Override
  public ByteBuffer takeWriteBuffer() {
    var bufferSize = config.getWriteBufferSize();
    LOGGER.debug(bufferSize, size -> "Allocate a new write buffer with size: " + size);
    return config.isDirectByteBuffer()
           ? ByteBuffer.allocateDirect(bufferSize)
           : ByteBuffer
               .allocate(bufferSize)
               .order(config.getByteOrder())
               .clear();
  }

  @Override
  public ByteBuffer takeBuffer(int bufferSize) {
    LOGGER.debug(bufferSize, size -> "Allocate a new buffer with size: " + size);
    return config.isDirectByteBuffer()
           ? ByteBuffer.allocateDirect(bufferSize)
           : ByteBuffer
               .allocate(bufferSize)
               .order(config.getByteOrder())
               .clear();
  }

  @Override
  public DefaultBufferAllocator putReadBuffer(ByteBuffer buffer) {
    LOGGER.debug("Skip storing a read buffer");
    return this;
  }

  @Override
  public DefaultBufferAllocator putPendingBuffer(ByteBuffer buffer) {
    LOGGER.debug("Skip storing a pending buffer");
    return this;
  }

  @Override
  public DefaultBufferAllocator putWriteBuffer(ByteBuffer buffer) {
    LOGGER.debug("Skip storing a write buffer");
    return this;
  }

  @Override
  public BufferAllocator putBuffer(ByteBuffer buffer) {
    LOGGER.debug("Skip storing a mapped byte buffer");
    return this;
  }
}
