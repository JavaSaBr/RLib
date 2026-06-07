package javasabr.rlib.logger.impl.config.render.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.config.render.LogMessageRender;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SimpleLogMessageRender implements LogMessageRender {

  DateTimeFormatter timeFormatter;

  public SimpleLogMessageRender() {
    this.timeFormatter = DateTimeFormatter.ofPattern("d.MM.yyyy HH:mm:ss:SSS");
  }

  @Override
  public String render(LoggerLevel level, Logger logger, String message) {
    var timestamp = timeFormatter.format(LocalDateTime.now());
    return timestamp + ' ' 
        + level.title()
        + level.offset() + ' '
        + logger.shortName() + ": "
        + message;
  }
}
