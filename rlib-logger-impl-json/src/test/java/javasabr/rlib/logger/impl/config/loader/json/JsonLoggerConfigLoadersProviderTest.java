package javasabr.rlib.logger.impl.config.loader.json;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Supplier;
import javasabr.rlib.common.util.ResourceClassLoader;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.impl.DefaultLoggerService;
import javasabr.rlib.logger.impl.config.loader.LoggerConfigResolver;
import javasabr.rlib.logger.impl.config.loader.impl.PropertyLoggerConfigLoader;
import javasabr.rlib.logger.impl.config.loader.spi.LoggerConfigLoadersProvider;
import org.junit.jupiter.api.Test;

class JsonLoggerConfigLoadersProviderTest {

  @Test
  void shouldDiscoverJsonProviderViaServiceLoader() {
    // when:
    var providers = ServiceLoader
        .load(LoggerConfigLoadersProvider.class)
        .stream()
        .map(ServiceLoader.Provider::get)
        .toList();

    // then:
    assertThat(providers)
        .anySatisfy(provider -> {
          assertThat(provider).isInstanceOf(JsonLoggerConfigLoadersProvider.class);
          assertThat(provider.getLoggerConfigLoaders().size()).isEqualTo(1);
          assertThat(provider.getLoggerConfigLoaders().get(0)).isInstanceOf(JsonLoggerConfigLoader.class);
        });
  }

  @Test
  void shouldResolveJsonConfigThroughResolverWhenPropertiesMissing() {
    // given:
    var contextClassLoader = new ResourceClassLoader(Map.of(
        JsonLoggerConfigLoader.FILE_TEST, """
            {
              "renders": [{"name":"render1","type":"SIMPLE"}],
              "consumers": [{"name":"consumer1","type":"CONSOLE","render":"render1"}],
              "loggers": [{"name":"ROOT","level":"TRACE","consumers":["consumer1"]}]
            }
            """), Set.of(
        JsonLoggerConfigLoader.FILE_TEST,
        JsonLoggerConfigLoader.FILE_MAIN,
        PropertyLoggerConfigLoader.FILE_TEST,
        PropertyLoggerConfigLoader.FILE_MAIN));

    // when:
    var config = withContextClassLoader(contextClassLoader, LoggerConfigResolver::load);

    // then:
    var loggerService = new DefaultLoggerService(config);
    Logger logger = loggerService.getLogger("example.logger");
    assertThat(logger)
        .returns(true, Logger::traceEnabled)
        .returns(true, Logger::debugEnabled)
        .returns(true, Logger::infoEnabled)
        .returns(true, Logger::warnEnabled)
        .returns(true, Logger::errorEnabled);
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
