package javasabr.rlib.collections.dictionary;

import static javasabr.rlib.collections.dictionary.IntToRefDictionary.entry;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MutableIntToRefDictionaryTest {

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldPutNewPairs(MutableIntToRefDictionary<String> dictionary) {
    // when:
    dictionary.put(1, "val1");
    dictionary.put(4, "val4");
    dictionary.put(7, "val7");
    dictionary.put(55, "val55");
    // then:
    assertThat(dictionary.get(1)).isEqualTo("val1");
    assertThat(dictionary.get(4)).isEqualTo("val4");
    assertThat(dictionary.get(55)).isEqualTo("val55");
    assertThat(dictionary.containsKey(1)).isTrue();
    assertThat(dictionary.containsKey(4)).isTrue();
    assertThat(dictionary.containsKey(55)).isTrue();
    assertThat(dictionary.get(10)).isNull();
    assertThat(dictionary.size()).isEqualTo(4);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldPutOptionalNewPairs(MutableIntToRefDictionary<String> dictionary) {
    // when:
    dictionary.put(4, "val4");
    dictionary.put(7, "val7");
    dictionary.put(55, "val55");

    Optional<String> prevForKey1 = dictionary.putOptional(1, "val1");
    Optional<String> prevForKey55 = dictionary.putOptional(55, "val56");

    // then:
    assertThat(prevForKey1).isEmpty();
    assertThat(prevForKey55).contains("val55");
    assertThat(dictionary.get(55)).isEqualTo("val56");
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldRemoveByKeys(MutableIntToRefDictionary<String> dictionary) {
    // given:
    dictionary.put(1, "val1");
    dictionary.put(4, "val4");
    dictionary.put(7, "val7");
    dictionary.put(55, "val55");

    // when:
    String removed1 = dictionary.remove(1);
    String removed2 = dictionary.remove(55);

    // then:
    assertThat(removed1).isEqualTo("val1");
    assertThat(removed2).isEqualTo("val55");
    assertThat(dictionary.get(4)).isEqualTo("val4");
    assertThat(dictionary.get(7)).isEqualTo("val7");
    assertThat(dictionary.get(1)).isNull();
    assertThat(dictionary.get(55)).isNull();
    assertThat(dictionary.containsKey(1)).isFalse();
    assertThat(dictionary.containsKey(55)).isFalse();
    assertThat(dictionary.size()).isEqualTo(2);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldRemoveOptionalByKeys(MutableIntToRefDictionary<String> dictionary) {
    // given:
    dictionary.put(1, "val1");
    dictionary.put(4, "val4");
    dictionary.put(7, "val7");
    dictionary.put(55, "val55");

    // when:
    Optional<String> removed1 = dictionary.removeOptional(1);
    Optional<String> removed2 = dictionary.removeOptional(75);

    // then:
    assertThat(removed1).contains("val1");
    assertThat(removed2).isEmpty();
    assertThat(dictionary).hasSize(3);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldCorrectlyComputeOnGet(MutableIntToRefDictionary<String> dictionary) {
    // when:
    var computed1 = dictionary.getOrCompute(1, () -> "val1");
    var computed2 = dictionary.getOrCompute(4, key -> "val4");
    var computed3 = dictionary.getOrCompute(10, 5, arg1 -> "val10");

    // then:
    assertThat(computed1).isEqualTo("val1");
    assertThat(computed2).isEqualTo("val4");
    assertThat(computed3).isEqualTo("val10");
    assertThat(dictionary.get(1)).isEqualTo("val1");
    assertThat(dictionary.get(4)).isEqualTo("val4");
    assertThat(dictionary.get(10)).isEqualTo("val10");
    assertThat(dictionary.containsKey(1)).isTrue();
    assertThat(dictionary.containsKey(4)).isTrue();
    assertThat(dictionary.containsKey(10)).isTrue();
    assertThat(dictionary.size()).isEqualTo(3);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldAppendDictionary(MutableIntToRefDictionary<String> dictionary) {
    // given:
    IntToRefDictionary<String> source = IntToRefDictionary.ofEntries(
        entry(1, "val1"),
        entry(2, "val2"),
        entry(3, "val3"),
        entry(4, "val4"),
        entry(5, "val5"));

    // when:
    dictionary.append(source);

    // then:
    assertThat(dictionary.get(1)).isEqualTo("val1");
    assertThat(dictionary.get(4)).isEqualTo("val4");
    assertThat(dictionary.get(5)).isEqualTo("val5");
    assertThat(dictionary.containsKey(3)).isTrue();
    assertThat(dictionary.containsKey(55)).isFalse();
    assertThat(dictionary.get(10)).isNull();
    assertThat(dictionary.size()).isEqualTo(5);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldBeEmptyAfterClear(MutableIntToRefDictionary<String> dictionary) {
    // when:
    dictionary.put(1, "val1");
    dictionary.put(4, "val4");

    // then:
    assertThat(dictionary.isEmpty()).isFalse();

    // when:
    dictionary.clear();

    // then:
    assertThat(dictionary.isEmpty()).isTrue();
    assertThat(dictionary.get(1)).isNull();
    assertThat(dictionary.containsKey(1)).isFalse();
  }

  private static Stream<Arguments> generateDictionaries() {
    return Stream.of(
        Arguments.of(MutableIntToRefDictionary.ofTypes(String.class)),
        Arguments.of(DictionaryFactory.mutableIntToRefDictionary()),
        Arguments.of(DictionaryFactory.gcOptimizedIntToRefDictionary()));
  }
}
