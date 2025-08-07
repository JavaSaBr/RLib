package javasabr.rlib.common.monitoring;

import org.jspecify.annotations.NullMarked;

/**
 * The class to monitor a state a Java process.
 *
 * @author JavaSaBr
 */
@NullMarked
public final class MonitoringManager {

  private static final MonitoringManager INSTANCE = new MonitoringManager();

  /**
   * Gets instance.
   *
   * @return the instance
   */
  public static MonitoringManager getInstance() {
    return INSTANCE;
  }

  /**
   * The memory monitoring.
   */
  private final MemoryMonitoring memoryMonitoring;

  /**
   * The runtime monitoring.
   */
  private final RuntimeMonitoring runtimeMonitoring;

  /**
   * The threads monitoring.
   */
  private final ThreadMonitoring threadMonitoring;

  private MonitoringManager() {
    this.memoryMonitoring = new MemoryMonitoring();
    this.runtimeMonitoring = new RuntimeMonitoring();
    this.threadMonitoring = new ThreadMonitoring();
  }

  /**
   * Gets memory monitoring.
   *
   * @return the memory monitoring.
   */
  public MemoryMonitoring getMemoryMonitoring() {
    return memoryMonitoring;
  }

  /**
   * Gets runtime monitoring.
   *
   * @return the runtime monitoring.
   */
  public RuntimeMonitoring getRuntimeMonitoring() {
    return runtimeMonitoring;
  }

  /**
   * Gets thread monitoring.
   *
   * @return the threads monitoring.
   */
  public ThreadMonitoring getThreadMonitoring() {
    return threadMonitoring;
  }

  @Override
  public String toString() {

    StringBuilder builder = new StringBuilder("\n");
    builder
        .append("#==========Java Process Info==========#")
        .append('\n');
    builder.append("#=============Memory Info=============#");
    builder
        .append('\n')
        .append(getMemoryMonitoring())
        .append('\n');
    builder.append("#============Runtime Info=============#");
    builder
        .append('\n')
        .append(getRuntimeMonitoring())
        .append('\n');
    // builder.append("#============Current state============#");
    // builder.append('\n').append(getThreadMonitoring()).append('\n');
    builder.append("#=====================================#");

    return builder.toString();
  }
}
