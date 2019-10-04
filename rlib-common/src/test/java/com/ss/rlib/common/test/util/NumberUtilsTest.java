package com.ss.rlib.common.test.util;

import com.ss.rlib.common.util.NumberUtils;
import com.ss.rlib.common.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

/**
 * Test methods in {@link NumberUtils}
 *
 * @author JavaSaBr
 */
class NumberUtilsTest {

    @Test
    void shouldCheckStringIsLong() {
        Assertions.assertTrue(NumberUtils.isLong("1"));
        Assertions.assertTrue(NumberUtils.isLong("123123213123"));
        Assertions.assertFalse(NumberUtils.isLong("notlong"));
        Assertions.assertFalse(NumberUtils.isLong(null));
        Assertions.assertFalse(NumberUtils.isLong("2.1234"));
    }

    @Test
    void shouldSafetyConvertStringToLong() {
        Assertions.assertNotNull(NumberUtils.safeToLong("1"));
        Assertions.assertNotNull(NumberUtils.safeToLong("123123213123"));
        Assertions.assertNull(NumberUtils.safeToLong("notlong"));
        Assertions.assertNull(NumberUtils.safeToLong(null));
        Assertions.assertNull(NumberUtils.safeToLong("2.1234"));
    }

    @Test
    void shouldConvertStringToOptionalLong() {
        Assertions.assertTrue(NumberUtils.toOptionalLong("1").isPresent());
        Assertions.assertTrue(NumberUtils.toOptionalLong("123123213123").isPresent());
        Assertions.assertFalse(NumberUtils.toOptionalLong("notlong").isPresent());
        Assertions.assertFalse(NumberUtils.toOptionalLong(null).isPresent());
        Assertions.assertFalse(NumberUtils.toOptionalLong("2.1234").isPresent());
    }

    @Test
    void shouldSafetyConvertStringToInt() {
        Assertions.assertNotNull(NumberUtils.safeToInt("1"));
        Assertions.assertNotNull(NumberUtils.safeToInt("123124"));
        Assertions.assertNull(NumberUtils.safeToInt("notlong"));
        Assertions.assertNull(NumberUtils.safeToInt(null));
        Assertions.assertNull(NumberUtils.safeToInt("2.1234"));
    }
}
