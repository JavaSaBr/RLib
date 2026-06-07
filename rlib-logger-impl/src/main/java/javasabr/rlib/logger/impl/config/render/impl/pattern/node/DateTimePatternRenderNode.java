package javasabr.rlib.logger.impl.config.render.impl.pattern.node;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(of = "pattern")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DateTimePatternRenderNode implements PatternRenderNode {

  public static final String NODE_NAME = "dateTime";

  String pattern;
  DateTimeFormatter formatter;

  public DateTimePatternRenderNode(String pattern) {
    this.formatter = DateTimeFormatter.ofPattern(pattern);
    this.pattern = pattern;
  }
  
  @Override
  public void append(LoggerLevel level, Logger logger, String message, StringBuilder buffer) {
    buffer.append(formatter.format(LocalDateTime.now()));
  }
}
