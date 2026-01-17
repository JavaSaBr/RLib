package javasabr.rlib.collections.array;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MutableArrayTest {

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should correctly add elements")
  void shouldCorrectlyAddElements(MutableArray<String> mutableArray) {
    // when:
    mutableArray.add("First");

    // then:
    Assertions.assertEquals(1, mutableArray.size());
    Assertions.assertEquals("First", mutableArray.get(0));

    // when:
    mutableArray.add("second");

    // then:
    Assertions.assertEquals(2, mutableArray.size());
    Assertions.assertEquals("second", mutableArray.get(1));

    // when:
    mutableArray.add("third");

    // then:
    Assertions.assertEquals(3, mutableArray.size());
    Assertions.assertEquals("third", mutableArray.get(2));
    Assertions.assertEquals(Array.of("First", "second", "third"), mutableArray);

    // when:
    for(int i = 0; i < 100; i++) {
      mutableArray.add("value_" + i);
    }

    // then:
    Assertions.assertEquals(103, mutableArray.size());
    Assertions.assertEquals("value_99", mutableArray.get(102));
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should correctly remove elements")
  void shouldCorrectlyRemoveElements(MutableArray<String> mutableArray) {
    // given:
    for(int i = 0; i < 101; i++) {
      mutableArray.add("value_" + i);
    }

    // when:
    mutableArray.remove("value_0");

    // then:
    Assertions.assertEquals(100, mutableArray.size());
    Assertions.assertEquals("value_1", mutableArray.get(0));

    // when:
    mutableArray.remove("value_10");

    // then:
    Assertions.assertEquals(99, mutableArray.size());
    Assertions.assertEquals("value_1", mutableArray.get(0));
    Assertions.assertEquals("value_9", mutableArray.get(8));
    Assertions.assertEquals("value_11", mutableArray.get(9));

    // when:
    mutableArray.remove("value_100");

    // then:
    Assertions.assertEquals(98, mutableArray.size());
    Assertions.assertEquals("value_99", mutableArray.get(97));
    Assertions.assertEquals("value_98", mutableArray.get(96));
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should correctly replace elements")
  void shouldCorrectlyReplaceElements(MutableArray<String> mutableArray) {
    // given:
    for(int i = 0; i < 101; i++) {
      mutableArray.add("value_" + i);
    }

    // when:
    mutableArray.replace(0, "value_200");

    // then:
    Assertions.assertEquals(101, mutableArray.size());
    Assertions.assertEquals("value_200", mutableArray.get(0));

    // when:
    mutableArray.replace(11, "value_211");

    // then:
    Assertions.assertEquals(101, mutableArray.size());
    Assertions.assertEquals("value_211", mutableArray.get(11));
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should correctly add batch elements")
  void shouldCorrectlyAddBatchElements(MutableArray<String> mutableArray) {
    // given:
    for(int i = 0; i < 21; i++) {
      mutableArray.add("value_" + i);
    }

    var anotherArray = Array.typed(String.class, "a1", "a2", "a3", "a4", "a5", "a6");
    var anotherList = List.of("l1", "l2", "l3", "l4", "l5", "l6");
    var anotherNativeArray = List.of("na1", "na2", "na3", "na4", "na5", "na6")
        .toArray(String[]::new);

    // when:
    mutableArray.addAll(anotherArray);

    // then:
    Assertions.assertEquals(27, mutableArray.size());
    Assertions.assertEquals("value_0", mutableArray.get(0));
    Assertions.assertEquals("value_20", mutableArray.get(20));
    Assertions.assertEquals("a1", mutableArray.get(21));
    Assertions.assertEquals("a6", mutableArray.get(26));

    // when:
    mutableArray.addAll(anotherList);

    // then:
    Assertions.assertEquals(33, mutableArray.size());
    Assertions.assertEquals("value_0", mutableArray.get(0));
    Assertions.assertEquals("value_20", mutableArray.get(20));
    Assertions.assertEquals("a1", mutableArray.get(21));
    Assertions.assertEquals("a6", mutableArray.get(26));
    Assertions.assertEquals("l1", mutableArray.get(27));
    Assertions.assertEquals("l6", mutableArray.get(32));

    // when:
    mutableArray.addAll(anotherNativeArray);

    // then:
    Assertions.assertEquals(39, mutableArray.size());
    Assertions.assertEquals("value_0", mutableArray.get(0));
    Assertions.assertEquals("value_20", mutableArray.get(20));
    Assertions.assertEquals("a1", mutableArray.get(21));
    Assertions.assertEquals("a6", mutableArray.get(26));
    Assertions.assertEquals("l1", mutableArray.get(27));
    Assertions.assertEquals("l6", mutableArray.get(32));
    Assertions.assertEquals("na1", mutableArray.get(33));
    Assertions.assertEquals("na6", mutableArray.get(38));
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should correctly remove batch elements")
  void shouldCorrectlyRemoveBatchElements(MutableArray<String> mutableArray) {
    // given:
    for(int i = 0; i < 21; i++) {
      mutableArray.add("value_" + i);
    }

    var anotherArray = Array.typed(String.class, "a1", "a2", "a3", "a4", "a5", "a6");
    var anotherList = List.of("l1", "l2", "l3", "l4", "l5", "l6");
    var anotherNativeArray = List.of("na1", "na2", "na3", "na4", "na5", "na6")
        .toArray(String[]::new);

    mutableArray.addAll(anotherArray);
    mutableArray.addAll(anotherList);
    mutableArray.addAll(anotherNativeArray);

    // when:
    mutableArray.removeAll(anotherList);

    // then:
    Assertions.assertEquals(33, mutableArray.size());
    Assertions.assertEquals("value_0", mutableArray.get(0));
    Assertions.assertEquals("value_20", mutableArray.get(20));
    Assertions.assertEquals("a1", mutableArray.get(21));
    Assertions.assertEquals("a6", mutableArray.get(26));
    Assertions.assertEquals("na1", mutableArray.get(27));
    Assertions.assertEquals("na6", mutableArray.get(32));
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should clear array")
  void shouldClearArray(MutableArray<String> mutableArray) {
    // when:
    for(int i = 0; i < 21; i++) {
      mutableArray.add("value_" + i);
    }

    // then:
    Assertions.assertEquals(21, mutableArray.size());
    Assertions.assertEquals("value_0", mutableArray.get(0));
    Assertions.assertEquals("value_20", mutableArray.get(20));

    // when:
    mutableArray.clear();

    // then:
    Assertions.assertEquals(0, mutableArray.size());
    Assertions.assertArrayEquals(new String[0], mutableArray.toArray());
  }

  @ParameterizedTest
  @MethodSource("generateArraysWithTrimToSize")
  @DisplayName("should trim to size wrapped array")
  void shouldTrimToSizeWrappedArray(MutableArray<String> mutableArray) {
    // given:
    UnsafeMutableArray<String> unsafe = mutableArray.asUnsafe();

    // when:
    for(int i = 0; i < 100; i++) {
      mutableArray.add("value_" + i);
    }

    // then:
    Assertions.assertEquals(100, mutableArray.size());

    // when:
    for(int i = 0; i < 30; i++) {
      mutableArray.remove(i);
    }

    // then:
    Assertions.assertEquals(70, mutableArray.size());
    Assertions.assertEquals(109, unsafe.wrapped().length);

    // when:
    unsafe.trimToSize();

    // then:
    Assertions.assertEquals(70, unsafe.wrapped().length);
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should render to string correctly")
  void shouldRenderToStringCorrectly(MutableArray<String> mutableArray) {
    // when:
    for(int i = 0; i < 10; i++) {
      mutableArray.add("val_" + i);
    }
    // then:
    Assertions.assertEquals(
        "[val_0, val_1, val_2, val_3, val_4, val_5, val_6, val_7, val_8, val_9]",
        mutableArray.toString());
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should sort array correctly")
  void shouldSortArrayCorrectly(MutableArray<String> mutableArray) {
    // given:
    mutableArray.addAll(Array.of("10", "99", "5", "3", "77", "45", "25", "56"));
    
    // when:
    mutableArray.sort();
    
    // then:
    var expected = Array.of("10", "25", "3", "45", "5", "56", "77", "99");
    Assertions.assertEquals(expected, mutableArray);
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should sort array correctly")
  void shouldSortArrayUsingComparatorCorrectly(MutableArray<String> mutableArray) {
    // given:
    mutableArray.addAll(Array.of("10", "99", "5", "3", "77", "45", "25", "56"));

    // when:
    mutableArray.sort(Comparator.comparingInt(Integer::parseInt));

    // then:
    var expected = Array.of("3", "5", "10", "25", "45", "56", "77", "99");
    Assertions.assertEquals(expected, mutableArray);
  }

  private static Stream<Arguments> generateMutableArrays() {
    return Stream.of(
        Arguments.of(ArrayFactory.mutableArray(String.class)),
        Arguments.of(ArrayFactory.copyOnModifyArray(String.class)),
        Arguments.of(ArrayFactory.stampedLockBasedArray(String.class)));
  }

  private static Stream<Arguments> generateArraysWithTrimToSize() {
    return Stream.of(
        Arguments.of(ArrayFactory.mutableArray(String.class)),
        Arguments.of(ArrayFactory.stampedLockBasedArray(String.class)));
  }
}
