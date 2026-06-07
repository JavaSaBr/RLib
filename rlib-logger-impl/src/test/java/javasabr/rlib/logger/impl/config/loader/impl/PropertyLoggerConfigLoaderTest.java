package javasabr.rlib.logger.impl.config.loader.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Properties;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.UnsafeArray;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.DefaultLogger;
import javasabr.rlib.logger.impl.DefaultLoggerService;
import javasabr.rlib.logger.impl.config.LoggerConfig;
import javasabr.rlib.logger.impl.config.consumer.LogMessageConsumer;
import javasabr.rlib.logger.impl.config.consumer.impl.ConsoleMessageConsumer;
import javasabr.rlib.logger.impl.config.impl.DefaultLoggerConfig;
import javasabr.rlib.logger.impl.config.render.impl.pattern.PatternLogMessageRender;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.DateTimePatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.LevelPatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.MessagePatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.PatternRenderNode;
import javasabr.rlib.logger.impl.config.render.impl.pattern.node.StringPatternRenderNode;
import org.junit.jupiter.api.Test;

public class PropertyLoggerConfigLoaderTest {
  
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
}
