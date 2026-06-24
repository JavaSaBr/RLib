package javasabr.rlib.logger.impl.config.loader.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.Supplier;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.UnsafeArray;
import javasabr.rlib.common.util.ResourceClassLoader;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.DefaultLogger;
import javasabr.rlib.logger.impl.DefaultLoggerService;
import javasabr.rlib.logger.impl.config.LoggerConfig;
import javasabr.rlib.logger.impl.config.consumer.LogMessageConsumer;
import javasabr.rlib.logger.impl.config.consumer.impl.ConsoleMessageConsumer;
import javasabr.rlib.logger.impl.config.impl.DefaultLoggerConfig;
import javasabr.rlib.logger.impl.config.render.impl.SimpleLogMessageRender;
import javasabr.rlib.logger.impl.config.render.impl.pattern.PatternLogMessageRender;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.DateTimePatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.LevelPatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.MessagePatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.PatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.StringPatternRenderNode;
import org.junit.jupiter.api.Test;

class PropertyLoggerConfigLoaderTest {
  
  @Test
  void shouldLoadLoggerConfigCorrectly() throws IOException {
    // given:
    var loader = new PropertyLoggerConfigLoader();
    var in = PropertyLoggerConfigLoaderTest.class.getResourceAsStream("/property-loader-test-1.properties");
    var properties = new Properties();
    properties.load(in);
    
    // when:
    LoggerConfig loggerConfig = loader.loadFromProperties(properties);
    
    // then:
    assertThat(loggerConfig)
        .isInstanceOf(DefaultLoggerConfig.class);
    
    // when:
    var defaultLoggerConfig = (DefaultLoggerConfig) loggerConfig;
    var loggerService = new DefaultLoggerService(loggerConfig);
    DefaultLogger logger = loggerService.getLogger(
        "javasabr.rlib.logger.impl.config.loader.impl.PropertyLoggerConfigLoaderTest");
    UnsafeArray<LogMessageConsumer> consumers = defaultLoggerConfig.resolveConsumers(
        logger,
        LoggerLevel.TRACE);

    // then:
    assertThat(consumers.size())
        .isEqualTo(1);
    assertThat(consumers.get(0))
        .isInstanceOf(ConsoleMessageConsumer.class)
        .extracting("logMessageRender")
        .isInstanceOf(PatternLogMessageRender.class)
        .extracting("renderNodes")
        .extracting(object ->  (UnsafeArray<PatternRenderNode>) object)
        .returns(5, Array::size)
        .returns(new DateTimePatternRenderNode("d.MM.yyyy HH:mm:ss:SSS"), nodes -> nodes.get(0))
        .returns(new StringPatternRenderNode(" "), nodes -> nodes.get(1))
        .returns(LevelPatternRenderNode.class, nodes -> nodes.get(2).getClass())
        .returns(new StringPatternRenderNode(" : "), nodes -> nodes.get(3))
        .returns(MessagePatternRenderNode.class, nodes -> nodes.get(4).getClass());
    
    // when:
    DefaultLogger loggerA = loggerService.getLogger(
        "javasabr.rlib.logger.impl.config.loader.impl.PropertyLoggerConfigLoaderTest.A");
    DefaultLogger loggerB = loggerService.getLogger(
        "javasabr.rlib.logger.impl.config.loader.impl.PropertyLoggerConfigLoaderTest.B");
    DefaultLogger loggerC = loggerService.getLogger(
        "javasabr.rlib.logger.impl.config.loader.impl.PropertyLoggerConfigLoaderTest.C");
    DefaultLogger loggerD = loggerService.getLogger(
        "javasabr.rlib.logger.impl.config.loader.impl.PropertyLoggerConfigLoaderTest.D");
    
    // then:
    assertThat(loggerA)
        .returns(false, Logger::traceEnabled)
        .returns(false, Logger::debugEnabled)
        .returns(true, Logger::infoEnabled)
        .returns(true, Logger::warnEnabled)
        .returns(true, Logger::errorEnabled);
    assertThat(loggerB)
        .returns(false, Logger::traceEnabled)
        .returns(true, Logger::debugEnabled)
        .returns(true, Logger::infoEnabled)
        .returns(true, Logger::warnEnabled)
        .returns(true, Logger::errorEnabled);
    assertThat(loggerC)
        .returns(true, Logger::traceEnabled)
        .returns(true, Logger::debugEnabled)
        .returns(true, Logger::infoEnabled)
        .returns(true, Logger::warnEnabled)
        .returns(true, Logger::errorEnabled);
    assertThat(loggerD)
        .returns(false, Logger::traceEnabled)
        .returns(false, Logger::debugEnabled)
        .returns(false, Logger::infoEnabled)
        .returns(false, Logger::warnEnabled)
        .returns(true, Logger::errorEnabled);
  }

