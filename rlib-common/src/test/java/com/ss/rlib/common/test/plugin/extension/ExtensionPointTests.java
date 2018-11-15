package com.ss.rlib.common.test.plugin.extension;

import com.ss.rlib.common.plugin.extension.ExtensionPoint;
import com.ss.rlib.common.plugin.extension.ExtensionPointManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * The list of tests {@link com.ss.rlib.common.plugin.extension.ExtensionPoint}.
 *
 * @author JavaSaBr
 */
public class ExtensionPointTests {

    @Test
    public void testRegisterExtensions() {

        ExtensionPoint<String> point = new ExtensionPoint<>();
        point.register("a", "b");
        point.register("c");

        Assertions.assertIterableEquals(Arrays.asList("a", "b", "c"), point.getExtensions());
    }

    @Test
    public void testRegisterExtensionPoint() {

        ExtensionPointManager manager = ExtensionPointManager.getInstance();
        manager.addExtension("Test1", 5);
        manager.addExtension("Test1", 6, 7);

        ExtensionPoint<Integer> test2 = ExtensionPointManager.register("Test2");
        test2.register(1, 2);
        test2.register(3);

        ExtensionPoint<Integer> forTest1 = manager.getExtensionPoint("Test1");
        ExtensionPoint<Integer> forTest2 = manager.getExtensionPoint("Test2");

        Assertions.assertIterableEquals(Arrays.asList(5, 6, 7), forTest1.getExtensions());
        Assertions.assertIterableEquals(Arrays.asList(1, 2, 3), forTest2.getExtensions());
    }
}
