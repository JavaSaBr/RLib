package javasabr.rlib.logger.impl.config.render.impl.pattern.node;

import static org.assertj.core.api.Assertions.assertThat;

import javasabr.rlib.logger.api.LoggerLevel;
import org.junit.jupiter.api.Test;

class ShortLoggerPatternRenderNodeTest {

  @Test
  void shouldAppendLoggerShortNameToBuffer() {
    // given:
    var node = new ShortLoggerPatternRenderNode();
    var buffer = new StringBuilder("prefix:");

    // when:
    node.append(LoggerLevel.INFO, PatternRenderNodeTestUtils.LOGGER, "ignored-message", buffer);

    // then:
    assertThat(buffer.toString())
        .isEqualTo("prefix:PatternRenderNodeTest");
  }
}
