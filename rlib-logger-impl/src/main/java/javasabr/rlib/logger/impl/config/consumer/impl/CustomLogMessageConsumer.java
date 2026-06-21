package javasabr.rlib.logger.impl.config.consumer.impl;

import java.util.Map;
import javasabr.rlib.logger.impl.config.consumer.LogMessageConsumer;

public abstract class CustomLogMessageConsumer implements LogMessageConsumer {

  protected CustomLogMessageConsumer(Map<String, Object> args) {
  }
}
