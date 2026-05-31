package javasabr.rlib.logger.api;

public interface LoggerMessageRender {
  
  String render(LoggerLevel level, String loggerName, String logMessage);
}
