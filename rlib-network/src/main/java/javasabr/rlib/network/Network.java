package javasabr.rlib.network;

import java.util.concurrent.ScheduledExecutorService;

/**
 * The interface to implement an asynchronous network.
 *
 * @param <C> the connection type
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface Network<C extends Connection<C>> {

  /**
   * Gets the scheduled executor service for this network.
   *
   * @return the scheduled executor service
   * @since 10.0.0
   */
  ScheduledExecutorService scheduledExecutor();

  /**
   * Gets the network configuration.
   *
   * @return the network config
   * @since 10.0.0
   */
  NetworkConfig config();

  /**
   * Executes a task in the network thread.
   *
   * @param task the task to execute
   * @since 10.0.0
   */
  void inNetworkThread(Runnable task);

  /**
   * Shuts down this network and releases all resources.
   *
   * @since 10.0.0
   */
  void shutdown();
}
