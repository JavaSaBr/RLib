package com.ss.rlib.common.test.util;

import static org.junit.jupiter.api.Assertions.*;
import com.ss.rlib.common.util.NumberUtils;
import org.junit.jupiter.api.Test;

/**
 * Test methods in {@link NumberUtils}
 *
 * @author JavaSaBr
 */
class NumberUtilsTest {

    @Test
    void shouldCheckStringIsLong() {
        assertTrue(NumberUtils.isLong("1"));
        assertTrue(NumberUtils.isLong("123123213123"));
        assertFalse(NumberUtils.isLong("notlong"));
        assertFalse(NumberUtils.isLong(null));
        assertFalse(NumberUtils.isLong("2.1234"));
    }

    @Test
    void shouldSafetyConvertStringToLong() {
        assertNotNull(NumberUtils.safeToLong("1"));
        assertNotNull(NumberUtils.safeToLong("123123213123"));
        assertNull(NumberUtils.safeToLong("notlong"));
        assertNull(NumberUtils.safeToLong(null));
        assertNull(NumberUtils.safeToLong("2.1234"));
    }

    @Test
    void shouldConvertStringToOptionalLong() {
        assertTrue(NumberUtils.toOptionalLong("1").isPresent());
        assertTrue(NumberUtils.toOptionalLong("123123213123").isPresent());
        assertFalse(NumberUtils.toOptionalLong("notlong").isPresent());
        assertFalse(NumberUtils.toOptionalLong(null).isPresent());
        assertFalse(NumberUtils.toOptionalLong("2.1234").isPresent());
    }

    @Test
    void shouldSafetyConvertStringToInt() {
        assertNotNull(NumberUtils.safeToInt("1"));
        assertNotNull(NumberUtils.safeToInt("123124"));
        assertNull(NumberUtils.safeToInt("notlong"));
        assertNull(NumberUtils.safeToInt(null));
        assertNull(NumberUtils.safeToInt("2.1234"));
    }

