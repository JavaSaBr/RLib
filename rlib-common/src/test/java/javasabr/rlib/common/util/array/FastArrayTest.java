package javasabr.rlib.common.util.array;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
public class FastArrayTest {

  @Test
  void equalsTest() {

    var integerArray = ArrayFactory.asArray(2, 5, 1, 7, 6, 8, 4);
    var stringArray = ArrayFactory.asArray("third", "first", "fifth");

    Assertions.assertEquals(ArrayFactory.asArray(2, 5, 1, 7, 6, 8, 4), integerArray);
    Assertions.assertNotEquals(ArrayFactory.asArray(2, 5, 1, 2, 6, 8, 4), integerArray);

    Assertions.assertEquals(ArrayFactory.asArray("third", "first", "fifth"), stringArray);
    Assertions.assertNotEquals(ArrayFactory.asArray("third", "first", "fi2fth"), stringArray);

    Assertions.assertEquals(ArrayFactory.newArray(String.class), ArrayFactory.newArray(String.class));
    Assertions.assertEquals(Array.empty(), ArrayFactory.newArray(String.class));
  }
}
