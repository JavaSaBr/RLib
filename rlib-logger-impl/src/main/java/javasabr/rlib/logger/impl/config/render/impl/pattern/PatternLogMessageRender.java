package javasabr.rlib.logger.impl.config.render.impl.pattern;

import javasabr.rlib.collections.array.UnsafeArray;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.config.render.LogMessageRender;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.PatternRenderNode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatternLogMessageRender implements LogMessageRender {
  
  UnsafeArray<PatternRenderNode> renderNodes;
  int initBufferSize;

  public PatternLogMessageRender(String pattern) {
    this(pattern, 256);
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
}
