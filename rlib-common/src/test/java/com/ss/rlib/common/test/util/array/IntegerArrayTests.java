package com.ss.rlib.common.test.util.array;

import com.ss.rlib.common.util.array.ArrayFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntegerArrayTests {

    @Test
    void addElementsTest() {

        var array = ArrayFactory.newMutableIntegerArray();

        Assertions.assertEquals(0, array.size());

        array.add(5);

        Assertions.assertEquals(1, array.size());
        Assertions.assertEquals(5, array.get(0));

        array.add(-5);

        Assertions.assertEquals(2, array.size());
        Assertions.assertEquals(-5, array.get(1));

        array.addAll(new int[]{-20, 4});

        Assertions.assertEquals(4, array.size());
        Assertions.assertEquals(-20, array.get(2));
        Assertions.assertEquals(4, array.get(3));

        array.addAll(ArrayFactory.newMutableIntegerArray(66, -22));

        Assertions.assertEquals(6, array.size());
        Assertions.assertEquals(66, array.get(4));
        Assertions.assertEquals(-22, array.get(5));
    }

    @Test
    void removeElementsTest() {

        var array = ArrayFactory.newMutableIntegerArray(5, -4, 25, -1, 70);

        Assertions.assertEquals(5, array.size());

        array.remove(25);


    }
}
