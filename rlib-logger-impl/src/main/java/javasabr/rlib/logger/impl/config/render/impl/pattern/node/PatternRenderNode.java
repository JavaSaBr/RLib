package javasabr.rlib.logger.impl.config.render.impl.pattern.node;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;

public interface PatternRenderNode {
  
  void append(LoggerLevel level, Logger logger, String message, StringBuilder buffer);
}
