package javasabr.rlib.logger.impl.config.loader.json.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javasabr.rlib.common.util.StringUtils;
import javasabr.rlib.logger.api.LoggerLevel;
import org.jspecify.annotations.Nullable;

public record JsonLoggerConfigDto(
    List<LoggerDto> loggers,
    List<RenderDto> renders,
    List<ConsumerDto> consumers) {

  public JsonLoggerConfigDto {
    loggers = loggers == null ? List.of() : List.copyOf(loggers);
    renders = renders == null ? List.of() : List.copyOf(renders);
    consumers = consumers == null ? List.of() : List.copyOf(consumers);
  }
  
  public record LoggerDto(
      String name,
      @Nullable LoggerLevel level,
      @JsonProperty("consumers") Set<String> consumerNames) {

    public LoggerDto {
      consumerNames = consumerNames == null ? Set.of() : Set.copyOf(consumerNames);
    }
  }
  
  public record RenderDto(
      String name,
      RenderType type,
      @JsonProperty("class") @Nullable String className,
      Map<String, Object> args) {

    public RenderDto {
      if (StringUtils.isBlank(name)) {
        throw new IllegalArgumentException("name is blank");
      } else if (type == null) {
        throw new IllegalArgumentException("type is null");
      }
      args = args == null ? Map.of() : Map.copyOf(args);
    }
  }
  
  public enum RenderType {
    SIMPLE,
    PATTERN,
    CUSTOM
  }

  public record ConsumerDto(
      String name,
      ConsumerType type,
      @JsonProperty("render") String renderName,
      @JsonProperty("class") @Nullable String className,
      Map<String, Object> args) {

    public ConsumerDto {
      args = args == null ? Map.of() : Map.copyOf(args);
    }
  }

  public enum ConsumerType {
    CONSOLE,
    CUSTOM
  }
}
