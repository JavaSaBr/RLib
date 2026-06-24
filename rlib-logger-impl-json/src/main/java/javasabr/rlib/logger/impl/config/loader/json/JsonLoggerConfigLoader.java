package javasabr.rlib.logger.impl.config.loader.json;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javasabr.rlib.common.util.StringUtils;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.config.LoggerConfig;
import javasabr.rlib.logger.impl.config.consumer.LogMessageConsumer;
import javasabr.rlib.logger.impl.config.consumer.impl.ConsoleMessageConsumer;
import javasabr.rlib.logger.impl.config.consumer.impl.CustomLogMessageConsumer;
import javasabr.rlib.logger.impl.config.impl.LoggerConfigBuilder;
import javasabr.rlib.logger.impl.config.loader.LoggerConfigLoader;
import javasabr.rlib.logger.impl.config.loader.json.dto.JsonLoggerConfigDto;
import javasabr.rlib.logger.impl.config.loader.json.dto.JsonLoggerConfigDto.ConsumerDto;
import javasabr.rlib.logger.impl.config.loader.json.dto.JsonLoggerConfigDto.LoggerDto;
import javasabr.rlib.logger.impl.config.loader.json.dto.JsonLoggerConfigDto.RenderDto;
import javasabr.rlib.logger.impl.config.render.LogMessageRender;
import javasabr.rlib.logger.impl.config.render.impl.CustomLogMessageRender;
import javasabr.rlib.logger.impl.config.render.impl.SimpleLogMessageRender;
import javasabr.rlib.logger.impl.config.render.impl.pattern.PatternLogMessageRender;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import tools.jackson.databind.ObjectMapper;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonLoggerConfigLoader implements LoggerConfigLoader {

  public static final String FILE_MAIN = "rlib.logger.json";
  public static final String FILE_TEST = "rlib.logger-test.json";
  
  ObjectMapper objectMapper = new ObjectMapper();
  
  @Override
  public Optional<LoggerConfig> tryToLoad() {
    ClassLoader classLoader = Thread
        .currentThread()
        .getContextClassLoader();
    InputStream configStream = classLoader.getResourceAsStream(FILE_TEST);
    if (configStream == null) {
      configStream = classLoader.getResourceAsStream(FILE_MAIN);
    }
    if (configStream == null) {
      return Optional.empty();
    } else {
      return Optional.of(load(configStream));
    }
  }

  @Override
  public int order() {
    return LoggerConfigLoader.ORDER_NORMAL;
  }

  private LoggerConfig load(InputStream inputStream) {
    try (var in = inputStream) {
      JsonLoggerConfigDto configDto = objectMapper.readValue(in, JsonLoggerConfigDto.class);
      return load(configDto);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private LoggerConfig load(JsonLoggerConfigDto configDto) {

    var builder = new LoggerConfigBuilder();
    
    List<CreatedRender> createdRenders = configDto
        .renders()
        .stream()
        .map(this::createRender)
        .toList();

    Map<String, LogMessageRender> renderMap = createdRenders
        .stream()
        .collect(Collectors.toMap(CreatedRender::name, CreatedRender::render));

    List<CreatedConsumer> createdConsumers = configDto
        .consumers()
        .stream()
        .map(consumerDto -> createConsumer(renderMap, consumerDto))
        .toList();

    Map<String, LogMessageConsumer> consumerMap = createdConsumers
        .stream()
        .collect(Collectors.toMap(CreatedConsumer::name, CreatedConsumer::consumer));

    for (LoggerDto loggerDto : configDto.loggers()) {
      if (loggerDto.level() != null) {
        builder.registerLoggerLevel(loggerDto.name(), loggerDto.level());
      }
      Set<String> consumerNames = loggerDto.consumerNames();
      if (!consumerNames.isEmpty()) {
        for (String consumerName : consumerNames) {
          LogMessageConsumer consumer = consumerMap.get(consumerName);
          if (consumer == null) {
            throw new RuntimeException("Consumer " + consumerName + " not found");
          }
          LoggerLevel level = loggerDto.level();
          if (level == null) {
            level = LoggerLevel.TRACE;
          }
          builder.registerLoggerConsumer(loggerDto.name(), level, consumer);
        }
      }
    }
    
    return builder.build();
  }

  CreatedRender createRender(RenderDto renderDto) {
    LogMessageRender render = switch (renderDto.type()) {
      case SIMPLE -> new SimpleLogMessageRender();
      case PATTERN -> new PatternLogMessageRender(renderDto.args());
      case CUSTOM -> createCustomRender(renderDto);
    };
    return new CreatedRender(renderDto.name(), render);
  }

  LogMessageRender createCustomRender(RenderDto renderDto) {
    String className = renderDto.className();
    if (StringUtils.isBlank(className)) {
      throw new IllegalArgumentException("'class' attribute is required for custom render");
    }
    ClassLoader classLoader = Thread
        .currentThread()
        .getContextClassLoader();
    try {
      var targetClass = (Class<? extends CustomLogMessageRender>) classLoader.loadClass(className);
      Constructor<? extends CustomLogMessageRender> constructor = targetClass.getDeclaredConstructor(Map.class);
      return constructor.newInstance(renderDto.args());
    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
             IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  CreatedConsumer createConsumer(Map<String, LogMessageRender> renderMap, ConsumerDto consumerDto) {
    LogMessageRender render = renderMap.get(consumerDto.renderName());
    if (render == null) {
      throw new IllegalArgumentException("Unknown render with name: " + consumerDto.renderName());
    }
    LogMessageConsumer consumer = switch (consumerDto.type()) {
      case CONSOLE -> new ConsoleMessageConsumer(render);
      case CUSTOM -> createCustomConsumer(render, consumerDto);
    };
    return new CreatedConsumer(consumerDto.name(), consumer);
  }

  LogMessageConsumer createCustomConsumer(LogMessageRender render, ConsumerDto consumerDto) {
    String className = consumerDto.className();
    if (StringUtils.isBlank(className)) {
      throw new IllegalArgumentException("'class' attribute is required for custom consumer");
    }
    ClassLoader classLoader = Thread
        .currentThread()
        .getContextClassLoader();
    try {
      var targetClass = (Class<? extends CustomLogMessageConsumer>) classLoader.loadClass(className);
      Constructor<? extends CustomLogMessageConsumer> constructor = targetClass
          .getDeclaredConstructor(LogMessageRender.class, Map.class);
      return constructor.newInstance(render, consumerDto.args());
    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
             IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private record CreatedRender(String name, LogMessageRender render) {}
  private record CreatedConsumer(String name, LogMessageConsumer consumer) {}
}
