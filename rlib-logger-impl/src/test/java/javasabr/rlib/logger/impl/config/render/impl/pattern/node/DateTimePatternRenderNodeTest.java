package javasabr.rlib.logger.impl.config.render.impl.pattern.node;

import static org.assertj.core.api.Assertions.assertThat;

import javasabr.rlib.logger.api.LoggerLevel;
import org.junit.jupiter.api.Test;

class DateTimePatternRenderNodeTest {

  @Test
  void shouldAppendCurrentDateTimeUsingProvidedPattern() {
    // given:
    var node = new DateTimePatternRenderNode("yyyy-MM-dd HH:mm:ss");
    var buffer = new StringBuilder("prefix:");

    // when:
    node.append(LoggerLevel.INFO, PatternRenderNodeTestUtils.LOGGER, "ignored-message", buffer);

    // then:
    assertThat(buffer.toString())
        .matches("prefix:\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
  }
}
