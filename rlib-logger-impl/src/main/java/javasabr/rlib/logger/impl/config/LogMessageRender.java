package javasabr.rlib.logger.impl.config;

import javasabr.rlib.logger.api.LoggerLevel;

public interface LogMessageRender {
  
  String render(LoggerLevel level, String loggerName, String message);
}
