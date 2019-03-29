package com.ss.rlib.common.test.util;

import com.ss.rlib.common.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test methods in {@link com.ss.rlib.common.util.StringUtils}
 *
 * @author JavaSaBr
 */
class StringUtilsTest {

    @Test
    void shouldConvertStringToHex() {

        var original = "EWfwefsbrt34532#%#$^$#^@gfh\"P{\">K<CCZA";
        var hex = StringUtils.toHex(original);
        var parsed = StringUtils.fromHex(hex);

        Assertions.assertEquals(original, parsed);
    }

    @Test
    void shouldConvertHexStringToString() {

        var original = new byte[]{5, 2, 8, 120, 21, -40, -120, 63, 70};
        var hex = StringUtils.bytesToHexString(original);
        var parsed = StringUtils.hexStringToBytes(hex);

        Assertions.assertArrayEquals(original, parsed);
    }

    @Test
    void shouldReplaceOneVariableInString() {

        var original = "some text ${test_var} with one variable";
        var expected = "some text var_val with one variable";

        Assertions.assertEquals(expected, StringUtils.replace(original, "${test_var}", "var_val"));
    }

    @Test
    void shouldReplaceTwoVariablesInString() {

        var original = "some text ${test_var1} with two variable ${test_var2}";
        var expected = "some text var_val_1 with two variable var_val_2";

        Assertions.assertEquals(
            expected,
            StringUtils.replace(original, "${test_var1}", "var_val_1", "${test_var2}", "var_val_2")
        );
    }

    @Test
    void shouldReplaceVariablesInString() {

        var original = "some text ${test_var1} with variables ${test_var2}, ${test_var3}";
        var expected = "some text var_val_1 with variables var_val_2, var_val_3";

        Assertions.assertEquals(
            expected,
            StringUtils.replace(original,
                "${test_var1}", "var_val_1",
                "${test_var2}", "var_val_2",
                "${test_var3}", "var_val_3"
            )
        );
    }

    @Test
    void shouldThrownIllegalArgumentExceptionDuringReplacingStringWithWrongArgs() {

        Assertions.assertThrows(IllegalArgumentException.class, () ->
            StringUtils.replace("string", "name1", "val1", "name2"));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
            StringUtils.replace("string", "name1"));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
            StringUtils.replace("string"));
    }
}
