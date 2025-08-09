package javasabr.rlib.common.util.array;

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

  @Test
  void toArrayTest() {

    var array = IntegerArray.of(5, -4, 25, -1, 70);

    Assertions.assertArrayEquals(ArrayFactory.toIntArray(5, -4, 25, -1, 70), array.toArray());

    Assertions.assertArrayEquals(ArrayFactory.toIntArray(5, -4, 25, -1, 70), array.toArray(new int[0]));

    Assertions.assertArrayEquals(ArrayFactory.toIntArray(5, -4, 25, -1, 70), array.toArray(new int[array.size()]));
  }

  @Test
  void streamTest() {

    var array = IntegerArray.of(5, -4, 25, -1, 70);

    Assertions.assertArrayEquals(
        ArrayFactory.toIntArray(5, -4, 25, -1, 70),
        array
            .stream()
            .toArray());
  }

  @Test
  void forEachTest() {

    var array = IntegerArray.of(5, -4, 25, -1, 70);
    var toCollect = ArrayFactory.newMutableIntegerArray();

    array.forEachInt(toCollect::add);

    Assertions.assertEquals(toCollect, array);
  }
}
