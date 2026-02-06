package javasabr.rlib.network;

import java.nio.ByteOrder;
import javasabr.rlib.common.util.GroupThreadFactory.ThreadConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * The interface to implement a network configuration.
 *
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface NetworkConfig {

  /**
   * Simple implementation of network configuration using Lombok builder.
   *
   * @since 10.0.0
   */
  @Builder
  @Getter
  @Accessors(fluent = true, chain = false)
  class SimpleNetworkConfig implements NetworkConfig {

    @Builder.Default
    private String threadGroupName = "NetworkThread";
    @Builder.Default
    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    @Builder.Default
    private ThreadConstructor threadConstructor = Thread::new;
    @Builder.Default
    private int threadPriority = Thread.NORM_PRIORITY;

    @Builder.Default
    private int readBufferSize = 2048;
    @Builder.Default
    private int pendingBufferSize = 4096;
    @Builder.Default
    private int writeBufferSize = 2048;
    @Builder.Default
    private int retryDelayInMs = 1000;
    @Builder.Default
    private int maxPacketSize = 5 * 1024 * 1024;
    @Builder.Default
    private int maxEmptyReadsBeforeClose = 3;
    @Builder.Default
    private boolean useDirectByteBuffer = false;
  }

  NetworkConfig DEFAULT_CLIENT = new NetworkConfig() {

    @Override
    public String threadGroupName() {
      return "ClientNetworkThread";
    }
  };

  /**
   * Gets the thread constructor for creating network threads.
   *
   * @return the thread constructor
   * @since 10.0.0
   */
  default ThreadConstructor threadConstructor() {
    return Thread::new;
  }

  /**
   * Gets the priority of network threads.
   *
   * @return the thread priority
   * @since 10.0.0
   */
  default int threadPriority() {
    return Thread.NORM_PRIORITY;
  }

  /**
   * Gets the group name of network threads.
   *
   * @return the thread group name
   * @since 10.0.0
   */
  default String threadGroupName() {
    return "NetworkThread";
  }

  /**
   * Gets the group name of scheduled network threads.
   *
   * @return the scheduled thread group name
   * @since 10.0.0
   */
  default String scheduledThreadGroupName() {
    return "ScheduledNetworkThread";
  }

  /**
   * Gets the size of buffer used to collect received data from network.
   *
   * @return the read buffer size in bytes
   * @since 10.0.0
   */
  default int readBufferSize() {
    return 2048;
  }

  /**
   * Gets the size of buffer for pending reading data. The pending buffer allows constructing
   * a packet with bigger data part than {@link #readBufferSize()}.
   * It should be at least 2x of {@link #readBufferSize()}.
   *
   * @return the pending buffer size in bytes
   * @since 10.0.0
   */
  default int pendingBufferSize() {
    return readBufferSize() * 2;
  }

  /**
   * Gets the size of buffer used for packet serialization.
   *
   * @return the write buffer size in bytes
   * @since 10.0.0
   */
  default int writeBufferSize() {
    return 2048;
  }

  /**
   * Gets the maximum size of a single network packet.
   *
   * @return the max packet size in bytes
   * @since 10.0.0
   */
  default int maxPacketSize() {
    return 5 * 1024 * 1024;
  }

  /**
   * Gets the timeout for retry read/write operations.
   *
   * @return the retry delay in milliseconds
   * @since 10.0.0
   */
  default int retryDelayInMs() {
    return 1000;
  }

  /**
   * Gets the maximum allowed empty reads from socket channel before closing a connection.
   *
   * @return the max empty reads count
   * @since 10.0.0
   */
  default int maxEmptyReadsBeforeClose() {
    return 3;
  }

  /**
   * Gets the byte order for network data.
   *
   * @return the byte order
   * @since 10.0.0
   */
  default ByteOrder byteOrder() {
    return ByteOrder.BIG_ENDIAN;
  }

  /**
   * Checks if direct byte buffers should be used.
   *
   * @return true if direct buffers should be used
   * @since 10.0.0
   */
  default boolean useDirectByteBuffer() {
    return false;
  }
}
