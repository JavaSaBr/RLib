package javasabr.rlib.logger.impl.config.render.impl.pattern;

import java.util.Map;
import javasabr.rlib.collections.array.UnsafeArray;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.config.render.LogMessageRender;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.PatternRenderNode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatternLogMessageRender implements LogMessageRender {

  private static final int DEF_INIT_BUFFER_SIZE = 256;
  
  UnsafeArray<PatternRenderNode> renderNodes;
  int initBufferSize;

  public PatternLogMessageRender(Map<String, Object> args) {
    this(extractPattern(args), extractInitBufferSize(args));
  }
  
  public PatternLogMessageRender(String pattern) {
    this(pattern, DEF_INIT_BUFFER_SIZE);
  }

  public PatternLogMessageRender(String pattern, int initBufferSize) {
    this.initBufferSize = initBufferSize;
    this.renderNodes = PatternRenderNodesParser
        .parse(pattern)
        .asUnsafe();
  }
  
  @Override
  public String render(LoggerLevel level, Logger logger, String message) {
    var buffer = new StringBuilder(initBufferSize);
    for (PatternRenderNode renderNode : renderNodes.wrapped()) {
      //noinspection DataFlowIssue
      renderNode.append(level, logger, message, buffer);
    }
    return buffer.toString();
  }

  private static String extractPattern(Map<String, Object> args) {
    Object pattern = args.get("pattern");
    if (pattern instanceof String stringPattern) {
      return stringPattern;
    } else {
      throw new IllegalArgumentException("'pattern' argument must be a string");
    }
  }

  private static int extractInitBufferSize(Map<String, Object> args) {
    Object initBufferSize = args.get("initBufferSize");
    if (initBufferSize instanceof Integer integer) {
      return integer;
    } else if (initBufferSize instanceof String string) {
      return Integer.parseInt(string);
    } else {
      return DEF_INIT_BUFFER_SIZE;
    }
  }
}
