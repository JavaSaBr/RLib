package javasabr.rlib.collections.array;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author JavaSaBr
 */
public class ArrayTest  {

  @Test
  void arrayOfTest() {
    // when:
    Array<String> array1 = Array.of("First");
    Array<String> array2 = Array.of("First", "Second");
    Array<String> array3 = Array.of("First", "Second", "Third");
    Array<String> array4 = Array.of("First", "Second", "Third", "Fourth");
    Array<String> array5 = Array.of("First", "Second", "Third", "Fourth", "Fifth");
    Array<String> array6 = Array.of("First", "Second", "Third", "Fourth", "Fifth", "Sixth");
    Array<String> array7 = Array.of("First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh");
    Array<String> array8 = Array.typed(String.class,"First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh");
    Array<Object> array9 = Array.of("First", "Second", 55, "Fourth", 20D, "Sixth", "Seventh");

    // then:
    assertThat(array1.toArray())
        .isEqualTo(new String[]{"First"});
    assertThat(array2.toArray())
        .isEqualTo(new String[]{"First", "Second"});
    assertThat(array3.toArray())
        .isEqualTo(new String[]{"First", "Second", "Third"});
    assertThat(array4.toArray())
        .isEqualTo(new String[]{"First", "Second", "Third", "Fourth"});
    assertThat(array5.toArray())
        .isEqualTo(new String[]{"First", "Second", "Third", "Fourth", "Fifth"});
    assertThat(array6.toArray())
        .isEqualTo(new String[]{"First", "Second", "Third", "Fourth", "Fifth", "Sixth"});
    assertThat(array7.toArray())
        .isEqualTo(new String[]{"First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh"});
    assertThat(array8.toArray())
        .isEqualTo(new String[]{"First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh"});
    assertThat(array9.toArray())
        .isEqualTo(new Object[]{"First", "Second", 55, "Fourth", 20D, "Sixth", "Seventh"});
  }

  @Test
  void arrayRepeatedTest() {
    // when:
    Array<String> array1 = Array.repeated("value", 4);
    Array<Integer> array2 = Array.repeated(20, 5);

    // then:
    assertThat(array1.toArray())
        .isEqualTo(new String[]{"value", "value", "value", "value"});
    assertThat(array2.toArray())
        .isEqualTo(new Integer[]{20, 20, 20, 20, 20});
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyTakeValues(Array<String> array) {

    // then:
    assertThat(array.size()).isEqualTo(4);
    assertThat(array.get(0)).isEqualTo("First");
    assertThat(array.get(1)).isEqualTo("Second");
    assertThat(array.get(2)).isEqualTo("Third");
    assertThat(array.get(3)).isEqualTo("  ");
    assertThat(array.first()).isEqualTo("First");
    assertThat(array.last()).isEqualTo("  ");

    // then:
    assertThat(array.stream().toArray()).isEqualTo(new String[]{
            "First",
            "Second",
            "Third",
            "  "
        });

    // then:
    assertThat(array.contains("Second")).isTrue();
    assertThat(array.contains("test")).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateArraysWithDuplicates")
  void shouldFindElementIndex(Array<String> array) {
    // when/then:
    assertThat(array.indexOf("First")).isEqualTo(0);
    assertThat(array.indexOf("  ")).isEqualTo(3);
    assertThat(array.indexOf("notexist")).isEqualTo(-1);
  }

  @ParameterizedTest
  @MethodSource("generateArraysWithDuplicates")
  void shouldFindLastElementIndex(Array<String> array) {
    // when/then:
    assertThat(array.lastIndexOf("First")).isEqualTo(4);
    assertThat(array.lastIndexOf("Third")).isEqualTo(6);
    assertThat(array.lastIndexOf("notexist")).isEqualTo(-1);
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldFindElementIndexWithFunction(Array<String> array) {
    // when/then:
    assertThat(array.indexOf(s -> s + s, "FirstFirst")).isEqualTo(0);
    assertThat(array.indexOf(s -> s + s, "    ")).isEqualTo(3);
    assertThat(array.indexOf("notexist")).isEqualTo(-1);
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyTransformToNativeArray(Array<String> array) {
    // then:
    assertThat(array.toArray()).isEqualTo(new String[]{
            "First",
            "Second",
            "Third",
            "  "
        });
    assertThat(array.toArray(new String[4])).isEqualTo(new String[]{
            "First",
            "Second",
            "Third",
            "  "
        });
    assertThat(array.toArray(String.class)).isEqualTo(new String[]{
            "First",
            "Second",
            "Third",
            "  "
        });
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyCheckOnContains(Array<String> array) {
    // when/then:
    assertThat(array.contains("First")).isTrue();
    assertThat(array.contains("  ")).isTrue();
    assertThat(array.contains("notexist")).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyCheckOnContainsAllByArray(Array<String> array) {
    // given:
    Array<String> check1 = Array.of("First");
    Array<String> check2 = Array.of("Second", "Third");
    Array<String> check3 = Array.of("Second", "Third", "noexist");
    // when/then:
    assertThat(array.containsAll(check1)).isTrue();
    assertThat(array.containsAll(check2)).isTrue();
    assertThat(array.containsAll(check3)).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyCheckOnContainsAllByCollection(Array<String> array) {
    // given:
    List<String> check1 = List.of("First");
    List<String> check2 = List.of("Second", "Third");
    List<String> check3 = List.of("Second", "Third", "noexist");
    // when/then:
    assertThat(array.containsAll(check1)).isTrue();
    assertThat(array.containsAll(check2)).isTrue();
    assertThat(array.containsAll(check3)).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateArrays")
  void shouldCorrectlyCheckOnContainsAllByNativeArray(Array<String> array) {
    // given:
    Object[] check1 = List.of("First").toArray();
    Object[] check2 = List.of("Second", "Third").toArray();
    Object[] check3 = List.of("Second", "Third", "noexist").toArray();
    // when/then:
    assertThat(array.containsAll(check1)).isTrue();
    assertThat(array.containsAll(check2)).isTrue();
    assertThat(array.containsAll(check3)).isFalse();
  }

  private static Stream<Arguments> generateArrays() {
    Array<String> array = Array.of("First", "Second", "Third", "  ");
    MutableArray<String> mutableArray = ArrayFactory.mutableArray(String.class);
    mutableArray.addAll(array);
    MutableArray<String> copyOnModifyArray = ArrayFactory.copyOnModifyArray(String.class);
    copyOnModifyArray.addAll(array);
    LockableArray<String> stampedLockBasedArray = ArrayFactory.stampedLockBasedArray(String.class);
    stampedLockBasedArray.addAll(array);
    return Stream.of(
        Arguments.of(array),
        Arguments.of(mutableArray),
        Arguments.of(copyOnModifyArray),
        Arguments.of(stampedLockBasedArray));
  }

  private static Stream<Arguments> generateArraysWithDuplicates() {
    Array<String> array = Array.of("First", "Second", "Third", "  ", "First", "Second", "Third");
    MutableArray<String> mutableArray = ArrayFactory.mutableArray(String.class);
    mutableArray.addAll(array);
    MutableArray<String> copyOnModifyArray = ArrayFactory.copyOnModifyArray(String.class);
    copyOnModifyArray.addAll(array);
    LockableArray<String> stampedLockBasedArray = ArrayFactory.stampedLockBasedArray(String.class);
    stampedLockBasedArray.addAll(array);
    return Stream.of(
        Arguments.of(array),
        Arguments.of(mutableArray),
        Arguments.of(copyOnModifyArray),
        Arguments.of(stampedLockBasedArray));
  }
}
