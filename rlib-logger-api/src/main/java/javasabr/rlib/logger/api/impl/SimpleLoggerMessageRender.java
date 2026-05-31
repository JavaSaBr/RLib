package javasabr.rlib.logger.api.impl;

import java.time.format.DateTimeFormatter;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerMessageRender;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SimpleLoggerMessageRender implements LoggerMessageRender {

  DateTimeFormatter timeFormatter;

  private SimpleLoggerMessageRender() {
    this.timeFormatter = DateTimeFormatter.ofPattern("d.MM.yyyy HH:mm:ss:SSS");
  }

  @Override
  public String render(LoggerLevel level, String loggerName, String logMessage) {
    return "";
  }
}
