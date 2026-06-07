package javasabr.rlib.logger.impl.config.render.impl.pattern;

import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.DateTimePatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.LevelPatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.MessagePatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.PatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.ShortLoggerPatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.StringPatternRenderNode;

public class PatternRenderNodesParser {
  
  public static Array<PatternRenderNode> parse(String pattern) {
    var parsedNodes = ArrayFactory.mutableArray(PatternRenderNode.class);
    var stringBuffer = new StringBuilder();
    for(int i = 0, length = pattern.length(); i < length; i++) {
      char nextChar = pattern.charAt(i);
      if (nextChar != '%') {
        stringBuffer.append(nextChar);
        continue;
      }
      if (!stringBuffer.isEmpty()) {
        parsedNodes.add(new StringPatternRenderNode(stringBuffer.toString()));
        stringBuffer.setLength(0);
      }
      var nodeName = resolveNodeName(pattern, i + 1);
      var afterNameIndex = i + 1 + nodeName.length();
      ParsedNode parsedNode = parse(nodeName, pattern, afterNameIndex);
      parsedNodes.add(parsedNode.renderNode);
      i = parsedNode.finishedIndex - 1;
    }
    if (!stringBuffer.isEmpty()) {
      parsedNodes.add(new StringPatternRenderNode(stringBuffer.toString()));
      stringBuffer.setLength(0);
    }
    return Array.copyOf(parsedNodes);
  }

  private static String resolveNodeName(String pattern, int startIndex) {
    var result = new StringBuilder();
    for (int i = startIndex, length = pattern.length(); i < length; i++) {
      char nextChar = pattern.charAt(i);
      if (nextChar == '{' || nextChar == ' ' || nextChar == '\t') {
        return result.toString();
      }
      result.append(nextChar);
    }
    return result.toString();
  }
  
  private static ParsedNode parse(String nodeName, String pattern, int afterNameIndex) {
    return switch (nodeName) {
      case LevelPatternRenderNode.NODE_NAME -> new ParsedNode(new LevelPatternRenderNode(), afterNameIndex);
      case MessagePatternRenderNode.NODE_NAME -> new ParsedNode(new MessagePatternRenderNode(), afterNameIndex);
      case ShortLoggerPatternRenderNode.NODE_NAME -> new ParsedNode(new ShortLoggerPatternRenderNode(), afterNameIndex);
      case DateTimePatternRenderNode.NODE_NAME -> {
        var dateTimePattern = extractArgumentsString(pattern, afterNameIndex);
        int finishedIndex = afterNameIndex + dateTimePattern.length() + 2;
        yield new ParsedNode(new DateTimePatternRenderNode(dateTimePattern), finishedIndex);
      }
      default -> throw new IllegalArgumentException("Unexpected node name:[%s]".formatted(nodeName));
    };
  }

  private static String extractArgumentsString(String pattern, int afterNameIndex) {
    if (pattern.charAt(afterNameIndex) != '{') {
      throw new IllegalArgumentException("Missed '{' at position:[%d] in pattern:[%s]"
          .formatted(afterNameIndex, pattern));
    }
    var result = new StringBuilder();
    int closingBraceIndex = -1;
    for (int i = afterNameIndex + 1, length = pattern.length(); i < length; i++) {
      char nextChar = pattern.charAt(i);
      if (nextChar == '}') {
        closingBraceIndex = i;
        break;
      }
      result.append(nextChar);
    }
    if (closingBraceIndex < 0) {
      throw new IllegalArgumentException("Missed closing '}' in pattern:[%s]"
          .formatted(pattern));
    }
    return result.toString();
  }

  private record ParsedNode(PatternRenderNode renderNode, int finishedIndex) {}
}
