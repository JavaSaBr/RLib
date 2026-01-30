package javasabr.rlib.collections.array;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author JavaSaBr
 */
public class IntArrayTest {

  @Test
  void arrayOfTest() {
    // when:
    IntArray array1 = IntArray.of(5);
    IntArray array2 = IntArray.of(5, 8);
    IntArray array3 = IntArray.of(5, 8, 13);
    IntArray array4 = IntArray.of(5, 8, 13, 25);
    IntArray array5 = IntArray.of(5, 8, 13, 25, 33);

    // then:
    assertThat(array1.toArray())
        .isEqualTo(new int[]{5});
    assertThat(array2.toArray())
        .isEqualTo(new int[]{5, 8});
    assertThat(array3.toArray())
        .isEqualTo(new int[]{5, 8, 13});
    assertThat(array4.toArray())
        .isEqualTo(new int[]{5, 8, 13, 25});
    assertThat(array5.toArray())
        .isEqualTo(new int[]{5, 8, 13, 25, 33});
  }

  @Test
  void arrayRepeatedTest() {
    // when:
    IntArray repeated = IntArray.repeated(20, 5);

    // then:
    assertThat(repeated.toArray())
        .isEqualTo(new int[]{20, 20, 20, 20, 20});
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyTakeValues(IntArray array) {
    // then:
    assertThat(array.size()).isEqualTo(4);
    assertThat(array.get(0)).isEqualTo(5);
    assertThat(array.get(1)).isEqualTo(8);
    assertThat(array.get(2)).isEqualTo(13);
    assertThat(array.get(3)).isEqualTo(25);
    assertThat(array.first()).isEqualTo(5);
    assertThat(array.last()).isEqualTo(25);

    // then:
    assertThat(array.stream().toArray()).isEqualTo(new int[]{5, 8, 13, 25});

    // then:
    assertThat(array.contains(8)).isTrue();
    assertThat(array.contains(99)).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateArraysWithDuplicates")
  void shouldFindElementIndex(IntArray array) {
    // when/then:
    assertThat(array.indexOf(5)).isEqualTo(0);
    assertThat(array.indexOf(25)).isEqualTo(3);
    assertThat(array.indexOf(99)).isEqualTo(-1);
  }

  @ParameterizedTest
  @MethodSource("generateArraysWithDuplicates")
  void shouldFindLastElementIndex(IntArray array) {
    // when/then:
    assertThat(array.lastIndexOf(5)).isEqualTo(4);
    assertThat(array.lastIndexOf(13)).isEqualTo(6);
    assertThat(array.lastIndexOf(99)).isEqualTo(-1);
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyTransformToNativeArray(IntArray array) {
    // when/then:
    assertThat(array.stream().toArray()).isEqualTo(new int[]{5, 8, 13, 25});
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyCheckOnContains(IntArray array) {
    // when/then:
    assertThat(array.contains(5)).isTrue();
    assertThat(array.contains(25)).isTrue();
    assertThat(array.contains(99)).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyCheckOnContainsAllByArray(IntArray array) {
    // given:
    IntArray check1 = IntArray.of(5);
    IntArray check2 = IntArray.of(8, 13);
    IntArray check3 = IntArray.of(8, 13, 99);
    // when/then:
    assertThat(array.containsAll(check1)).isTrue();
    assertThat(array.containsAll(check2)).isTrue();
    assertThat(array.containsAll(check3)).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyCheckOnContainsAllByNativeArray(IntArray array) {
    // given:
    int[] check1 = new int[]{5};
    int[] check2 = new int[]{8, 13};
    int[] check3 = new int[]{8, 13, 99};
    // when/then:
    assertThat(array.containsAll(check1)).isTrue();
    assertThat(array.containsAll(check2)).isTrue();
    assertThat(array.containsAll(check3)).isFalse();
  }

  private static Stream<Arguments> generateArrays() {
    IntArray array = IntArray.of(5, 8, 13, 25);
    MutableIntArray mutableArray = ArrayFactory.mutableIntArray();
    mutableArray.addAll(array);
    return Stream.of(
        Arguments.of(array),
        Arguments.of(mutableArray));
  }

  private static Stream<Arguments> generateArraysWithDuplicates() {
    IntArray array = IntArray.of(5, 8, 13, 25, 5, 8, 13);
    MutableIntArray mutableArray = ArrayFactory.mutableIntArray();
    mutableArray.addAll(array);
    return Stream.of(
        Arguments.of(array),
        Arguments.of(mutableArray));
  }
}
