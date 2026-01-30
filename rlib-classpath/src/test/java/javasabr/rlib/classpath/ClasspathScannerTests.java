package javasabr.rlib.classpath;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.impl.AbstractArray;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerManager;
import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
public class ClasspathScannerTests {

  static {
    LoggerManager.configureDefault(LoggerLevel.DEBUG, true);
  }

  @Test
  void testSystemClasspathScanner() {

    ClassPathScanner scanner = ClassPathScannerFactory.newDefaultScanner();
    scanner.useSystemClassPath(true);
    scanner.scan();

    Array<Class<Collection>> implementations = scanner.findImplementations(Collection.class);

    assertThat(implementations.isEmpty()).isFalse();

    Array<Class<AbstractArray>> inherited = scanner.findInherited(AbstractArray.class);

    assertThat(inherited.isEmpty()).isFalse();
  }
}
