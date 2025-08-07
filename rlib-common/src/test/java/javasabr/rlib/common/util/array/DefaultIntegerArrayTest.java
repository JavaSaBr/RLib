package javasabr.rlib.common.util.array;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DefaultIntegerArrayTest {

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
        Assertions.assertEquals(2, array.indexOf(25));

        array.remove(25);

        Assertions.assertEquals(4, array.size());
        Assertions.assertEquals(-1, array.indexOf(25));

        array.fastRemove(5);

        Assertions.assertEquals(3, array.size());
        Assertions.assertEquals(-1, array.indexOf(5));
        Assertions.assertEquals(0, array.indexOf(70));

        array = ArrayFactory.newMutableIntegerArray(5, -4, 25, -1, 70);
        array.removeByIndex(3);

        Assertions.assertEquals(4, array.size());
        Assertions.assertEquals(-1, array.indexOf(-1));
        Assertions.assertEquals(3, array.indexOf(70));

        array = ArrayFactory.newMutableIntegerArray(5, -4, 25, -1, 70);
        array.fastRemoveByIndex(1);

        Assertions.assertEquals(4, array.size());
        Assertions.assertEquals(-1, array.indexOf(-4));
        Assertions.assertEquals(1, array.indexOf(70));

        array = ArrayFactory.newMutableIntegerArray(5, -4, 25, -1, 70);
        array.removeAll(ArrayFactory.newMutableIntegerArray(-4, 70));

        Assertions.assertEquals(3, array.size());
        Assertions.assertEquals(-1, array.indexOf(-4));
        Assertions.assertEquals(-1, array.indexOf(40));
        Assertions.assertEquals(2, array.indexOf(25));

        array = ArrayFactory.newMutableIntegerArray(5, -4, 25, -1, 70);
        array.retainAll(ArrayFactory.newMutableIntegerArray(25, -1));

        Assertions.assertEquals(2, array.size());
        Assertions.assertEquals(-1, array.indexOf(5));
        Assertions.assertEquals(-1, array.indexOf(-4));
        Assertions.assertEquals(-1, array.indexOf(70));
        Assertions.assertEquals(0, array.indexOf(25));
        Assertions.assertEquals(1, array.indexOf(-1));
    }

    @Test
    void getElementsTest() {

        var array = ArrayFactory.newMutableIntegerArray(5, -4, 25, -1, 70);

        Assertions.assertEquals(25, array.get(2));
        Assertions.assertEquals(5, array.first());
        Assertions.assertEquals(70, array.last());

        array.clear();

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> array.get(2));
        Assertions.assertThrows(IllegalStateException.class, array::first);
        Assertions.assertThrows(IllegalStateException.class, array::last);
    }

    @Test
    void sortTest() {

        var array = ArrayFactory.newMutableIntegerArray(5, -4, 25, -1, 70);
        array.sort();

        Assertions.assertEquals(-4, array.first());
        Assertions.assertEquals(70, array.last());
        Assertions.assertEquals(IntegerArray.of(-4, -1, 5, 25, 70), array);
    }

    @Test
    void equalsTest() {

        var first = ArrayFactory.newMutableIntegerArray(5, -4, 25, -1, 70);
        var second = ArrayFactory.newMutableIntegerArray(5, -4, 25, -1, 70);

        Assertions.assertEquals(first, second);

        first.add(14);
        second.add(14);

        Assertions.assertEquals(first, second);

        first.add(33);
        second.add(33);

        Assertions.assertEquals(first, second);

        first.add(55);
        second.add(45);

        Assertions.assertNotEquals(first, second);

        first.pop();
        second.pop();

        Assertions.assertEquals(first, second);

        first.removeByIndex(0);
        second.removeByIndex(0);

        Assertions.assertEquals(first, second);
    }

    @Test
    void toArrayTest() {

        var array = ArrayFactory.newMutableIntegerArray(5, -4, 25, -1, 70);

        Assertions.assertArrayEquals(
            ArrayFactory.toIntArray(5, -4, 25, -1, 70),
            array.toArray()
        );

        Assertions.assertArrayEquals(
            ArrayFactory.toIntArray(5, -4, 25, -1, 70),
            array.toArray(new int[0])
        );

        Assertions.assertArrayEquals(
            ArrayFactory.toIntArray(5, -4, 25, -1, 70),
            array.toArray(new int[array.size()])
        );
    }

    @Test
    void streamTest() {

        var array = ArrayFactory.newMutableIntegerArray(5, -4, 25, -1, 70);

        Assertions.assertArrayEquals(
            ArrayFactory.toIntArray(5, -4, 25, -1, 70),
            array.stream().toArray()
        );
    }

    @Test
    void forEachTest() {

        var array = ArrayFactory.newMutableIntegerArray(5, -4, 25, -1, 70);
        var toCollect = ArrayFactory.newMutableIntegerArray();

        array.forEachInt(toCollect::add);

        Assertions.assertEquals(toCollect, array);

        array = ArrayFactory.newMutableIntegerArray();
        array.add(5);
        array.add(-4);
        array.add(25);
        array.add(-1);
        array.add(70);

        var toCollect2 = ArrayFactory.newMutableIntegerArray();

        array.forEachInt(toCollect2::add);

        Assertions.assertEquals(toCollect2, array);
    }
}
