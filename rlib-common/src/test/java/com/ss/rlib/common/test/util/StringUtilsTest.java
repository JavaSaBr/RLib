package com.ss.rlib.common.test.util;

import com.ss.rlib.common.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test methods in {@link com.ss.rlib.common.util.StringUtils}
 *
 * @author JavaSaBr
 */
public class StringUtilsTest {

    @Test
    void testHexConverting() {

        var original = "EWfwefsbrt34532#%#$^$#^@gfh\"P{\">K<CCZA";
        var hex = StringUtils.toHex(original);
        var parsed = StringUtils.fromHex(hex);

        Assertions.assertEquals(original, parsed);
    }

    @Test
    void testBinaryString() {

        var original = new byte[]{5, 2, 8, 120, 21, -40, -120, 63, 70};
        var hex = StringUtils.bytesToHexString(original);
        var parsed = StringUtils.hexStringToBytes(hex);

        Assertions.assertArrayEquals(original, parsed);
    }
}
