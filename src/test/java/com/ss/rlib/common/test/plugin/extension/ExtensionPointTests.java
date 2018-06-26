package com.ss.rlib.common.test.plugin.extension;

import com.ss.rlib.common.plugin.extension.ExtensionPoint;
import com.ss.rlib.common.util.array.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The list of tests {@link com.ss.rlib.common.plugin.extension.ExtensionPoint}.
 *
 * @author JavaSaBr
 */
public class ExtensionPointTests {

    @Test
    public void testCreation() {

        ExtensionPoint<String> point = new ExtensionPoint<>();
    }
}
