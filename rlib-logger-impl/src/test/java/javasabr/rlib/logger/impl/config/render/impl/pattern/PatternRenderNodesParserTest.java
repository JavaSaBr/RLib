package javasabr.rlib.logger.impl.config.render.impl.pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javasabr.rlib.collections.array.Array;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.DateTimePatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.LevelPatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.MessagePatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.PatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.ShortLoggerPatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.StringPatternRenderNode;
import org.junit.jupiter.api.Test;

public class PatternRenderNodesParserTest {
  
  @Test
  void shouldParseCorrectlyPattern1() {
    // given:
    var pattern = "  %dateTime{d.MM.yyyy HH:mm:ss:SSS} %level %shortLogger : %msg";
    
    // when:
    Array<PatternRenderNode> parsed = PatternRenderNodesParser.parse(pattern);
    
    // then:
    assertThat(parsed.size()).isEqualTo(8);
    assertThat(parsed.get(0))
        .isInstanceOf(StringPatternRenderNode.class)
        .isEqualTo(new StringPatternRenderNode("  "));
    assertThat(parsed.get(1))
        .isInstanceOf(DateTimePatternRenderNode.class)
        .isEqualTo(new DateTimePatternRenderNode("d.MM.yyyy HH:mm:ss:SSS"));
    assertThat(parsed.get(2))
        .isInstanceOf(StringPatternRenderNode.class)
        .isEqualTo(new StringPatternRenderNode(" "));
    assertThat(parsed.get(3))
        .isInstanceOf(LevelPatternRenderNode.class);
    assertThat(parsed.get(4))
        .isInstanceOf(StringPatternRenderNode.class)
        .isEqualTo(new StringPatternRenderNode(" "));
    assertThat(parsed.get(5))
        .isInstanceOf(ShortLoggerPatternRenderNode.class);
    assertThat(parsed.get(6))
        .isInstanceOf(StringPatternRenderNode.class)
        .isEqualTo(new StringPatternRenderNode(" : "));
    assertThat(parsed.get(7))
        .isInstanceOf(MessagePatternRenderNode.class);
  }

  @Test
  void shouldParsePatternWithOnlyLiteralText() {
    // given:
    var pattern = "just a literal string";

    // when:
    Array<PatternRenderNode> parsed = PatternRenderNodesParser.parse(pattern);

    // then:
    assertThat(parsed.size())
        .isEqualTo(1);
    assertThat(parsed.get(0))
        .isInstanceOf(StringPatternRenderNode.class)
        .isEqualTo(new StringPatternRenderNode("just a literal string"));
  }

  @Test
  void shouldParsePatternWithMessageBetweenLiteralTexts() {
    // given:
    var pattern = "prefix %msg suffix";

    // when:
    Array<PatternRenderNode> parsed = PatternRenderNodesParser.parse(pattern);

    // then:
    assertThat(parsed.size())
        .isEqualTo(3);
    assertThat(parsed.get(0))
        .isInstanceOf(StringPatternRenderNode.class)
        .isEqualTo(new StringPatternRenderNode("prefix "));
    assertThat(parsed.get(1))
        .isInstanceOf(MessagePatternRenderNode.class);
    assertThat(parsed.get(2))
        .isInstanceOf(StringPatternRenderNode.class)
        .isEqualTo(new StringPatternRenderNode(" suffix"));
  }

  @Test
  void shouldParsePatternWithSimpleNodeSequence() {
    // given:
    var pattern = "%level %msg %shortLogger";

    // when:
    Array<PatternRenderNode> parsed = PatternRenderNodesParser.parse(pattern);

    // then:
    assertThat(parsed.size())
        .isEqualTo(5);
    assertThat(parsed.get(0))
        .isInstanceOf(LevelPatternRenderNode.class);
    assertThat(parsed.get(1))
        .isInstanceOf(StringPatternRenderNode.class)
        .isEqualTo(new StringPatternRenderNode(" "));
    assertThat(parsed.get(2))
        .isInstanceOf(MessagePatternRenderNode.class);
    assertThat(parsed.get(3))
        .isInstanceOf(StringPatternRenderNode.class)
        .isEqualTo(new StringPatternRenderNode(" "));
    assertThat(parsed.get(4))
        .isInstanceOf(ShortLoggerPatternRenderNode.class);
  }

  @Test
  void shouldThrowWhenNodeNameIsUnknown() {
    // given:
    var pattern = "%unknown";

    // when/then:
    assertThatThrownBy(() -> PatternRenderNodesParser.parse(pattern))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Unexpected node name:[unknown]");
  }

  @Test
  void shouldThrowWhenDateTimeNodeHasNoOpeningBrace() {
    // given:
    var pattern = "%dateTime yyyy-MM-dd";

    // when/then:
    assertThatThrownBy(() -> PatternRenderNodesParser.parse(pattern))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Missed '{'");
  }

  @Test
  void shouldThrowWhenDateTimeNodeHasNoClosingBrace() {
    // given:
    var pattern = "%dateTime{yyyy-MM-dd";

    // when/then:
    assertThatThrownBy(() -> PatternRenderNodesParser.parse(pattern))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Missed closing '}'");
  }
}
