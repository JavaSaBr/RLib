package javasabr.rlib.network.impl;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.function.Function;
import javasabr.rlib.common.util.array.ArrayFactory;
import javasabr.rlib.common.util.array.ConcurrentArray;
import javasabr.rlib.common.util.pools.Pool;
import javasabr.rlib.common.util.pools.PoolFactory;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerManager;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.NetworkConfig;
import lombok.ToString;

/**
 * @author JavaSaBr
 */
@ToString
public class ReuseBufferAllocator implements BufferAllocator {

  protected static final Logger LOGGER = LoggerManager.getLogger(ReuseBufferAllocator.class);

  protected final Pool<ByteBuffer> readBufferPool;
  protected final Pool<ByteBuffer> pendingBufferPool;
  protected final Pool<ByteBuffer> writeBufferPool;
  protected final ConcurrentArray<ByteBuffer> byteBuffers;

  protected final NetworkConfig config;

  public ReuseBufferAllocator(NetworkConfig config) {
    this.config = config;
    this.readBufferPool = PoolFactory.newConcurrentStampedLockPool(ByteBuffer.class);
    this.pendingBufferPool = PoolFactory.newConcurrentStampedLockPool(ByteBuffer.class);
    this.writeBufferPool = PoolFactory.newConcurrentStampedLockPool(ByteBuffer.class);
    this.byteBuffers = ArrayFactory.newConcurrentStampedLockArray(ByteBuffer.class);
  }

  @Override
  public ByteBuffer takeReadBuffer() {
    return readBufferPool
        .take(config, readBufferFactory())
        .clear();
  }

  @Override
  public ByteBuffer takePendingBuffer() {
    return pendingBufferPool
        .take(config, pendingBufferFactory())
        .clear();
  }

  @Override
  public ByteBuffer takeWriteBuffer() {
    return writeBufferPool
        .take(config, writeBufferFactory())
        .clear();
  }

  protected Function<NetworkConfig, ByteBuffer> pendingBufferFactory() {
    return config -> {
      var bufferSize = config.getPendingBufferSize();
      LOGGER.debug(bufferSize, size -> "Allocate a new pending buffer with size: " + size);
      return config.isDirectByteBuffer()
             ? ByteBuffer.allocateDirect(bufferSize)
             : ByteBuffer
                 .allocate(bufferSize)
                 .order(config.getByteOrder())
                 .clear();
    };
  }

  protected Function<NetworkConfig, ByteBuffer> readBufferFactory() {
    return config -> {
      var bufferSize = config.getReadBufferSize();
      LOGGER.debug(bufferSize, size -> "Allocate a new read buffer with size: " + size);
      return config.isDirectByteBuffer()
             ? ByteBuffer.allocateDirect(bufferSize)
             : ByteBuffer
                 .allocate(bufferSize)
                 .order(config.getByteOrder())
                 .clear();
    };
  }

  protected Function<NetworkConfig, ByteBuffer> writeBufferFactory() {
    return config -> {
      var bufferSize = config.getWriteBufferSize();
      LOGGER.debug(bufferSize, size -> "Allocate a new write buffer with size: " + size);
      return config.isDirectByteBuffer()
             ? ByteBuffer.allocateDirect(bufferSize)
             : ByteBuffer
                 .allocate(bufferSize)
                 .order(config.getByteOrder())
                 .clear();
    };
  }

  @Override
  public ByteBuffer takeBuffer(int bufferSize) {

    // check of existing enough buffer for the size under read lock
    var exist = byteBuffers.findAnyInReadLock(bufferSize, (required, buffer) -> required < buffer.capacity());

    // if we already possible have this buffer we need to take it under write lock
    if (exist != null) {
      long stamp = byteBuffers.writeLock();
      try {

        // re-find enough buffer again
        exist = byteBuffers.findAny(bufferSize, (required, buffer) -> required < buffer.capacity());

        // take it from pool if exist
        if (exist != null && byteBuffers.fastRemove(exist)) {
          LOGGER.debug(exist, buffer -> "Reuse old buffer: " + buffer + " - (" + buffer.hashCode() + ")");
          return exist;
        }

      } finally {
        byteBuffers.writeUnlock(stamp);
      }
    }

    LOGGER.debug(bufferSize, size -> "Allocate a new buffer with size: " + size);
    return config.isDirectByteBuffer()
           ? ByteBuffer.allocateDirect(bufferSize)
           : ByteBuffer
               .allocate(bufferSize)
               .order(config.getByteOrder())
               .clear();
  }

  @Override
  public ReuseBufferAllocator putReadBuffer(ByteBuffer buffer) {
    readBufferPool.put(buffer);
    return this;
  }

  @Override
  public ReuseBufferAllocator putPendingBuffer(ByteBuffer buffer) {
    pendingBufferPool.put(buffer);
    return this;
  }

  @Override
  public ReuseBufferAllocator putWriteBuffer(ByteBuffer buffer) {
    writeBufferPool.put(buffer);
    return this;
  }

  @Override
  public BufferAllocator putBuffer(ByteBuffer buffer) {
    LOGGER.debug(buffer, buf -> "Save used temp buffer: " + buf + " - (" + buf.hashCode() + ")");
    byteBuffers.runInWriteLock(buffer.clear(), Collection::add);
    return this;
  }
}