  @Test
  void shouldPreferTestPropertiesOverMainProperties() {
    // given:
    var loader = new PropertyLoggerConfigLoader();
    var contextClassLoader = new ResourceClassLoader(Map.of(
        PropertyLoggerConfigLoader.FILE_TEST, "logger.level.ROOT=TRACE",
        PropertyLoggerConfigLoader.FILE_MAIN, "logger.level.ROOT=ERROR"), Set.of(
        PropertyLoggerConfigLoader.FILE_TEST,
        PropertyLoggerConfigLoader.FILE_MAIN));

    // when:
    Optional<LoggerConfig> loadedConfig = withContextClassLoader(contextClassLoader, loader::tryToLoad);

    // then:
    assertThat(loadedConfig).isPresent();

    // when:
    var loggerService = new DefaultLoggerService(loadedConfig.orElseThrow());
    DefaultLogger logger = loggerService.getLogger("example.logger");

    // then:
    assertThat(logger)
        .returns(true, Logger::traceEnabled)
        .returns(true, Logger::debugEnabled)
        .returns(true, Logger::infoEnabled)
        .returns(true, Logger::warnEnabled)
        .returns(true, Logger::errorEnabled);
  }

  @Test
  void shouldFallbackToMainPropertiesWhenTestMissing() {
    // given:
    var loader = new PropertyLoggerConfigLoader();
    var contextClassLoader = new ResourceClassLoader(Map.of(
        PropertyLoggerConfigLoader.FILE_MAIN, "logger.level.ROOT=ERROR"), Set.of(
        PropertyLoggerConfigLoader.FILE_TEST,
        PropertyLoggerConfigLoader.FILE_MAIN));

    // when:
    Optional<LoggerConfig> loadedConfig = withContextClassLoader(contextClassLoader, loader::tryToLoad);

    // then:
    assertThat(loadedConfig).isPresent();

    // when:
    var loggerService = new DefaultLoggerService(loadedConfig.orElseThrow());
    DefaultLogger logger = loggerService.getLogger("example.logger");

    // then:
    assertThat(logger)
        .returns(false, Logger::traceEnabled)
        .returns(false, Logger::debugEnabled)
        .returns(false, Logger::infoEnabled)
        .returns(false, Logger::warnEnabled)
        .returns(true, Logger::errorEnabled);
  }

  @Test
  void shouldReturnEmptyWhenNoPropertiesFound() {
    // given:
    var loader = new PropertyLoggerConfigLoader();
    var contextClassLoader = new ResourceClassLoader(Map.of(), Set.of(
        PropertyLoggerConfigLoader.FILE_TEST,
        PropertyLoggerConfigLoader.FILE_MAIN));

    // when:
    Optional<LoggerConfig> loadedConfig = withContextClassLoader(contextClassLoader, loader::tryToLoad);

    // then:
    assertThat(loadedConfig).isEmpty();
  }

  @Test
  void shouldUseRootLevelForUnconfiguredLogger() {
    // given:
    var loader = new PropertyLoggerConfigLoader();
    var contextClassLoader = new ResourceClassLoader(Map.of(
        PropertyLoggerConfigLoader.FILE_MAIN,
        "logger.level.ROOT=WARNING\nlogger.level.configured.logger=TRACE"), Set.of(
        PropertyLoggerConfigLoader.FILE_TEST,
        PropertyLoggerConfigLoader.FILE_MAIN));

    // when:
    Optional<LoggerConfig> loadedConfig = withContextClassLoader(contextClassLoader, loader::tryToLoad);

    // then:
    assertThat(loadedConfig).isPresent();

    // when:
    var loggerService = new DefaultLoggerService(loadedConfig.orElseThrow());
    DefaultLogger unconfiguredLogger = loggerService.getLogger("unknown.logger");
    DefaultLogger configuredLogger = loggerService.getLogger("configured.logger");

    // then:
    assertThat(unconfiguredLogger)
        .returns(false, Logger::traceEnabled)
        .returns(false, Logger::debugEnabled)
        .returns(false, Logger::infoEnabled)
        .returns(true, Logger::warnEnabled)
        .returns(true, Logger::errorEnabled);
    assertThat(configuredLogger)
        .returns(true, Logger::traceEnabled)
        .returns(true, Logger::debugEnabled)
        .returns(true, Logger::infoEnabled)
        .returns(true, Logger::warnEnabled)
        .returns(true, Logger::errorEnabled);
  }

  @Test
  void shouldUseSimpleMessageRenderWhenPatternIsAbsent() {
    // given:
    var loader = new PropertyLoggerConfigLoader();
    var properties = new Properties();
    properties.setProperty("logger.level.ROOT", "INFO");

    // when:
    LoggerConfig loggerConfig = loader.loadFromProperties(properties);

    // then:
    assertThat(loggerConfig)
        .isInstanceOf(DefaultLoggerConfig.class);

    // when:
    var defaultLoggerConfig = (DefaultLoggerConfig) loggerConfig;
    var loggerService = new DefaultLoggerService(loggerConfig);
    DefaultLogger logger = loggerService.getLogger("example.logger");
    UnsafeArray<LogMessageConsumer> consumers = defaultLoggerConfig.resolveConsumers(
        logger,
        LoggerLevel.TRACE);

    // then:
    assertThat(consumers.size())
        .isEqualTo(1);
    assertThat(consumers.get(0))
        .isInstanceOf(ConsoleMessageConsumer.class)
        .extracting("logMessageRender")
        .isInstanceOf(SimpleLogMessageRender.class);
  }

  @Test
  void shouldThrowExceptionWhenLevelValueIsInvalid() {
    // given:
    var loader = new PropertyLoggerConfigLoader();
    var properties = new Properties();
    properties.setProperty("logger.level.ROOT", "INF0");

    // when/then:
    assertThatThrownBy(() -> loader.loadFromProperties(properties))
        .isInstanceOf(IllegalArgumentException.class);
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
}
