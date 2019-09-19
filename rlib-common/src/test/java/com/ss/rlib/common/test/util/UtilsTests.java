package com.ss.rlib.common.test.util;

import com.ss.rlib.common.util.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link com.ss.rlib.common.util.Utils} methods.
 *
 * @author JavaSaBr
 */
public class UtilsTests {

    @Test
    void shouldSafetyTryGet() {

        Assertions.assertEquals(Integer.valueOf(15), Utils.tryGet("15", Integer::valueOf));
        Assertions.assertNull(Utils.tryGet("invalidnumber", Integer::valueOf));

        Assertions.assertEquals(Integer.valueOf(15), Utils.tryGet("15", Integer::valueOf, 2));
        Assertions.assertEquals(Integer.valueOf(2), Utils.tryGet("invalidnumber", Integer::valueOf, 2));
    }

    @Test
    void shouldSafetyTryGetAndConvert() {
        Assertions.assertEquals("15", Utils.tryGetAndConvert("15", Integer::valueOf, Object::toString));
        Assertions.assertNull(Utils.tryGetAndConvert("invalidnumber", Integer::valueOf, Object::toString));
    }
}
