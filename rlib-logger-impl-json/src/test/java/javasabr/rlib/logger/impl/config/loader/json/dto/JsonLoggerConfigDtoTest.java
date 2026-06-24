package javasabr.rlib.logger.impl.config.loader.json.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javasabr.rlib.logger.impl.config.loader.json.dto.JsonLoggerConfigDto.ConsumerDto;
import javasabr.rlib.logger.impl.config.loader.json.dto.JsonLoggerConfigDto.ConsumerType;
import javasabr.rlib.logger.impl.config.loader.json.dto.JsonLoggerConfigDto.LoggerDto;
import javasabr.rlib.logger.impl.config.loader.json.dto.JsonLoggerConfigDto.RenderDto;
import javasabr.rlib.logger.impl.config.loader.json.dto.JsonLoggerConfigDto.RenderType;
import org.junit.jupiter.api.Test;

class JsonLoggerConfigDtoTest {

  @Test
  void shouldNormalizeTopLevelNullCollections() {
    // given:
    var dto = new JsonLoggerConfigDto(null, null, null);

    // then:
    assertThat(dto.loggers()).isEmpty();
    assertThat(dto.renders()).isEmpty();
    assertThat(dto.consumers()).isEmpty();
  }

  @Test
  void shouldNormalizeNestedNullCollections() {
    // given:
    var loggerDto = new LoggerDto("logger", null, null);
    var renderDto = new RenderDto("render", RenderType.SIMPLE, null, null);
    var consumerDto = new ConsumerDto("consumer", ConsumerType.CONSOLE, "render", null, null);

    // then:
    assertThat(loggerDto.consumerNames()).isEmpty();
    assertThat(renderDto.args()).isEmpty();
    assertThat(consumerDto.args()).isEmpty();
  }

  @Test
  void shouldCopyInputCollectionsDefensively() {
    // given:
    List<LoggerDto> loggers = List.of(new LoggerDto("logger", null, Set.of()));
    List<RenderDto> renders = List.of(new RenderDto("render", RenderType.SIMPLE, null, Map.of()));
    List<ConsumerDto> consumers = List.of(new ConsumerDto("consumer", ConsumerType.CONSOLE, "render", null, Map.of()));

    // when:
    var dto = new JsonLoggerConfigDto(loggers, renders, consumers);

    // then:
    assertThatThrownBy(() -> dto.loggers().add(new LoggerDto("other", null, Set.of())))
        .isInstanceOf(UnsupportedOperationException.class);
    assertThatThrownBy(() -> dto.renders().add(new RenderDto("other", RenderType.SIMPLE, null, Map.of())))
        .isInstanceOf(UnsupportedOperationException.class);
    assertThatThrownBy(() -> dto.consumers().add(new ConsumerDto("other", ConsumerType.CONSOLE, "render", null, Map.of())))
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void shouldThrowWhenLoggerNameIsBlank() {
    // when/then:
    assertThatThrownBy(() -> new LoggerDto(" ", null, Set.of()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("name is blank");
  }

  @Test
  void shouldThrowWhenRenderNameIsBlank() {
    // when/then:
    assertThatThrownBy(() -> new RenderDto(" ", RenderType.SIMPLE, null, Map.of()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("name is blank");
  }

  @Test
  void shouldThrowWhenRenderTypeIsNull() {
    // when/then:
    assertThatThrownBy(() -> new RenderDto("render", null, null, Map.of()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("type is null");
  }

  @Test
  void shouldThrowWhenConsumerNameIsBlank() {
    // when/then:
    assertThatThrownBy(() -> new ConsumerDto(" ", ConsumerType.CONSOLE, "render", null, Map.of()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("name is blank");
  }

  @Test
  void shouldThrowWhenConsumerTypeIsNull() {
    // when/then:
    assertThatThrownBy(() -> new ConsumerDto("consumer", null, "render", null, Map.of()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("type is null");
  }
}
