package javasabr.rlib.collections.dictionary;

import static javasabr.rlib.collections.dictionary.LongToRefDictionary.entry;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.LongArray;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.common.tuple.LongRefTuple;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LongToRefDictionaryTest {

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldProvideCorrectSize(LongToRefDictionary<String> dictionary) {
    // then:
    assertThat(dictionary.size()).isEqualTo(5);
    assertThat(dictionary.isEmpty()).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateEmptyDictionaries")
  void shouldConfirmThatDictionaryIsEmpty(LongToRefDictionary<String> dictionary) {
    // then:
    assertThat(dictionary.isEmpty()).isTrue();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldFindValuesByKeys(LongToRefDictionary<String> dictionary) {
    // then:
    assertThat(dictionary.get(1)).isEqualTo("val1");
    assertThat(dictionary.get(3)).isEqualTo("val3");
    assertThat(dictionary.get(4)).isEqualTo("val4");
    assertThat(dictionary.get(10)).isNull();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldFindValuesByKeysOrReturnDefault(LongToRefDictionary<String> dictionary) {
    // then:
    assertThat(dictionary.get(1)).isEqualTo("val1");
    assertThat(dictionary.get(10)).isNull();
    assertThat(dictionary.getOrDefault(10, "def1")).isEqualTo("def1");
    assertThat(dictionary.getOrDefault(30, "def2")).isEqualTo("def2");
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldReturnOptionalValuesByKeys(LongToRefDictionary<String> dictionary) {
    // then:
    assertThat(dictionary.getOptional(1)).contains("val1");
    assertThat(dictionary.getOptional(10)).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldCheckContainsKeys(LongToRefDictionary<String> dictionary) {
    // then:
    assertThat(dictionary.containsKey(1)).isTrue();
    assertThat(dictionary.containsKey(2)).isTrue();
    assertThat(dictionary.containsKey(5)).isTrue();
    assertThat(dictionary.containsKey(10)).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldCheckContainsValues(LongToRefDictionary<String> dictionary) {
    // then:
    assertThat(dictionary.containsValue("val1")).isTrue();
    assertThat(dictionary.containsValue("val3")).isTrue();
    assertThat(dictionary.containsValue("val4")).isTrue();
    assertThat(dictionary.containsValue("val10")).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldHaveExpectedKeys(LongToRefDictionary<String> dictionary) {
    // given:
    var expectedArray = Array.typed(Long.class, 1L, 2L, 3L, 4L, 5L);
    var expectedLongArray = LongArray.of(1, 2, 3, 4, 5);
    var expectedSet = Set.copyOf(expectedArray.toList());

    // when:
    Array<Long> keys = dictionary.keys(Long.class);

    // then:
    assertThat(keys).isEqualTo(expectedArray);

    // when:
    Array<Long> keys2 = dictionary.keys(MutableArray.ofType(Long.class));

    // then:
    assertThat(keys2).isEqualTo(expectedArray);

    // when:
    Set<Long> keys3 = dictionary.keys(new HashSet<>());

    // then:
    assertThat(keys3).isEqualTo(expectedSet);

    // when:
    LongArray longArray = dictionary.keys();

    // then:
    assertThat(longArray).isEqualTo(expectedLongArray);

    // when:
    longArray = dictionary.keys(ArrayFactory.mutableLongArray());

    // then:
    assertThat(longArray).isEqualTo(expectedLongArray);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldHaveExpectedValues(LongToRefDictionary<String> dictionary) {
    // given:
    var expectedArray = Array.typed(String.class, "val1", "val2", "val3", "val4", "val5");
    var expectedSet = Set.copyOf(expectedArray.toList());

    // when:
    Array<String> values = dictionary.values(String.class);

    // then:
    assertThat(values).isEqualTo(expectedArray);

    // when:
    Array<String> values2 = dictionary.values(MutableArray.ofType(String.class));

    // then:
    assertThat(values2).isEqualTo(expectedArray);

    // when:
    Set<String> values3 = dictionary.values(new HashSet<>());

    // then:
    assertThat(values3).isEqualTo(expectedSet);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldHaveExpectedResultFromForEach(LongToRefDictionary<String> dictionary) {
    // given:
    var expectedArray = Array.typed(String.class, "val1", "val2", "val3", "val4", "val5");
    var expectedPairs = Array.typed(LongRefTuple.class,
        new LongRefTuple<>(1, "val1"),
        new LongRefTuple<>(2, "val2"),
        new LongRefTuple<>(3, "val3"),
        new LongRefTuple<>(4, "val4"),
        new LongRefTuple<>(5, "val5"));

    // when:
    MutableArray<String> values = MutableArray.ofType(String.class);
    dictionary.forEach(values::add);

    // then:
    assertThat(values).isEqualTo(expectedArray);

    // when:
    MutableArray<LongRefTuple> pairs = MutableArray.ofType(LongRefTuple.class);
    dictionary.forEach((key, value) -> pairs.add(new LongRefTuple<>(key, value)));

    // then:
    assertThat(pairs).isEqualTo(expectedPairs);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldCheckEqualsCorrectly(LongToRefDictionary<String> dictionary) {
    // given:
    LongToRefDictionary<String> theSame = LongToRefDictionary.ofEntries(
        entry(1, "val1"),
        entry(2, "val2"),
        entry(3, "val3"),
        entry(4, "val4"),
        entry(5, "val5"));
    LongToRefDictionary<String> notTheSame1 = LongToRefDictionary.ofEntries(
        entry(1, "val1"),
        entry(2, "val2"),
        entry(33, "val3"),
        entry(4, "val4"),
        entry(5, "val5"));
    LongToRefDictionary<String> notTheSame2 = LongToRefDictionary.ofEntries(
        entry(1, "val1"),
        entry(2, "val2"),
        entry(3, "val3"),
        entry(4, "val44"),
        entry(5, "val5"));

    // when/then:
    assertThat(dictionary).isEqualTo(theSame);
    assertThat(dictionary).isNotEqualTo(notTheSame1);
    assertThat(dictionary).isNotEqualTo(notTheSame2);
  }
  
  private static Stream<Arguments> generateDictionaries() {

    LongToRefDictionary<String> source = LongToRefDictionary.ofEntries(
        entry(1, "val1"),
        entry(2, "val2"),
        entry(3, "val3"),
        entry(4, "val4"),
        entry(5, "val5"));

    return Stream.of(
        Arguments.of(source),
        Arguments.of(DictionaryFactory.mutableLongToRefDictionary().append(source)));
  }

  private static Stream<Arguments> generateEmptyDictionaries() {
    return Stream.of(
        Arguments.of(LongToRefDictionary.empty()),
        Arguments.of(DictionaryFactory.mutableLongToRefDictionary()));
  }
}
