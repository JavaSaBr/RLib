package javasabr.rlib.collections.dictionary;

import static javasabr.rlib.collections.dictionary.IntToRefDictionary.entry;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayFactory;
import javasabr.rlib.collections.array.IntArray;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.common.tuple.IntRefTuple;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class IntToRefDictionaryTest {

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldProvideCorrectSize(IntToRefDictionary<String> dictionary) {
    // then:
    assertThat(dictionary.size()).isEqualTo(5);
    assertThat(dictionary.isEmpty()).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateEmptyDictionaries")
  void shouldConfirmThatDictionaryIsEmpty(IntToRefDictionary<String> dictionary) {
    // then:
    assertThat(dictionary.isEmpty()).isTrue();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldFindValuesByKeys(IntToRefDictionary<String> dictionary) {
    // then:
    assertThat(dictionary.get(1)).isEqualTo("val1");
    assertThat(dictionary.get(3)).isEqualTo("val3");
    assertThat(dictionary.get(4)).isEqualTo("val4");
    assertThat(dictionary.get(10)).isNull();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldFindValuesByKeysOrReturnDefault(IntToRefDictionary<String> dictionary) {
    // then:
    assertThat(dictionary.get(1)).isEqualTo("val1");
    assertThat(dictionary.get(10)).isNull();
    assertThat(dictionary.getOrDefault(10, "def1")).isEqualTo("def1");
    assertThat(dictionary.getOrDefault(30, "def2")).isEqualTo("def2");
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldReturnOptionalValuesByKeys(IntToRefDictionary<String> dictionary) {
    // then:
    assertThat(dictionary.getOptional(1)).contains("val1");
    assertThat(dictionary.getOptional(10)).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldCheckContainsKeys(IntToRefDictionary<String> dictionary) {
    // then:
    assertThat(dictionary.containsKey(1)).isTrue();
    assertThat(dictionary.containsKey(2)).isTrue();
    assertThat(dictionary.containsKey(5)).isTrue();
    assertThat(dictionary.containsKey(10)).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldCheckContainsValues(IntToRefDictionary<String> dictionary) {
    // then:
    assertThat(dictionary.containsValue("val1")).isTrue();
    assertThat(dictionary.containsValue("val3")).isTrue();
    assertThat(dictionary.containsValue("val4")).isTrue();
    assertThat(dictionary.containsValue("val10")).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldHaveExpectedKeys(IntToRefDictionary<String> dictionary) {
    // given:
    var expectedArray = Array.typed(Integer.class, 1, 2, 3, 4, 5);
    var expectedIntArray = IntArray.of(1, 2, 3, 4, 5);
    var expectedSet = Set.copyOf(expectedArray.toList());

    // when:
    Array<Integer> keys = dictionary.keys(Integer.class);

    // then:
    assertThat(keys).isEqualTo(expectedArray);

    // when:
    Array<Integer> keys2 = dictionary.keys(MutableArray.ofType(Integer.class));

    // then:
    assertThat(keys2).isEqualTo(expectedArray);

    // when:
    Set<Integer> keys3 = dictionary.keys(new HashSet<>());

    // then:
    assertThat(keys3).isEqualTo(expectedSet);

    // when:
    IntArray intKeys = dictionary.keys();

    // then:
    assertThat(intKeys).isEqualTo(expectedIntArray);

    // when:
    intKeys = dictionary.keys(ArrayFactory.mutableIntArray());

    // then:
    assertThat(intKeys).isEqualTo(expectedIntArray);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldHaveExpectedValues(IntToRefDictionary<String> dictionary) {
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
  void shouldHaveExpectedResultFromForEach(IntToRefDictionary<String> dictionary) {
    // given:
    var expectedArray = Array.typed(String.class, "val1", "val2", "val3", "val4", "val5");
    var expectedPairs = Array.typed(IntRefTuple.class,
        new IntRefTuple<>(1, "val1"),
        new IntRefTuple<>(2, "val2"),
        new IntRefTuple<>(3, "val3"),
        new IntRefTuple<>(4, "val4"),
        new IntRefTuple<>(5, "val5"));

    // when:
    MutableArray<String> values = MutableArray.ofType(String.class);
    dictionary.forEach(values::add);

    // then:
    assertThat(values).isEqualTo(expectedArray);

    // when:
    MutableArray<IntRefTuple> pairs = MutableArray.ofType(IntRefTuple.class);
    dictionary.forEach((key, value) -> pairs.add(new IntRefTuple<>(key, value)));

    // then:
    assertThat(pairs).isEqualTo(expectedPairs);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldCheckEqualsCorrectly(IntToRefDictionary<String> dictionary) {
    // given:
    IntToRefDictionary<String> theSame = IntToRefDictionary.ofEntries(
        entry(1, "val1"),
        entry(2, "val2"),
        entry(3, "val3"),
        entry(4, "val4"),
        entry(5, "val5"));
    IntToRefDictionary<String> notTheSame1 = IntToRefDictionary.ofEntries(
        entry(1, "val1"),
        entry(2, "val2"),
        entry(33, "val3"),
        entry(4, "val4"),
        entry(5, "val5"));
    IntToRefDictionary<String> notTheSame2 = IntToRefDictionary.ofEntries(
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

    IntToRefDictionary<String> source = IntToRefDictionary.ofEntries(
        entry(1, "val1"),
        entry(2, "val2"),
        entry(3, "val3"),
        entry(4, "val4"),
        entry(5, "val5"));

    return Stream.of(
        Arguments.of(source),
        Arguments.of(DictionaryFactory.mutableIntToRefDictionary().append(source)),
        Arguments.of(DictionaryFactory.gcOptimizedIntToRefDictionary().append(source)));
  }

  private static Stream<Arguments> generateEmptyDictionaries() {
    return Stream.of(
        Arguments.of(IntToRefDictionary.empty()),
        Arguments.of(DictionaryFactory.mutableIntToRefDictionary()),
        Arguments.of(DictionaryFactory.gcOptimizedIntToRefDictionary()));
  }
}
