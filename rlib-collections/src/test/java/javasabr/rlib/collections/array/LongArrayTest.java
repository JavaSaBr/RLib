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
public class LongArrayTest {

  @Test
  void arrayOfTest() {
    // when:
    LongArray array1 = LongArray.of(5);
    LongArray array2 = LongArray.of(5, 8);
    LongArray array3 = LongArray.of(5, 8, 13);
    LongArray array4 = LongArray.of(5, 8, 13, 25);
    LongArray array5 = LongArray.of(5, 8, 13, 25, 33);

    // then:
    assertThat(array1.toArray())
        .isEqualTo(new long[]{5});
    assertThat(array2.toArray())
        .isEqualTo(new long[]{5, 8});
    assertThat(array3.toArray())
        .isEqualTo(new long[]{5, 8, 13});
    assertThat(array4.toArray())
        .isEqualTo(new long[]{5, 8, 13, 25});
    assertThat(array5.toArray())
        .isEqualTo(new long[]{5, 8, 13, 25, 33});
  }

  @Test
  void arrayRepeatedTest() {
    // when:
    LongArray repeated = LongArray.repeated(20, 5);

    // then:
    assertThat(repeated.toArray())
        .isEqualTo(new long[]{20, 20, 20, 20, 20});
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyTakeValues(LongArray array) {
    // then:
    assertThat(array.size()).isEqualTo(4);
    assertThat(array.get(0)).isEqualTo(5);
    assertThat(array.get(1)).isEqualTo(8);
    assertThat(array.get(2)).isEqualTo(13);
    assertThat(array.get(3)).isEqualTo(25);
    assertThat(array.first()).isEqualTo(5);
    assertThat(array.last()).isEqualTo(25);

    // then:
    assertThat(array.stream().toArray()).isEqualTo(new long[]{5, 8, 13, 25});

    // then:
    assertThat(array.contains(8)).isTrue();
    assertThat(array.contains(99)).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateArraysWithDuplicates")
  void shouldFindElementIndex(LongArray array) {
    // when/then:
    assertThat(array.indexOf(5)).isEqualTo(0);
    assertThat(array.indexOf(25)).isEqualTo(3);
    assertThat(array.indexOf(99)).isEqualTo(-1);
  }

  @ParameterizedTest
  @MethodSource("generateArraysWithDuplicates")
  void shouldFindLastElementIndex(LongArray array) {
    // when/then:
    assertThat(array.lastIndexOf(5)).isEqualTo(4);
    assertThat(array.lastIndexOf(13)).isEqualTo(6);
    assertThat(array.lastIndexOf(99)).isEqualTo(-1);
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyTransformToNativeArray(LongArray array) {
    // when/then:
    assertThat(array.stream().toArray())
        .isEqualTo(new long[]{5, 8, 13, 25});
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyCheckOnContains(LongArray array) {
    // when/then:
    assertThat(array.contains(5)).isTrue();
    assertThat(array.contains(25)).isTrue();
    assertThat(array.contains(99)).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyCheckOnContainsAllByArray(LongArray array) {
    // given:
    LongArray check1 = LongArray.of(5);
    LongArray check2 = LongArray.of(8, 13);
    LongArray check3 = LongArray.of(8, 13, 99);
    // when/then:
    assertThat(array.containsAll(check1)).isTrue();
    assertThat(array.containsAll(check2)).isTrue();
    assertThat(array.containsAll(check3)).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyCheckOnContainsAllByNativeArray(LongArray array) {
    // given:
    long[] check1 = new long[]{5};
    long[] check2 = new long[]{8, 13};
    long[] check3 = new long[]{8, 13, 99};
    // when/then:
    assertThat(array.containsAll(check1)).isTrue();
    assertThat(array.containsAll(check2)).isTrue();
    assertThat(array.containsAll(check3)).isFalse();
  }

  private static Stream<Arguments> generateArrays() {
    LongArray array = LongArray.of(5, 8, 13, 25);
    MutableLongArray mutableArray = ArrayFactory.mutableLongArray();
    mutableArray.addAll(array);
    return Stream.of(
        Arguments.of(array),
        Arguments.of(mutableArray));
  }

  private static Stream<Arguments> generateArraysWithDuplicates() {
    LongArray array = LongArray.of(5, 8, 13, 25, 5, 8, 13);
    MutableLongArray mutableArray = ArrayFactory.mutableLongArray();
    mutableArray.addAll(array);
    return Stream.of(
        Arguments.of(array),
        Arguments.of(mutableArray));
  }
}
