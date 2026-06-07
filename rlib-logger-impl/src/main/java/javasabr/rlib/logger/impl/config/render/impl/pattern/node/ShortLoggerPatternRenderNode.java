package javasabr.rlib.logger.impl.config.render.impl.pattern.node;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShortLoggerPatternRenderNode implements PatternRenderNode {

  public static final String NODE_NAME = "shortLogger";
  
  @Override
  public void append(LoggerLevel level, Logger logger, String message, StringBuilder buffer) {
    buffer.append(logger.shortName());
  }
}
