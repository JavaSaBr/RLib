package javasabr.rlib.logger.slf4j;

import static org.assertj.core.api.Assertions.assertThat;

import javasabr.rlib.logger.api.LoggerManager;
import org.junit.jupiter.api.Test;

public class Slf4jLoggerTest {

  @Test
  void shouldCreateDefaultLoggerImplementation() {
    var logger = LoggerManager.getLogger(Slf4jLoggerTest.class);
    assertThat(logger).isInstanceOf(Slf4jLogger.class);
  }
}