    @Test
    void shouldReadBitsCorrectly() {

        assertTrue(NumberUtils.isSetBit((byte) 0b00000001, 0));
        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 0));

        assertTrue(NumberUtils.isSetBit((byte) 0b00000010, 1));
        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 1));

        assertTrue(NumberUtils.isSetBit((byte) 0b00000100, 2));
        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 2));

        assertTrue(NumberUtils.isSetBit((byte) 0b00001000, 3));
        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 3));

        assertTrue(NumberUtils.isSetBit((byte) 0b00010000, 4));
        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 4));

        assertTrue(NumberUtils.isSetBit((byte) 0b00100000, 5));
        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 5));

        assertTrue(NumberUtils.isSetBit((byte) 0b01000000, 6));
        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 6));

        assertTrue(NumberUtils.isSetBit((byte) 0b10000000, 7));
        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 7));

        assertTrue(NumberUtils.isSetBit((byte) 0b11111111, 0));
        assertTrue(NumberUtils.isSetBit((byte) 0b11111111, 1));
        assertTrue(NumberUtils.isSetBit((byte) 0b11111111, 2));
        assertTrue(NumberUtils.isSetBit((byte) 0b11111111, 3));
        assertTrue(NumberUtils.isSetBit((byte) 0b11111111, 4));
        assertTrue(NumberUtils.isSetBit((byte) 0b11111111, 5));
        assertTrue(NumberUtils.isSetBit((byte) 0b11111111, 6));
        assertTrue(NumberUtils.isSetBit((byte) 0b11111111, 7));

        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 0));
        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 1));
        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 2));
        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 3));
        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 4));
        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 5));
        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 6));
        assertTrue(NumberUtils.isNotSetBit((byte) 0b00000000, 7));
    }

    @Test
    void shouldWriteHighLowBitsCorrectly() {

        for (int low = 0; low < 16; low++) {
            for (int high = 0; high < 16; high++) {
                var result = NumberUtils.setHighByteBits(low, high);
                assertEquals(high, NumberUtils.getHighByteBits(result));
                assertEquals(low, NumberUtils.getLowByteBits(result));
            }
        }

        for (int low = 0; low < 16; low++) {
            var result = NumberUtils.setLowByteBits(256, low);
            assertEquals(16, NumberUtils.getHighByteBits(result));
            assertEquals(low, NumberUtils.getLowByteBits(result));
        }
    }

    @Test
    void shouldReadHighLowBitsCorrectly() {

        for (int i = 0; i < 16; i++) {
            assertEquals(i, NumberUtils.getHighByteBits(i << 4));
        }

        assertEquals(0b0000_1000, NumberUtils.getHighByteBits(0b1000_0100));
        assertEquals(0b0000_0100, NumberUtils.getHighByteBits(0b0100_1000));
        assertEquals(0b0000_0010, NumberUtils.getHighByteBits(0b0010_0010));
        assertEquals(0b0000_0001, NumberUtils.getHighByteBits(0b0001_0001));
        assertEquals(0b0000_0101, NumberUtils.getHighByteBits(0b0101_1000));

        for (int i = 0; i < 16; i++) {
            assertEquals(i, NumberUtils.getLowByteBits(i & 0x0F));
        }

        assertEquals(0b0000_1000, NumberUtils.getLowByteBits(0b1000_1000));
        assertEquals(0b0000_0100, NumberUtils.getLowByteBits(0b0100_0100));
        assertEquals(0b0000_0010, NumberUtils.getLowByteBits(0b0010_0010));
        assertEquals(0b0000_0001, NumberUtils.getLowByteBits(0b0001_0001));
        assertEquals(0b0000_0101, NumberUtils.getLowByteBits(0b0101_0101));
    }

    @Test
    void shouldByteToUnsignedByteCorrectly() {
        assertEquals(255, NumberUtils.toUnsignedByte((byte) -1));
        assertEquals(106, NumberUtils.toUnsignedByte((byte) -150));
        assertEquals(1, NumberUtils.toUnsignedByte((byte) 1));
        assertEquals(150, NumberUtils.toUnsignedByte((byte) 150));
    }

    @Test
    void shouldChangeBitsInByteCorrectly() {

        assertTrue(NumberUtils.isSetBit(NumberUtils.setBit(0, 0), 0));
        assertTrue(NumberUtils.isSetBit(NumberUtils.setBit(0, 1), 1));
        assertTrue(NumberUtils.isSetBit(NumberUtils.setBit(0, 2), 2));
        assertTrue(NumberUtils.isSetBit(NumberUtils.setBit(0, 3), 3));
        assertTrue(NumberUtils.isSetBit(NumberUtils.setBit(0, 4), 4));
        assertTrue(NumberUtils.isSetBit(NumberUtils.setBit(0, 5), 5));
        assertTrue(NumberUtils.isSetBit(NumberUtils.setBit(0, 6), 6));
        assertTrue(NumberUtils.isSetBit(NumberUtils.setBit(0, 7), 7));

        assertTrue(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 0), 0));
        assertTrue(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 1), 1));
        assertTrue(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 2), 2));
        assertTrue(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 3), 3));
        assertTrue(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 4), 4));
        assertTrue(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 5), 5));
        assertTrue(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 6), 6));
        assertTrue(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 7), 7));
    }

    @Test
    void shouldReadDifferentBitsCorrectly() {

        assertEquals(0b0000_1000, NumberUtils.getHighByteBits(0b1000_0100));
        System.out.println(Integer.toString(NumberUtils.getHighByteBits(0b1000_0100), 2));
        System.out.println(Integer.toString(NumberUtils.readBits(0b1000_0100, 4, 1), 2));
        assertEquals(0b0000_1000, NumberUtils.readBits(0b1000_0100, 1, 4));
    }
}
