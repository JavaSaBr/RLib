package javasabr.rlib.logger.impl.config.render.impl.pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerManager;
import org.junit.jupiter.api.Test;

class PatternLogMessageRenderTest {

  @Test
  void shouldRenderMessageUsingMapArgsConstructor() {
    // given:
    var render = new PatternLogMessageRender(Map.of("pattern", "%msg"));
    var logger = LoggerManager.getLogger("test.logger");

    // when:
    String result = render.render(LoggerLevel.INFO, logger, "hello");

    // then:
    assertThat(result).isEqualTo("hello");
  }

  @Test
  void shouldUseDefaultInitBufferSizeWhenNotProvided() {
    // given:
    var render = new PatternLogMessageRender(Map.of("pattern", "%msg"));

    // then:
    assertThat(render)
        .extracting("initBufferSize")
        .isEqualTo(256);
  }

  @Test
  void shouldUseInitBufferSizeFromIntegerArgument() {
    // given:
    var render = new PatternLogMessageRender(Map.of(
        "pattern", "%msg",
        "initBufferSize", 128));

    // then:
    assertThat(render)
        .extracting("initBufferSize")
        .isEqualTo(128);
  }

  @Test
  void shouldUseInitBufferSizeFromStringArgument() {
    // given:
    var render = new PatternLogMessageRender(Map.of(
        "pattern", "%msg",
        "initBufferSize", "512"));

    // then:
    assertThat(render)
        .extracting("initBufferSize")
        .isEqualTo(512);
  }

  @Test
  void shouldThrowWhenPatternArgumentIsMissing() {
    // when/then:
    assertThatThrownBy(() -> new PatternLogMessageRender(Map.of()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("'pattern' argument must be a string");
  }

  @Test
  void shouldThrowWhenPatternArgumentIsNotString() {
    // when/then:
    assertThatThrownBy(() -> new PatternLogMessageRender(Map.of("pattern", 10)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("'pattern' argument must be a string");
  }

  @Test
  void shouldThrowWhenInitBufferSizeStringIsInvalid() {
    // when/then:
    assertThatThrownBy(() -> new PatternLogMessageRender(Map.of(
        "pattern", "%msg",
        "initBufferSize", "invalid")))
        .isInstanceOf(NumberFormatException.class);
  }
}
