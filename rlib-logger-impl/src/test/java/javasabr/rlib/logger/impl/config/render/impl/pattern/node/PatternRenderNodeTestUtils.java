package javasabr.rlib.logger.impl.config.render.impl.pattern.node;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;

final class PatternRenderNodeTestUtils {

  static final Logger LOGGER = new Logger() {
    @Override
    public String name() {
      return "example.logger.PatternRenderNodeTest";
    }

    @Override
    public String shortName() {
      return "PatternRenderNodeTest";
    }

    @Override
    public void print(LoggerLevel level, String message) {}

    @Override
    public void print(LoggerLevel level, Throwable exception) {}

    @Override
    public void print(LoggerLevel level, String message, Throwable exception) {}
  };

  private PatternRenderNodeTestUtils() {}
}
