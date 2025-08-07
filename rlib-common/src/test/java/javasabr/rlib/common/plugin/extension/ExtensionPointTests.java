package javasabr.rlib.common.plugin.extension;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The list of tests {@link ExtensionPoint}.
 *
 * @author JavaSaBr
 */
public class ExtensionPointTests {

    @Test
    void registerExtensionTest() {

        var point = new ExtensionPoint<String>();
        point.register("a", "b");
        point.register("c");

        Assertions.assertIterableEquals(Arrays.asList("a", "b", "c"), point.getExtensions());
    }

    @Test
    void registerExtensionPointTest() {

        var manager = ExtensionPointManager.getInstance();
        manager.addExtension("Test1", 5);
        manager.addExtension("Test1", 6, 7);

        var test2 = ExtensionPointManager.<Integer>register("Test2");
        test2.register(1, 2);
        test2.register(3);

        var forTest1 = manager.<Integer>getExtensionPoint("Test1");
        var forTest2 = manager.<Integer>getExtensionPoint("Test2");

        Assertions.assertIterableEquals(Arrays.asList(5, 6, 7), forTest1.getExtensions());
        Assertions.assertIterableEquals(Arrays.asList(1, 2, 3), forTest2.getExtensions());
    }
}
