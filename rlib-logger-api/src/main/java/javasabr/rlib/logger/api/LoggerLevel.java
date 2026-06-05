package javasabr.rlib.logger.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * Enumeration of log levels.
 *
 * @since 10.0.0
 */
@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum LoggerLevel {
  TRACE("TRACE", " ", 0, false, false),
  DEBUG("DEBUG", " ", 1, false, false),
  INFO("INFO", "  ", 2, true, true),
  WARNING("WARN", "  ", 3, true, true),
  ERROR("ERROR", " ", 4, true, true);

  /**
   * The number of log levels.
   */
  public static final int LENGTH = values().length;

  String title;
  String offset;

  int severity;
  boolean enabled;
  boolean forceFlush;

  @Override
  public String toString() {
    return title;
  }
}
