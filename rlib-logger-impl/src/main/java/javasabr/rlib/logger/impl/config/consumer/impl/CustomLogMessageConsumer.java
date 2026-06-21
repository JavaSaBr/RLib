package javasabr.rlib.logger.impl.config.consumer.impl;

import java.util.Map;
import javasabr.rlib.logger.impl.config.consumer.LogMessageConsumer;
import javasabr.rlib.logger.impl.config.render.LogMessageRender;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class CustomLogMessageConsumer implements LogMessageConsumer {

  LogMessageRender render;

  protected CustomLogMessageConsumer(LogMessageRender render, Map<String, Object> args) {
    this.render = render;
  }
}
