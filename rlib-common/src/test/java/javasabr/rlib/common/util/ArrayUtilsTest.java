package javasabr.rlib.common.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javasabr.rlib.common.util.array.ArrayFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of methods in ArrayUtils class.
 *
 * @author JavaSaBr
 */
public class ArrayUtilsTest {

  @Test
  void convertIntegerArrayToIntArrayTest() {

    Integer[] integers = ArrayFactory.toArray(1, 2, 3, 4, 5);
    int[] ints = ArrayUtils.toIntArray(integers);
    int[] empty = ArrayUtils.toIntArray(new Integer[0]);

    assertArrayEquals(ints, ArrayFactory.toIntArray(1, 2, 3, 4, 5));
    assertArrayEquals(empty, ArrayFactory.toIntArray());
  }

  @Test
  void convertStringToIntArrayTest() {

    assertArrayEquals(ArrayUtils.toIntArray("1,2,3,4,5", ","), ArrayFactory.toIntArray(1, 2, 3, 4, 5));
    assertArrayEquals(ArrayUtils.toIntArray(" 1 ,2 , 3,4, 5", ","), ArrayFactory.toIntArray(1, 2, 3, 4, 5));
    assertArrayEquals(ArrayUtils.toIntArray(" 1 ,2 , 3,4, 5", ","), ArrayFactory.toIntArray(1, 2, 3, 4, 5));

    assertThrows(NumberFormatException.class, () -> ArrayUtils.toIntArray(" 1 ,,2 , 3,4, 5,", ","));
    assertThrows(NumberFormatException.class, () -> ArrayUtils.toIntArray(" 1 ,qwd , 3,4, 5", ","));
    assertThrows(NumberFormatException.class, () -> ArrayUtils.toIntArray(" 1 ,2.5 , 3,4, 5", ","));
  }

  @Test
  void mapNullableTest() {

    String[] nullStrings = null;
    String[] emptyStrings = new String[0];
    String[] singleStrings = ArrayFactory.toArray("5");
    String[] strings = ArrayFactory.toArray("8", "1", "6");

    Assertions.assertNull(ArrayUtils.mapNullable(nullStrings, Integer::parseInt, Integer.class));

    assertArrayEquals(new Integer[0], ArrayUtils.mapNullable(emptyStrings, Integer::parseInt, Integer.class));
    assertArrayEquals(ArrayFactory.toArray(5), ArrayUtils.mapNullable(singleStrings, Integer::parseInt, Integer.class));
    assertArrayEquals(ArrayFactory.toArray(8, 1, 6), ArrayUtils.mapNullable(strings, Integer::parseInt, Integer.class));
  }

  @Test
  void longsToIntsTest() {

    assertArrayEquals(ArrayFactory.toIntArray(1, 5, 3), ArrayUtils.longsToInts(ArrayFactory.toLongArray(1, 5, 3)));

    assertArrayEquals(ArrayUtils.EMPTY_INT_ARRAY, ArrayUtils.longsToInts(ArrayUtils.EMPTY_LONG_ARRAY));
  }

  @Test
  void mapTest() {

    String[] emptyStrings = new String[0];
    String[] singleStrings = ArrayFactory.toArray("5");
    String[] strings = ArrayFactory.toArray("8", "1", "6");

    assertArrayEquals(new Integer[0], ArrayUtils.map(emptyStrings, Integer::parseInt, Integer.class));
    assertArrayEquals(ArrayFactory.toArray(5), ArrayUtils.map(singleStrings, Integer::parseInt, Integer.class));
    assertArrayEquals(ArrayFactory.toArray(8, 1, 6), ArrayUtils.map(strings, Integer::parseInt, Integer.class));
  }

  @Test
  void mapWithDefTest() {

    String[] nullStrings = null;
    String[] emptyStrings = new String[0];
    String[] singleStrings = ArrayFactory.toArray("5");
    String[] strings = ArrayFactory.toArray("8", "1", "6");

    assertArrayEquals(new Integer[0], ArrayUtils.map(nullStrings, Integer::parseInt, new Integer[0]));

    assertArrayEquals(new Integer[0], ArrayUtils.map(emptyStrings, Integer::parseInt, new Integer[0]));
    assertArrayEquals(ArrayFactory.toArray(5), ArrayUtils.map(singleStrings, Integer::parseInt, new Integer[0]));
    assertArrayEquals(ArrayFactory.toArray(8, 1, 6), ArrayUtils.map(strings, Integer::parseInt, new Integer[0]));
  }

  @Test
  void copyOfAndExtendTest() {

    var array = ArrayFactory.toArray("First", "Second");
    var newArray = ArrayUtils.copyOfAndExtend(array, 2);

    assertEquals(4, newArray.length);
    assertArrayEquals(ArrayFactory.toArray("First", "Second", null, null), newArray);
  }

  @Test
  void isNotEmptyTest() {

    assertTrue(ArrayUtils.isNotEmpty(new byte[1]));
    assertFalse(ArrayUtils.isNotEmpty(new byte[0]));
    assertFalse(ArrayUtils.isNotEmpty((byte[]) null));

    assertTrue(ArrayUtils.isNotEmpty(new short[1]));
    assertFalse(ArrayUtils.isNotEmpty(new short[0]));
    assertFalse(ArrayUtils.isNotEmpty((short[]) null));

    assertTrue(ArrayUtils.isNotEmpty(new int[1]));
    assertFalse(ArrayUtils.isNotEmpty(new int[0]));
    assertFalse(ArrayUtils.isNotEmpty((int[]) null));

    assertTrue(ArrayUtils.isNotEmpty(new char[1]));
    assertFalse(ArrayUtils.isNotEmpty(new char[0]));
    assertFalse(ArrayUtils.isNotEmpty((char[]) null));

    assertTrue(ArrayUtils.isNotEmpty(new long[1]));
    assertFalse(ArrayUtils.isNotEmpty(new long[0]));
    assertFalse(ArrayUtils.isNotEmpty((long[]) null));

    assertTrue(ArrayUtils.isNotEmpty(new float[1]));
    assertFalse(ArrayUtils.isNotEmpty(new float[0]));
    assertFalse(ArrayUtils.isNotEmpty((float[]) null));

    assertTrue(ArrayUtils.isNotEmpty(new double[1]));
    assertFalse(ArrayUtils.isNotEmpty(new double[0]));
    assertFalse(ArrayUtils.isNotEmpty((double[]) null));

    assertTrue(ArrayUtils.isNotEmpty(new Object[1]));
    assertFalse(ArrayUtils.isNotEmpty(new Object[0]));
    assertFalse(ArrayUtils.isNotEmpty((Object[]) null));
  }
}
