package javasabr.rlib.logger.impl.config.render.impl.pattern.node;

import static org.assertj.core.api.Assertions.assertThat;

import javasabr.rlib.logger.api.LoggerLevel;
import org.junit.jupiter.api.Test;

class LevelPatternRenderNodeTest {

  @Test
  void shouldAppendLevelTitleWithOffset() {
    // given:
    var node = new LevelPatternRenderNode();
    var buffer = new StringBuilder("prefix:");

    // when:
    node.append(LoggerLevel.WARNING, PatternRenderNodeTestUtils.LOGGER, "ignored-message", buffer);

    // then:
    assertThat(buffer.toString())
        .isEqualTo("prefix:WARN  ");
  }
}
