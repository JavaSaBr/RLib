package javasabr.rlib.logger.impl.config;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;

/**
 * Renderer for log messages.
 *
 * @since 10.0.0
 */
public interface LogMessageRender {
  
  /**
   * Renders a log message.
   *
   * @param level the log level
   * @param logger the logger
   * @param message the message
   * @return the rendered message
   * @since 10.0.0
   */
  String render(LoggerLevel level, Logger logger, String message);
}
