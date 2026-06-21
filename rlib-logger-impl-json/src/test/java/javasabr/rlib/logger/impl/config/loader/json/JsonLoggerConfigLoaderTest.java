package javasabr.rlib.logger.impl.config.loader.json;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Optional;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.impl.DefaultLoggerService;
import javasabr.rlib.logger.impl.config.LoggerConfig;
import javasabr.rlib.logger.impl.config.consumer.impl.CustomLogMessageConsumer;
import javasabr.rlib.logger.impl.config.render.LogMessageRender;
import javasabr.rlib.logger.impl.config.render.impl.CustomLogMessageRender;
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
}
