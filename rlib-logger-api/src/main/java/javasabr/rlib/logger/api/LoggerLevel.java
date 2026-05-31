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
  TRACE("TRACE", " ", false, false),
  DEBUG("DEBUG", " ", false, false),
  INFO("INFO", "  ", true, true),
  WARNING("WARN", "  ", true, true),
  ERROR("ERROR", " ", true, true);

  /**
   * The number of log levels.
   */
  public static final int LENGTH = values().length;

  String title;
  String offset;

  boolean enabled;
  boolean forceFlush;

  @Override
  public String toString() {
    return title;
  }
}
