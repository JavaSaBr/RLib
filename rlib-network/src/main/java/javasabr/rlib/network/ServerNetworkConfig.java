package javasabr.rlib.network;

import java.nio.ByteOrder;
import javasabr.rlib.common.util.GroupThreadFactory.ThreadConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * The interface to implement a server network configuration.
 *
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface ServerNetworkConfig extends NetworkConfig {

  /**
   * Simple implementation of server network configuration using Lombok builder.
   *
   * @since 10.0.0
   */
  @Builder
  @Getter
  @Accessors(fluent = true, chain = false)
  class SimpleServerNetworkConfig implements ServerNetworkConfig {

    @Builder.Default
    private String threadGroupName = "ServerNetworkThread";
    @Builder.Default
    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    @Builder.Default
    private ThreadConstructor threadConstructor = Thread::new;

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
    @Builder.Default
    private int threadGroupMinSize = 1;
    @Builder.Default
    private int threadGroupMaxSize = 1;
    @Builder.Default
    private int scheduledThreadGroupSize = 1;
    @Builder.Default
    private int threadPriority = Thread.NORM_PRIORITY;
  }

  ServerNetworkConfig DEFAULT_SERVER = new ServerNetworkConfig() {

    @Override
    public String threadGroupName() {
      return "ServerNetworkThread";
    }

    @Override
    public String scheduledThreadGroupName() {
      return "ServerScheduledNetworkThread";
    }
  };

  /**
   * Gets the minimal size of network thread executor.
   *
   * @return the minimum thread pool size
   * @since 10.0.0
   */
  default int threadGroupMinSize() {
    return 1;
  }

  /**
   * Gets the maximum size of network thread executor.
   *
   * @return the maximum thread pool size
   * @since 10.0.0
   */
  default int threadGroupMaxSize() {
    return threadGroupMinSize();
  }

  /**
   * Gets the size of network scheduled thread executor.
   *
   * @return the scheduled thread pool size
   * @since 10.0.0
   */
  default int scheduledThreadGroupSize() {
    return 1;
  }
}
