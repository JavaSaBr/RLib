package javasabr.rlib.logger.impl.config.render.impl.pattern.node;

import static org.assertj.core.api.Assertions.assertThat;

import javasabr.rlib.logger.api.LoggerLevel;
import org.junit.jupiter.api.Test;

class MessagePatternRenderNodeTest {

  @Test
  void shouldAppendMessageToBuffer() {
    // given:
    var node = new MessagePatternRenderNode();
    var buffer = new StringBuilder("prefix:");

    // when:
    node.append(LoggerLevel.ERROR, PatternRenderNodeTestUtils.LOGGER, "actual-message", buffer);

    // then:
    assertThat(buffer.toString())
        .isEqualTo("prefix:actual-message");
  }
}
