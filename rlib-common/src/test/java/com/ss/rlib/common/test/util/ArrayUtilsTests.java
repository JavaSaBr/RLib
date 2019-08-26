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
}
