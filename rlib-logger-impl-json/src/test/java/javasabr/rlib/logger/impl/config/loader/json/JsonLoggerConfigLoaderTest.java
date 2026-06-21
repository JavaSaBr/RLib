package javasabr.rlib.logger.impl.config.loader.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.DefaultLoggerService;
import javasabr.rlib.logger.impl.config.LoggerConfig;
import javasabr.rlib.logger.impl.config.consumer.impl.CustomLogMessageConsumer;
import javasabr.rlib.logger.impl.config.render.LogMessageRender;
import javasabr.rlib.logger.impl.config.render.impl.CustomLogMessageRender;
import javasabr.rlib.logger.impl.config.render.impl.SimpleLogMessageRender;
import javasabr.rlib.logger.impl.config.loader.json.dto.JsonLoggerConfigDto.ConsumerDto;
import javasabr.rlib.logger.impl.config.loader.json.dto.JsonLoggerConfigDto.ConsumerType;
import javasabr.rlib.logger.impl.config.loader.json.dto.JsonLoggerConfigDto.RenderDto;
import javasabr.rlib.logger.impl.config.loader.json.dto.JsonLoggerConfigDto.RenderType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class JsonLoggerConfigLoaderTest {

  private static final ThreadLocal<MutableArray<String>> CONSUMED_MESSAGES = ThreadLocal
      .withInitial(() -> ArrayFactory.mutableArray(String.class));
  
  @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
  public static class TestCustomLogMessageConsumer extends CustomLogMessageConsumer {

    int consumerArg1;
    String consumerArg2;
    
    public TestCustomLogMessageConsumer(LogMessageRender render, Map<String, Object> args) {
      super(render, args);
      consumerArg1 = (Integer) args.get("consumerArg1");
      consumerArg2 = (String) args.get("consumerArg2");
    }

    @Override
    public void consume(LoggerLevel level, Logger logger, String message) {
      CONSUMED_MESSAGES.get().add("[%s][%s]->%s->%s".formatted(
          consumerArg1,
          consumerArg2,
          logger.shortName(),
          render.render(level, logger, message)));
    }
  }

  @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
  public static class WrongCustomLogMessageRender extends CustomLogMessageRender {

    public WrongCustomLogMessageRender() {
      super(Map.of());
    }

    @Override
    public String render(LoggerLevel level, Logger logger, String message) {
      return message;
    }
  }

  @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
  public static class WrongCustomLogMessageConsumer extends CustomLogMessageConsumer {

    public WrongCustomLogMessageConsumer(LogMessageRender render, String unsupportedArg) {
      super(render, Map.of());
    }

    @Override
    public void consume(LoggerLevel level, Logger logger, String message) {
    }
  }

  @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
  public static class TestCustomLogMessageRender extends CustomLogMessageRender {

    int renderArg1;
    String renderArg2;
    
    public TestCustomLogMessageRender(Map<String, Object> args) {
      super(args);
      renderArg1 = (Integer) args.get("renderArg1");
      renderArg2 = (String) args.get("renderArg2");
    }

    @Override
    public String render(LoggerLevel level, Logger logger, String message) {
      return "[%s][%s]->%s".formatted(renderArg1, renderArg2, message);
    }
  }
  
  @AfterEach
  void cleanup() {
    CONSUMED_MESSAGES.get().clear();
  }
  
  @Test
  void shouldLoadConfigCorrectly() {
    // given:
    var loader = new JsonLoggerConfigLoader();
    MutableArray<String> receivedMessages = CONSUMED_MESSAGES.get();

    //when:
    Optional<LoggerConfig> loadedLoggerConfig = loader.tryToLoad();
    
    // then:
    assertThat(loadedLoggerConfig).isPresent();
    
    // when:
    var service = new DefaultLoggerService(loadedLoggerConfig.get());
    Logger logger1 = service.getLogger(
        "javasabr.rlib.logger.impl.config.loader.json.JsonLoggerConfigLoaderTest.logger1");
    logger1.error("test error 1");
    logger1.warn("test warning 1");
    logger1.info("test info 1");
    logger1.debug("test debug 1");
    logger1.trace("test trace 1");
    
    // then:
    assertThat(receivedMessages)
        .hasSize(2)
        .containsExactly(
            "[66][arg2]->logger1->[55][arg2]->test error 1",
            "[66][arg2]->logger1->[55][arg2]->test warning 1");
    
    // when:
    receivedMessages.clear();
    Logger logger2 = service.getLogger(
        "javasabr.rlib.logger.impl.config.loader.json.JsonLoggerConfigLoaderTest.logger2");
    logger2.error("test error 2");
    logger2.warn("test warning 2");
    logger2.info("test info 2");
    logger2.debug("test debug 2");
    logger2.trace("test trace 2");

    // then:
    assertThat(receivedMessages)
        .hasSize(4)
        .containsExactly(
            "[66][arg2]->logger2->[55][arg2]->test error 2",
            "[66][arg2]->logger2->[55][arg2]->test warning 2",
            "[66][arg2]->logger2->[55][arg2]->test info 2",
            "[66][arg2]->logger2->[55][arg2]->test debug 2");
    
    // when:
    receivedMessages.clear();
    Logger logger3 = service.getLogger(
        "javasabr.rlib.logger.impl.config.loader.json.JsonLoggerConfigLoaderTest.logger3");
    logger3.error("test error 3");
    logger3.warn("test warning 3");
    logger3.info("test info 3");
    logger3.debug("test debug 3");
    logger3.trace("test trace 3");

    // then:
    assertThat(receivedMessages)
        .hasSize(1)
        .containsExactly(
            "[66][arg2]->logger3->[55][arg2]->test error 3");

    // when:
    receivedMessages.clear();
    Logger rootLogger = service.getRootLogger();
    rootLogger.error("test error 4");
    rootLogger.warn("test warning 4");
    rootLogger.info("test info 4");
    rootLogger.debug("test debug 4");
    rootLogger.trace("test trace 4");
    
    // then:
    assertThat(receivedMessages)
        .hasSize(1)
        .containsExactly(
            "[66][arg2]->ROOT->[55][arg2]->test error 4");
  }

  @Test
  void shouldPreferTestJsonOverMainJson() {
    // given:
    var loader = new JsonLoggerConfigLoader();
    var contextClassLoader = new ResourceClassLoader(Map.of(
        JsonLoggerConfigLoader.FILE_TEST, buildRootOnlyConfigJson("TRACE"),
        JsonLoggerConfigLoader.FILE_MAIN, buildRootOnlyConfigJson("ERROR")));

    // when:
    Optional<LoggerConfig> loadedConfig = withContextClassLoader(contextClassLoader, loader::tryToLoad);

    // then:
    assertThat(loadedConfig).isPresent();

    // when:
    var loggerService = new DefaultLoggerService(loadedConfig.orElseThrow());
    Logger logger = loggerService.getLogger("example.logger");

    // then:
    assertThat(logger)
        .returns(true, Logger::traceEnabled)
        .returns(true, Logger::debugEnabled)
        .returns(true, Logger::infoEnabled)
        .returns(true, Logger::warnEnabled)
        .returns(true, Logger::errorEnabled);
  }

  @Test
  void shouldFallbackToMainJsonWhenTestMissing() {
    // given:
    var loader = new JsonLoggerConfigLoader();
    var contextClassLoader = new ResourceClassLoader(Map.of(
        JsonLoggerConfigLoader.FILE_MAIN, buildRootOnlyConfigJson("ERROR")));

    // when:
    Optional<LoggerConfig> loadedConfig = withContextClassLoader(contextClassLoader, loader::tryToLoad);

    // then:
    assertThat(loadedConfig).isPresent();

    // when:
    var loggerService = new DefaultLoggerService(loadedConfig.orElseThrow());
    Logger logger = loggerService.getLogger("example.logger");

    // then:
    assertThat(logger)
        .returns(false, Logger::traceEnabled)
        .returns(false, Logger::debugEnabled)
        .returns(false, Logger::infoEnabled)
        .returns(false, Logger::warnEnabled)
        .returns(true, Logger::errorEnabled);
  }

  @Test
  void shouldReturnEmptyWhenNoJsonConfigFound() {
    // given:
    var loader = new JsonLoggerConfigLoader();
    var contextClassLoader = new ResourceClassLoader(Map.of());

    // when:
    Optional<LoggerConfig> loadedConfig = withContextClassLoader(contextClassLoader, loader::tryToLoad);

    // then:
    assertThat(loadedConfig).isEmpty();
  }

  @Test
  void shouldThrowWhenJsonContentIsMalformed() {
    // given:
    var loader = new JsonLoggerConfigLoader();
    var contextClassLoader = new ResourceClassLoader(Map.of(
        JsonLoggerConfigLoader.FILE_TEST, "{ malformed-json"));

    // when/then:
    assertThatThrownBy(() -> withContextClassLoader(contextClassLoader, loader::tryToLoad))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  void shouldThrowWhenConsumerReferencesUnknownRender() {
    // given:
    var loader = new JsonLoggerConfigLoader();
    var contextClassLoader = new ResourceClassLoader(Map.of(
        JsonLoggerConfigLoader.FILE_TEST, """
            {
              "renders": [{"name":"render1","type":"SIMPLE"}],
              "consumers": [{"name":"consumer1","type":"CONSOLE","render":"unknown-render"}],
              "loggers": [{"name":"ROOT","level":"INFO","consumers":["consumer1"]}]
            }
            """));

    // when/then:
    assertThatThrownBy(() -> withContextClassLoader(contextClassLoader, loader::tryToLoad))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Unknown render with name");
  }

  @Test
  void shouldThrowWhenCustomRenderClassIsMissing() {
    // given:
    var loader = new JsonLoggerConfigLoader();
    var contextClassLoader = new ResourceClassLoader(Map.of(
        JsonLoggerConfigLoader.FILE_TEST, """
            {
              "renders": [{"name":"render1","type":"CUSTOM"}]
            }
            """));

    // when/then:
    assertThatThrownBy(() -> withContextClassLoader(contextClassLoader, loader::tryToLoad))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("'class' attribute is required for custom render");
  }

  @Test
  void shouldThrowWhenCustomConsumerClassIsMissing() {
    // given:
    var loader = new JsonLoggerConfigLoader();
    var contextClassLoader = new ResourceClassLoader(Map.of(
        JsonLoggerConfigLoader.FILE_TEST, """
            {
              "renders": [{"name":"render1","type":"SIMPLE"}],
              "consumers": [{"name":"consumer1","type":"CUSTOM","render":"render1"}]
            }
            """));

    // when/then:
    assertThatThrownBy(() -> withContextClassLoader(contextClassLoader, loader::tryToLoad))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("'class' attribute is required for custom consumer");
  }

  @Test
  void shouldThrowWhenCustomRenderClassCannotBeFound() {
    // given:
    var loader = new JsonLoggerConfigLoader();
    var renderDto = new RenderDto(
        "render1",
        RenderType.CUSTOM,
        "example.missing.CustomRender",
        Map.of());

    // when/then:
    assertThatThrownBy(() -> loader.createCustomRender(renderDto))
        .isInstanceOf(RuntimeException.class)
        .hasCauseInstanceOf(ClassNotFoundException.class);
  }

  @Test
  void shouldThrowWhenCustomConsumerClassCannotBeFound() {
    // given:
    var loader = new JsonLoggerConfigLoader();
    var consumerDto = new ConsumerDto(
        "consumer1",
        ConsumerType.CUSTOM,
        "render1",
        "example.missing.CustomConsumer",
        Map.of());

    // when/then:
    assertThatThrownBy(() -> loader.createCustomConsumer(new SimpleLogMessageRender(), consumerDto))
        .isInstanceOf(RuntimeException.class)
        .hasCauseInstanceOf(ClassNotFoundException.class);
  }

  @Test
  void shouldThrowWhenCustomRenderConstructorSignatureIsInvalid() {
    // given:
    var loader = new JsonLoggerConfigLoader();
    var renderDto = new RenderDto(
        "render1",
        RenderType.CUSTOM,
        WrongCustomLogMessageRender.class.getName(),
        Map.of());

    // when/then:
    assertThatThrownBy(() -> loader.createCustomRender(renderDto))
        .isInstanceOf(RuntimeException.class)
        .hasCauseInstanceOf(NoSuchMethodException.class);
  }

  @Test
  void shouldThrowWhenCustomConsumerConstructorSignatureIsInvalid() {
    // given:
    var loader = new JsonLoggerConfigLoader();
    var consumerDto = new ConsumerDto(
        "consumer1",
        ConsumerType.CUSTOM,
        "render1",
        WrongCustomLogMessageConsumer.class.getName(),
        Map.of());

    // when/then:
    assertThatThrownBy(() -> loader.createCustomConsumer(new SimpleLogMessageRender(), consumerDto))
        .isInstanceOf(RuntimeException.class)
        .hasCauseInstanceOf(NoSuchMethodException.class);
  }

  private static String buildRootOnlyConfigJson(String level) {
    return """
        {
          "renders": [{"name":"render1","type":"SIMPLE"}],
          "consumers": [{"name":"consumer1","type":"CONSOLE","render":"render1"}],
          "loggers": [{"name":"ROOT","level":"%s","consumers":["consumer1"]}]
        }
        """.formatted(level);
  }

  private static <T> T withContextClassLoader(ClassLoader contextClassLoader, Supplier<T> action) {
    Thread currentThread = Thread.currentThread();
    ClassLoader previousClassLoader = currentThread.getContextClassLoader();
    try {
      currentThread.setContextClassLoader(contextClassLoader);
      return action.get();
    } finally {
      currentThread.setContextClassLoader(previousClassLoader);
    }
  }

  private static class ResourceClassLoader extends ClassLoader {

    private final Map<String, byte[]> resources;

    private ResourceClassLoader(Map<String, String> resources) {
      super(Thread
          .currentThread()
          .getContextClassLoader());
      this.resources = resources
          .entrySet()
          .stream()
          .collect(Collectors.toUnmodifiableMap(
              Map.Entry::getKey,
              entry -> entry.getValue().getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public InputStream getResourceAsStream(String name) {
      byte[] loaded = resources.get(name);
      if (loaded != null) {
        return new ByteArrayInputStream(loaded);
      }
      if (JsonLoggerConfigLoader.FILE_TEST.equals(name) || JsonLoggerConfigLoader.FILE_MAIN.equals(name)) {
        return null;
      }
      return super.getResourceAsStream(name);
    }
  }
}
