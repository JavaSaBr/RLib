package javasabr.rlib.logger.impl.config.render.impl.pattern.node;

import static org.assertj.core.api.Assertions.assertThat;

import javasabr.rlib.logger.api.LoggerLevel;
import org.junit.jupiter.api.Test;

class StringPatternRenderNodeTest {

  @Test
  void shouldAppendConfiguredStringToBuffer() {
    // given:
    var node = new StringPatternRenderNode("configured-value");
    var buffer = new StringBuilder("prefix:");

    // when:
    node.append(LoggerLevel.DEBUG, PatternRenderNodeTestUtils.LOGGER, "ignored-message", buffer);

    // then:
    assertThat(buffer.toString())
        .isEqualTo("prefix:configured-value");
  }
}
