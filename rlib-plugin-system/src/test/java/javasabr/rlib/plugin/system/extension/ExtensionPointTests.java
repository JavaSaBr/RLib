package javasabr.rlib.plugin.system.extension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
public class ExtensionPointTests {

  @Test
  void registerExtensionTest() {
    var point = new ExtensionPoint<String>();
    point.register("a", "b");
    point.register("c");

    assertThat(point.extensions())
        .isEqualTo(Arrays.asList("a", "b", "c"));
  }

  @Test
  void registerExtensionPointTest() {
    var manager = new ExtensionPointManager();
    manager.addExtension("Test1", 5);
    manager.addExtension("Test1", 6, 7);

    var test2 = manager.<Integer>create("Test2");
    test2.register(1, 2);
    test2.register(3);

    var forTest1 = manager.<Integer>getOrCreateExtensionPoint("Test1");
    var forTest2 = manager.<Integer>getOrCreateExtensionPoint("Test2");

    assertThat(forTest1.extensions())
        .isEqualTo(Arrays.asList(5, 6, 7));
    assertThat(forTest2.extensions())
        .isEqualTo(Arrays.asList(1, 2, 3));
  }
}
