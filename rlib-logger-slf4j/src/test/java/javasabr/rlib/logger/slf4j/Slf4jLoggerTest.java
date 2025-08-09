package javasabr.rlib.logger.slf4j;

import javasabr.rlib.logger.api.LoggerManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Slf4jLoggerTest {

  @Test
  void shouldCreateDefaultLoggerImplementation() {

    var logger = LoggerManager.getLogger(Slf4jLoggerTest.class);

    Assertions.assertTrue(logger instanceof Slf4jLogger);
  }
}
