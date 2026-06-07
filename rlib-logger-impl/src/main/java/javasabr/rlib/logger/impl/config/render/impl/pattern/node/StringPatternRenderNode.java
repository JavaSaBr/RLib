package javasabr.rlib.logger.impl.config.render.impl.pattern.node;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StringPatternRenderNode implements PatternRenderNode {

  String string;
  
  @Override
  public void append(LoggerLevel level, Logger logger, String message, StringBuilder buffer) {
    buffer.append(string);
  }
}
