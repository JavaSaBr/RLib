package com.ss.rlib.common.test.util.array;

import com.ss.rlib.common.util.array.IntegerArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReadOnlyIntegerArrayTest {

    @Test
    void getElementsTest() {

        var array = IntegerArray.of(5, -4, 25, -1, 70);

        Assertions.assertEquals(25, array.get(2));
        Assertions.assertEquals(5, array.first());
        Assertions.assertEquals(70, array.last());

        var empty = IntegerArray.EMPTY;

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> empty.get(2));
        Assertions.assertThrows(IllegalStateException.class, empty::first);
        Assertions.assertThrows(IllegalStateException.class, empty::last);
    }

    @Test
    void equalsTest() {

        var first = IntegerArray.of(5, -4, 25, -1, 70);
        var second = IntegerArray.of(5, -4, 25, -1, 70);

        Assertions.assertEquals(first, second);
        Assertions.assertNotEquals(IntegerArray.of(5, -4, 4, -1, 70), second);
    }
}
