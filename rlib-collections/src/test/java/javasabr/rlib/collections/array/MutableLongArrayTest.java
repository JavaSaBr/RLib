package javasabr.rlib.collections.array;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MutableLongArrayTest {

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should correctly add elements")
  void shouldCorrectlyAddElements(MutableLongArray mutableArray) {
    // when:
    mutableArray.add(10);

    // then:
    assertThat(mutableArray.size()).isEqualTo(1);
    assertThat(mutableArray.get(0)).isEqualTo(10);

    // when:
    mutableArray.add(20);

    // then:
    assertThat(mutableArray.size()).isEqualTo(2);
    assertThat(mutableArray.get(1)).isEqualTo(20);

    // when:
    mutableArray.add(30);

    // then:
    assertThat(mutableArray.size()).isEqualTo(3);
    assertThat(mutableArray.get(2)).isEqualTo(30);
    assertThat(mutableArray).isEqualTo(LongArray.of(10, 20, 30));

    // when:
    for(int i = 0; i < 100; i++) {
      mutableArray.add(100 + i);
    }

    // then:
    assertThat(mutableArray.size()).isEqualTo(103);
    assertThat(mutableArray.get(102)).isEqualTo(199);
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should correctly remove elements")
  void shouldCorrectlyRemoveElements(MutableLongArray mutableArray) {
    // given:
    for(int i = 0; i < 101; i++) {
      mutableArray.add(100 + i);
    }

    // when:
    mutableArray.remove(100);

    // then:
    assertThat(mutableArray.size()).isEqualTo(100);
    assertThat(mutableArray.get(0)).isEqualTo(101);

    // when:
    mutableArray.remove(110);

    // then:
    assertThat(mutableArray.size()).isEqualTo(99);
    assertThat(mutableArray.get(0)).isEqualTo(101);
    assertThat(mutableArray.get(8)).isEqualTo(109);
    assertThat(mutableArray.get(9)).isEqualTo(111);

    // when:
    mutableArray.remove(200);

    // then:
    assertThat(mutableArray.size()).isEqualTo(98);
    assertThat(mutableArray.get(97)).isEqualTo(199);
    assertThat(mutableArray.get(96)).isEqualTo(198);
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should correctly replace elements")
  void shouldCorrectlyReplaceElements(MutableLongArray mutableArray) {
    // given:
    for(int i = 0; i < 101; i++) {
      mutableArray.add(100 + i);
    }

    // when:
    mutableArray.replace(0, 200);

    // then:
    assertThat(mutableArray.size()).isEqualTo(101);
    assertThat(mutableArray.get(0)).isEqualTo(200);

    // when:
    mutableArray.replace(11, 211);

    // then:
    assertThat(mutableArray.size()).isEqualTo(101);
    assertThat(mutableArray.get(11)).isEqualTo(211);
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should correctly add batch elements")
  void shouldCorrectlyAddBatchElements(MutableLongArray mutableArray) {
    // given:
    for(int i = 0; i < 21; i++) {
      mutableArray.add(100 + i);
    }

    var anotherArray = LongArray.of(31, 32, 33, 34, 35, 36);
    var anotherNativeArray = new long[] {41, 42, 43, 44, 45, 46};

    // when:
    mutableArray.addAll(anotherArray);

    // then:
    assertThat(mutableArray.size()).isEqualTo(27);
    assertThat(mutableArray.get(0)).isEqualTo(100);
    assertThat(mutableArray.get(20)).isEqualTo(120);
    assertThat(mutableArray.get(21)).isEqualTo(31);
    assertThat(mutableArray.get(26)).isEqualTo(36);

    // when:
    mutableArray.addAll(anotherNativeArray);

    // then:
    assertThat(mutableArray.size()).isEqualTo(33);
    assertThat(mutableArray.get(0)).isEqualTo(100);
    assertThat(mutableArray.get(20)).isEqualTo(120);
    assertThat(mutableArray.get(21)).isEqualTo(31);
    assertThat(mutableArray.get(26)).isEqualTo(36);
    assertThat(mutableArray.get(27)).isEqualTo(41);
    assertThat(mutableArray.get(32)).isEqualTo(46);
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should correctly remove batch elements")
  void shouldCorrectlyRemoveBatchElements(MutableLongArray mutableArray) {
    // given:
    for(int i = 0; i < 21; i++) {
      mutableArray.add(100 + i);
    }

    var anotherArray = LongArray.of(31, 32, 33, 34, 35, 36);
    var anotherNativeArray = new long[] {41, 42, 43, 44, 45, 46};

    mutableArray.addAll(anotherArray);
    mutableArray.addAll(anotherNativeArray);

    // when:
    mutableArray.removeAll(anotherArray);

    // then:
    assertThat(mutableArray.size()).isEqualTo(27);
    assertThat(mutableArray.get(0)).isEqualTo(100);
    assertThat(mutableArray.get(20)).isEqualTo(120);
    assertThat(mutableArray.get(21)).isEqualTo(41);
    assertThat(mutableArray.get(26)).isEqualTo(46);

    // when:
    mutableArray.removeAll(anotherNativeArray);

    // then:
    assertThat(mutableArray.size()).isEqualTo(21);
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should clear array")
  void shouldClearArray(MutableLongArray mutableArray) {
    // when:
    for(int i = 0; i < 21; i++) {
      mutableArray.add(100 + i);
    }

    // then:
    assertThat(mutableArray.size()).isEqualTo(21);
    assertThat(mutableArray.get(0)).isEqualTo(100);
    assertThat(mutableArray.get(20)).isEqualTo(120);

    // when:
    mutableArray.clear();

    // then:
    assertThat(mutableArray.size()).isEqualTo(0);
    assertThat(mutableArray.toArray()).isEqualTo(new long[0]);
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should trim to size wrapped array")
  void shouldTrimToSizeWrappedArray(MutableLongArray mutableArray) {
    // given:
    UnsafeMutableLongArray unsafe = mutableArray.asUnsafe();

    // when:
    for(int i = 0; i < 100; i++) {
      mutableArray.add(100 + i);
    }

    // then:
    assertThat(mutableArray.size())
        .isEqualTo(100);

    // when:
    for(int i = 0; i < 30; i++) {
      mutableArray.removeByIndex(i);
    }

    // then:
    assertThat(mutableArray.size())
        .isEqualTo(70);
    assertThat(unsafe.wrapped().length)
        .isEqualTo(109);

    // when:
    unsafe.trimToSize();

    // then:
    assertThat(unsafe.wrapped().length)
        .isEqualTo(70);
  }

  @ParameterizedTest
  @MethodSource("generateMutableArrays")
  @DisplayName("should render to string correctly")
  void shouldRenderToStringCorrectly(MutableLongArray mutableArray) {
    // when:
    for(int i = 0; i < 10; i++) {
      mutableArray.add(20 + i);
    }
    // then:
    assertThat(mutableArray.toString())
        .isEqualTo("[20,21,22,23,24,25,26,27,28,29]");
  }

  private static Stream<Arguments> generateMutableArrays() {
    return Stream.of(
        Arguments.of(ArrayFactory.mutableLongArray()));
  }
}
