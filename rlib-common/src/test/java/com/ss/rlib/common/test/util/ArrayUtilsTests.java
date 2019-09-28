package com.ss.rlib.common.test.util;

import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of methods in ArrayUtils class.
 *
 * @author JavaSaBr
 */
public class ArrayUtilsTests {

    @Test
    void convertIntegerArrayToIntArrayTest() {

        Integer[] integers = ArrayFactory.toArray(1, 2, 3, 4, 5);
        int[] ints = ArrayUtils.toIntArray(integers);
        int[] empty = ArrayUtils.toIntArray(new Integer[0]);

        Assertions.assertArrayEquals(ints, ArrayFactory.toIntArray(1, 2, 3, 4, 5));
        Assertions.assertArrayEquals(empty, ArrayFactory.toIntArray());
    }

    @Test
    void convertStringToIntArrayTest() {

        Assertions.assertArrayEquals(
            ArrayUtils.toIntArray("1,2,3,4,5", ","),
            ArrayFactory.toIntArray(1, 2, 3, 4, 5)
        );
        Assertions.assertArrayEquals(
            ArrayUtils.toIntArray(" 1 ,2 , 3,4, 5", ","),
            ArrayFactory.toIntArray(1, 2, 3, 4, 5)
        );
        Assertions.assertArrayEquals(
            ArrayUtils.toIntArray(" 1 ,2 , 3,4, 5", ","),
            ArrayFactory.toIntArray(1, 2, 3, 4, 5)
        );

        Assertions.assertThrows(NumberFormatException.class,
            () -> ArrayUtils.toIntArray(" 1 ,,2 , 3,4, 5,", ","));
        Assertions.assertThrows(NumberFormatException.class,
            () -> ArrayUtils.toIntArray(" 1 ,qwd , 3,4, 5", ","));
        Assertions.assertThrows(NumberFormatException.class,
            () -> ArrayUtils.toIntArray(" 1 ,2.5 , 3,4, 5", ","));
    }

    @Test
    void mapNullableTest() {

        String[] nullStrings = null;
        String[] emptyStrings = new String[0];
        String[] singleStrings = ArrayFactory.toArray("5");
        String[] strings = ArrayFactory.toArray("8", "1", "6");

        Assertions.assertNull(ArrayUtils.mapNullable(nullStrings, Integer::parseInt, Integer.class));

        Assertions.assertArrayEquals(
            new Integer[0], ArrayUtils.mapNullable(emptyStrings, Integer::parseInt, Integer.class)
        );
        Assertions.assertArrayEquals(
            ArrayFactory.toArray(5), ArrayUtils.mapNullable(singleStrings, Integer::parseInt, Integer.class)
        );
        Assertions.assertArrayEquals(
            ArrayFactory.toArray(8, 1, 6), ArrayUtils.mapNullable(strings, Integer::parseInt, Integer.class)
        );
    }

    @Test
    void longsToIntsTest() {

        Assertions.assertArrayEquals(
            ArrayFactory.toIntArray(1, 5, 3),
            ArrayUtils.longsToInts(ArrayFactory.toLongArray(1, 5, 3))
        );

        Assertions.assertArrayEquals(
            ArrayUtils.EMPTY_INT_ARRAY,
            ArrayUtils.longsToInts(ArrayUtils.EMPTY_LONG_ARRAY)
        );
    }
}
