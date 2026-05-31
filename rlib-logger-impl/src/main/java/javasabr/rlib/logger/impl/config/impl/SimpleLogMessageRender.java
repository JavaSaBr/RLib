package javasabr.rlib.logger.impl.config.impl;

import java.time.format.DateTimeFormatter;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.config.LogMessageRender;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SimpleLogMessageRender implements LogMessageRender {

  DateTimeFormatter timeFormatter;

  private SimpleLogMessageRender() {
    this.timeFormatter = DateTimeFormatter.ofPattern("d.MM.yyyy HH:mm:ss:SSS");
  }

  @Override
  public String render(LoggerLevel level, String loggerName, String message) {
    return "";
  }
}
