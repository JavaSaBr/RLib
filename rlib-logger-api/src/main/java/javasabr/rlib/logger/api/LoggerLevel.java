package javasabr.rlib.logger.api;

import lombok.Getter;
import lombok.Setter;

/**
 * The list of logging levels.
 *
 * @author JavaSaBr
 */
@Getter
public enum LoggerLevel {
  /**
   * Info logger level.
   */
  INFO("INFO", false, true),
  /**
   * Debug logger level.
   */
  DEBUG("DEBUG", false, false),
  /**
   * Warning logger level.
   */
  WARNING("WARNING", true, true),
  /**
   * Error logger level.
   */
  ERROR("ERROR", true, true);

  public static final int LENGTH = values().length;

  /**
   * The level title.
   */
  @Setter
  private String title;

  /**
   * The flag of activity.
   */
  @Setter
  private boolean enabled;

  /**
   * The flag of force flushing.
   */
  private boolean forceFlush;

  LoggerLevel(String title, boolean forceFlush, boolean enabled) {
    this.title = title;
    this.forceFlush = forceFlush;
    this.enabled = enabled;
  }

  @Override
  public String toString() {
    return title;
  }
}
