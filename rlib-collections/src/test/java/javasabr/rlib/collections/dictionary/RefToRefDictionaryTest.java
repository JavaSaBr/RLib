package javasabr.rlib.collections.dictionary;

import static javasabr.rlib.collections.dictionary.RefToRefDictionary.entry;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.common.tuple.Tuple;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RefToRefDictionaryTest {

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldProvideCorrectSize(RefToRefDictionary<String, String> dictionary) {
    // then:
    assertThat(dictionary.size()).isEqualTo(5);
    assertThat(dictionary.isEmpty()).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateEmptyDictionaries")
  void shouldConfirmThatDictionaryIsEmpty(RefToRefDictionary<String, String> dictionary) {
    // then:
    assertThat(dictionary.isEmpty()).isTrue();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldFindValuesByKeys(RefToRefDictionary<String, String> dictionary) {
    // then:
    assertThat(dictionary.get("key1")).isEqualTo("val1");
    assertThat(dictionary.get("key3")).isEqualTo("val3");
    assertThat(dictionary.get("key4")).isEqualTo("val4");
    assertThat(dictionary.get("key10")).isNull();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldFindValuesByKeysOrReturnDefault(RefToRefDictionary<String, String> dictionary) {
    // then:
    assertThat(dictionary.get("key1")).isEqualTo("val1");
    assertThat(dictionary.get("key10")).isNull();
    assertThat(dictionary.getOrDefault("key10", "def1")).isEqualTo("def1");
    assertThat(dictionary.getOrDefault("key30", "def2")).isEqualTo("def2");
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldReturnOptionalValuesByKeys(RefToRefDictionary<String, String> dictionary) {
    // then:
    assertThat(dictionary.getOptional("key1")).contains("val1");
    assertThat(dictionary.getOptional("key10")).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldCheckContainsKeys(RefToRefDictionary<String, String> dictionary) {
    // then:
    assertThat(dictionary.containsKey("key1")).isTrue();
    assertThat(dictionary.containsKey("key2")).isTrue();
    assertThat(dictionary.containsKey("key5")).isTrue();
    assertThat(dictionary.containsKey("key10")).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldCheckContainsValues(RefToRefDictionary<String, String> dictionary) {
    // then:
    assertThat(dictionary.containsValue("val1")).isTrue();
    assertThat(dictionary.containsValue("val3")).isTrue();
    assertThat(dictionary.containsValue("val4")).isTrue();
    assertThat(dictionary.containsValue("val10")).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldHaveExpectedKeys(RefToRefDictionary<String, String> dictionary) {
    // given:
    var expectedArray = Array.typed(String.class, "key4", "key3", "key5", "key2", "key1");
    var expectedSet = Set.copyOf(expectedArray.toList());

    // when:
    Array<String> keys = dictionary.keys(String.class);

    // then:
    assertThat(keys).isEqualTo(expectedArray);

    // when:
    Array<String> keys2 = dictionary.keys(MutableArray.ofType(String.class));

    // then:
    assertThat(keys2).isEqualTo(expectedArray);

    // when:
    Set<String> keys3 = dictionary.keys(new HashSet<>());

    // then:
    assertThat(keys3).isEqualTo(expectedSet);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldHaveExpectedValues(RefToRefDictionary<String, String> dictionary) {
    // given:
    var expectedArray = Array.typed(String.class, "val4", "val3", "val5", "val2", "val1");
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
  void shouldHaveExpectedResultFromForEach(RefToRefDictionary<String, String> dictionary) {
    // given:
    var expectedArray = Array.typed(String.class, "val4", "val3", "val5", "val2", "val1");
    var expectedPairs = Array.typed(Tuple.class,
        new Tuple<>("key4", "val4"),
        new Tuple<>("key3", "val3"),
        new Tuple<>("key5", "val5"),
        new Tuple<>("key2", "val2"),
        new Tuple<>("key1", "val1"));

    // when:
    MutableArray<String> values = MutableArray.ofType(String.class);
    dictionary.forEach(values::add);

    // then:
    assertThat(values).isEqualTo(expectedArray);

    // when:
    MutableArray<Tuple> pairs = MutableArray.ofType(Tuple.class);
    dictionary.forEach((key, value) -> pairs.add(new Tuple<>(key, value)));

    // then:
    assertThat(pairs).isEqualTo(expectedPairs);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldCheckEqualsCorrectly(RefToRefDictionary<String, String> dictionary) {
    // given:
    RefToRefDictionary<String, String> theSame = RefToRefDictionary.ofEntries(
        entry("key1", "val1"),
        entry("key2", "val2"),
        entry("key3", "val3"),
        entry("key4", "val4"),
        entry("key5", "val5"));
    RefToRefDictionary<String, String> notTheSame1 = RefToRefDictionary.ofEntries(
        entry("key1", "val1"),
        entry("key2", "val2"),
        entry("key3", "val3"),
        entry("key4", "val46"),
        entry("key5", "val5"));
    RefToRefDictionary<String, String> notTheSame2 = RefToRefDictionary.ofEntries(
        entry("key1", "val1"),
        entry("key21", "val2"),
        entry("key3", "val3"),
        entry("key4", "val4"),
        entry("key5", "val5"));

    // when/then:
    assertThat(dictionary).isEqualTo(theSame);
    assertThat(dictionary).isNotEqualTo(notTheSame1);
    assertThat(dictionary).isNotEqualTo(notTheSame2);
  }
  
  private static Stream<Arguments> generateDictionaries() {

    RefToRefDictionary<String, String> source = RefToRefDictionary.ofEntries(
        entry("key1", "val1"),
        entry("key2", "val2"),
        entry("key3", "val3"),
        entry("key4", "val4"),
        entry("key5", "val5"));

    return Stream.of(
        Arguments.of(source),
        Arguments.of(DictionaryFactory.mutableRefToRefDictionary().append(source)),
        Arguments.of(DictionaryFactory.stampedLockBasedRefToRefDictionary().append(source)));
  }

  private static Stream<Arguments> generateEmptyDictionaries() {
    return Stream.of(
        Arguments.of(RefToRefDictionary.empty()),
        Arguments.of(DictionaryFactory.mutableRefToRefDictionary()),
        Arguments.of(DictionaryFactory.stampedLockBasedRefToRefDictionary()));
  }
}
