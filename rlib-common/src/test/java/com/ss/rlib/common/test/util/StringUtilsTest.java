package com.ss.rlib.common.test.util;

import com.ss.rlib.common.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;

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
    void shouldReplaceTwoVariablesInStringTwice() {

        var original = "some text ${test_var1} with two variable ${test_var2}, and ${test_var1} and ${test_var2}";
        var expected = "some text var_val_1 with two variable var_val_2, and var_val_1 and var_val_2";

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

    @Test
    void shouldVerifyEmails() {

        Assertions.assertTrue(StringUtils.isValidEmail("test@test.com"));
        Assertions.assertTrue(StringUtils.isValidEmail("тест@test.com"));
        Assertions.assertTrue(StringUtils.isValidEmail("тест@тест.рф"));
        Assertions.assertTrue(StringUtils.isValidEmail("my-name.test@test.com"));

        Assertions.assertFalse(StringUtils.isValidEmail("@test.com"));
        Assertions.assertFalse(StringUtils.isValidEmail("test@.com"));
        Assertions.assertFalse(StringUtils.isValidEmail("%$%&$&%@$%&&%$&.com"));
    }

    @Test
    void shouldDetectEmails() {

        Assertions.assertTrue(StringUtils.isEmail("test@test.com"));
        Assertions.assertTrue(StringUtils.isEmail("test.test@test.com"));
        Assertions.assertTrue(StringUtils.isEmail("test.test@test.test.com"));

        Assertions.assertFalse(StringUtils.isEmail("@test.com"));
        Assertions.assertFalse(StringUtils.isEmail("test-test.com"));
        Assertions.assertFalse(StringUtils.isEmail("test@test"));
        Assertions.assertFalse(StringUtils.isEmail("test@test."));
    }

    @Test
    void convertToLocalDateTest() {

        Assertions.assertEquals(
            LocalDate.of(1800, 5, 20),
            StringUtils.toLocalDate("1800-05-20")
        );

        Assertions.assertNull(StringUtils.toLocalDate("2020-1-10"));
        Assertions.assertEquals(
            LocalDate.of(2020, 1, 10),
            StringUtils.toLocalDate("2020-01-10")
        );

        Assertions.assertNull(StringUtils.toLocalDate("2015-5-1"));
        Assertions.assertEquals(
            LocalDate.of(2015, 5, 1),
            StringUtils.toLocalDate("2015-05-01")
        );

        Assertions.assertNull(StringUtils.toLocalDate("invaliddate"));
    }

    @Test
    void formatTimestampTest() {

        var localDateTime = LocalDateTime.of(
            2010,
            5,
            12,
            23,
            10,
            35,
            0
        );

        Assertions.assertEquals("23:10:35:000", StringUtils.formatShortTimestamp(localDateTime));
        Assertions.assertEquals("23:10:35:000", StringUtils.formatShortTimestamp(localDateTime
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli())
        );

        var zonedDateTime = ZonedDateTime.of(
            2010,
            5,
            12,
            23,
            10,
            35,
            0,
            ZoneOffset.ofHours(3)
        );

        Assertions.assertEquals("23:10:35:000", StringUtils.formatShortTimestamp(zonedDateTime));
        Assertions.assertEquals("23:10:35:000", StringUtils.formatShortTimestamp(zonedDateTime
            .toLocalDateTime()
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli())
        );
    }
}
