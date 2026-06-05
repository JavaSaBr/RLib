package javasabr.rlib.logger.impl.config.impl;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.config.LogMessageConsumer;
import javasabr.rlib.logger.impl.config.LogMessageRender;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsoleMessageConsumer implements LogMessageConsumer {
  
  LogMessageRender logMessageRender;
  
  @Override
  public void consume(LoggerLevel level, Logger logger, String message) {
    String rendered = logMessageRender.render(level, logger, message);
    switch (level) {
      case INFO, DEBUG -> System.out.println(rendered);
      case ERROR, WARNING -> System.err.println(rendered);
    }
  }
}
