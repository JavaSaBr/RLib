package javasabr.rlib.collections.array;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
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
    assertThat(mutableArray.size()).isEqualTo(1);
    assertThat(mutableArray.get(0)).isEqualTo("First");

    // when:
    mutableArray.add("second");

    // then:
    assertThat(mutableArray.size()).isEqualTo(2);
    assertThat(mutableArray.get(1)).isEqualTo("second");

    // when:
    mutableArray.add("third");

    // then:
    assertThat(mutableArray.size()).isEqualTo(3);
    assertThat(mutableArray.get(2)).isEqualTo("third");
    assertThat(mutableArray).isEqualTo(Array.of("First", "second", "third"));

    // when:
    for(int i = 0; i < 100; i++) {
      mutableArray.add("value_" + i);
    }

    // then:
    assertThat(mutableArray.size()).isEqualTo(103);
    assertThat(mutableArray.get(102)).isEqualTo("value_99");
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
    assertThat(mutableArray.size()).isEqualTo(100);
    assertThat(mutableArray.get(0)).isEqualTo("value_1");

    // when:
    mutableArray.remove("value_10");

    // then:
    assertThat(mutableArray.size()).isEqualTo(99);
    assertThat(mutableArray.get(0)).isEqualTo("value_1");
    assertThat(mutableArray.get(8)).isEqualTo("value_9");
    assertThat(mutableArray.get(9)).isEqualTo("value_11");

    // when:
    mutableArray.remove("value_100");

    // then:
    assertThat(mutableArray.size()).isEqualTo(98);
    assertThat(mutableArray.get(97)).isEqualTo("value_99");
    assertThat(mutableArray.get(96)).isEqualTo("value_98");
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
    assertThat(mutableArray.size()).isEqualTo(101);
    assertThat(mutableArray.get(0)).isEqualTo("value_200");

    // when:
    mutableArray.replace(11, "value_211");

    // then:
    assertThat(mutableArray.size()).isEqualTo(101);
    assertThat(mutableArray.get(11)).isEqualTo("value_211");
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
    assertThat(mutableArray.size()).isEqualTo(27);
    assertThat(mutableArray.get(0)).isEqualTo("value_0");
    assertThat(mutableArray.get(20)).isEqualTo("value_20");
    assertThat(mutableArray.get(21)).isEqualTo("a1");
    assertThat(mutableArray.get(26)).isEqualTo("a6");

    // when:
    mutableArray.addAll(anotherList);

    // then:
    assertThat(mutableArray.size()).isEqualTo(33);
    assertThat(mutableArray.get(0)).isEqualTo("value_0");
    assertThat(mutableArray.get(20)).isEqualTo("value_20");
    assertThat(mutableArray.get(21)).isEqualTo("a1");
    assertThat(mutableArray.get(26)).isEqualTo("a6");
    assertThat(mutableArray.get(27)).isEqualTo("l1");
    assertThat(mutableArray.get(32)).isEqualTo("l6");

    // when:
    mutableArray.addAll(anotherNativeArray);

    // then:
    assertThat(mutableArray.size()).isEqualTo(39);
    assertThat(mutableArray.get(0)).isEqualTo("value_0");
    assertThat(mutableArray.get(20)).isEqualTo("value_20");
    assertThat(mutableArray.get(21)).isEqualTo("a1");
    assertThat(mutableArray.get(26)).isEqualTo("a6");
    assertThat(mutableArray.get(27)).isEqualTo("l1");
    assertThat(mutableArray.get(32)).isEqualTo("l6");
    assertThat(mutableArray.get(33)).isEqualTo("na1");
    assertThat(mutableArray.get(38)).isEqualTo("na6");
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
    assertThat(mutableArray.size()).isEqualTo(33);
    assertThat(mutableArray.get(0)).isEqualTo("value_0");
    assertThat(mutableArray.get(20)).isEqualTo("value_20");
    assertThat(mutableArray.get(21)).isEqualTo("a1");
    assertThat(mutableArray.get(26)).isEqualTo("a6");
    assertThat(mutableArray.get(27)).isEqualTo("na1");
    assertThat(mutableArray.get(32)).isEqualTo("na6");
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
    assertThat(mutableArray.size()).isEqualTo(21);
    assertThat(mutableArray.get(0)).isEqualTo("value_0");
    assertThat(mutableArray.get(20)).isEqualTo("value_20");

    // when:
    mutableArray.clear();

    // then:
    assertThat(mutableArray.size()).isEqualTo(0);
    assertThat(mutableArray.toArray()).isEqualTo(new String[0]);
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
    assertThat(mutableArray.size()).isEqualTo(100);

    // when:
    for(int i = 0; i < 30; i++) {
      mutableArray.remove(i);
    }

    // then:
    assertThat(mutableArray.size()).isEqualTo(70);
    assertThat(unsafe.wrapped().length).isEqualTo(109);

    // when:
    unsafe.trimToSize();

    // then:
    assertThat(unsafe.wrapped().length).isEqualTo(70);
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
    assertThat(mutableArray.toString())
        .isEqualTo("[val_0, val_1, val_2, val_3, val_4, val_5, val_6, val_7, val_8, val_9]");
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
    assertThat(mutableArray).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should sort array using comparator correctly")
  void shouldSortArrayUsingComparatorCorrectly(MutableArray<String> mutableArray) {
    // given:
    mutableArray.addAll(Array.of("10", "99", "5", "3", "77", "45", "25", "56"));

    // when:
    mutableArray.sort(Comparator.comparingInt(Integer::parseInt));

    // then:
    var expected = Array.of("3", "5", "10", "25", "45", "56", "77", "99");
    assertThat(mutableArray).isEqualTo(expected);
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
