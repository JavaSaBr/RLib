package javasabr.rlib.logger.impl.config.render.impl.pattern.node;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LevelPatternRenderNode implements PatternRenderNode {
  
  public static final String NODE_NAME = "level";
  
  @Override
  public void append(LoggerLevel level, Logger logger, String message, StringBuilder buffer) {
    buffer
        .append(level.title())
        .append(level.offset());
  }
}
