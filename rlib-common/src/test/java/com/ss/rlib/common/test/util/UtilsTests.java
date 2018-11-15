package com.ss.rlib.common.test.util;

import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Tests of {@link com.ss.rlib.common.util.Utils} methods.
 *
 * @author JavaSaBr
 */
public class UtilsTests {

    @Test
    void testHexConverting() {

        var random = ThreadLocalRandom.current();
        var chars = new char[random.nextInt(500, 1500)];

        ArrayUtils.fill(chars, () -> (char) random.nextInt(1, Character.MAX_VALUE));

        var source = String.valueOf(chars);
        var hexString = Utils.toHex(source);

        Assertions.assertEquals(source, Utils.fromHex(hexString));
    }
}
