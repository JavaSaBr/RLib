package javasabr.rlib.collections.dictionary;

import static javasabr.rlib.collections.dictionary.RefToRefDictionary.entry;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MutableRefToRefDictionaryTest {

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldPutNewPairs(MutableRefToRefDictionary<String, String> dictionary) {
    // when:
    dictionary.put("key1", "val1");
    dictionary.put("key4", "val4");
    dictionary.put("key7", "val7");
    dictionary.put("key55", "val55");
    // then:
    assertThat(dictionary.get("key1")).isEqualTo("val1");
    assertThat(dictionary.get("key4")).isEqualTo("val4");
    assertThat(dictionary.get("key55")).isEqualTo("val55");
    assertThat(dictionary.containsKey("key1")).isTrue();
    assertThat(dictionary.containsKey("key4")).isTrue();
    assertThat(dictionary.containsKey("key55")).isTrue();
    assertThat(dictionary.get("key10")).isNull();
    assertThat(dictionary.size()).isEqualTo(4);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldPutIfAbsentNewPairs(MutableRefToRefDictionary<String, String> dictionary) {
    // given:
    dictionary.put("key1", "val1");
    dictionary.put("key4", "val4");
    dictionary.put("key7", "val7");
    dictionary.put("key55", "val55");
    // when:
    String result1 = dictionary.putIfAbsent("key1", "val1_1");
    String result2 = dictionary.putIfAbsent("key4", "val4_1");
    String result3 = dictionary.putIfAbsent("key44", "val44");
    // then:
    assertThat(result1).isNull();
    assertThat(result2).isNull();
    assertThat(result3).isNull();
    assertThat(dictionary.get("key1")).isEqualTo("val1");
    assertThat(dictionary.get("key4")).isEqualTo("val4");
    assertThat(dictionary.get("key55")).isEqualTo("val55");
    assertThat(dictionary.get("key44")).isEqualTo("val44");
    assertThat(dictionary.containsKey("key1")).isTrue();
    assertThat(dictionary.containsKey("key4")).isTrue();
    assertThat(dictionary.containsKey("key55")).isTrue();
    assertThat(dictionary.containsKey("key44")).isTrue();
    assertThat(dictionary.size()).isEqualTo(5);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldPutOptionalNewPairs(MutableRefToRefDictionary<String, String> dictionary) {
    // when:
    dictionary.put("key4", "val4");
    dictionary.put("key7", "val7");
    dictionary.put("key55", "val55");

    Optional<String> prevForKey1 = dictionary.putOptional("key1", "val1");
    Optional<String> prevForKey55 = dictionary.putOptional("key55", "val56");

    // then:
    assertThat(prevForKey1).isEmpty();
    assertThat(prevForKey55).contains("val55");
    assertThat(dictionary.get("key55")).isEqualTo("val56");
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldRemoveByKeys(MutableRefToRefDictionary<String, String> dictionary) {
    // given:
    dictionary.put("key1", "val1");
    dictionary.put("key4", "val4");
    dictionary.put("key7", "val7");
    dictionary.put("key55", "val55");

    // when:
    String removed1 = dictionary.remove("key1");
    String removed2 = dictionary.remove("key55");

    // then:
    assertThat(removed1).isEqualTo("val1");
    assertThat(removed2).isEqualTo("val55");
    assertThat(dictionary.get("key4")).isEqualTo("val4");
    assertThat(dictionary.get("key7")).isEqualTo("val7");
    assertThat(dictionary.get("key1")).isNull();
    assertThat(dictionary.get("key55")).isNull();
    assertThat(dictionary.containsKey("key1")).isFalse();
    assertThat(dictionary.containsKey("key55")).isFalse();
    assertThat(dictionary.size()).isEqualTo(2);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldRemoveByKeysWithExpectedValues(MutableRefToRefDictionary<String, String> dictionary) {
    // given:
    dictionary.put("key1", "val1");
    dictionary.put("key4", "val4");
    dictionary.put("key7", "val7");
    dictionary.put("key55", "val55");

    // when:
    boolean removed1 = dictionary.remove("key1", "val2");
    boolean removed2 = dictionary.remove("key55", "val55");

    // then:
    assertThat(removed1).isFalse();
    assertThat(removed2).isTrue();
    assertThat(dictionary.get("key4")).isEqualTo("val4");
    assertThat(dictionary.get("key7")).isEqualTo("val7");
    assertThat(dictionary.get("key1")).isEqualTo("val1");
    assertThat(dictionary.get("key55")).isNull();
    assertThat(dictionary.containsKey("key1")).isTrue();
    assertThat(dictionary.containsKey("key55")).isFalse();
    assertThat(dictionary.size()).isEqualTo(3);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldRemoveOptionalByKeys(MutableRefToRefDictionary<String, String> dictionary) {
    // given:
    dictionary.put("key1", "val1");
    dictionary.put("key4", "val4");
    dictionary.put("key7", "val7");
    dictionary.put("key55", "val55");

    // when:
    Optional<String> removed1 = dictionary.removeOptional("key1");
    Optional<String> removed2 = dictionary.removeOptional("key75");

    // then:
    assertThat(removed1).contains("val1");
    assertThat(removed2).isEmpty();
    assertThat(dictionary).hasSize(3);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldCorrectlyComputeOnGet(MutableRefToRefDictionary<String, String> dictionary) {
    // when:
    var computed1 = dictionary.getOrCompute("key1", () -> "val1");
    var computed2 = dictionary.getOrCompute("key4", key -> "val4");
    var computed3 = dictionary.getOrCompute("key10", 5, arg1 -> "val10");

    // then:
    assertThat(computed1).isEqualTo("val1");
    assertThat(computed2).isEqualTo("val4");
    assertThat(computed3).isEqualTo("val10");
    assertThat(dictionary.get("key1")).isEqualTo("val1");
    assertThat(dictionary.get("key4")).isEqualTo("val4");
    assertThat(dictionary.get("key10")).isEqualTo("val10");
    assertThat(dictionary.containsKey("key1")).isTrue();
    assertThat(dictionary.containsKey("key4")).isTrue();
    assertThat(dictionary.containsKey("key10")).isTrue();
    assertThat(dictionary.size()).isEqualTo(3);
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldBeEmptyAfterClear(MutableRefToRefDictionary<String, String> dictionary) {
    // when:
    dictionary.put("key1", "val1");
    dictionary.put("key4", "val4");

    // then:
    assertThat(dictionary.isEmpty()).isFalse();

    // when:
    dictionary.clear();

    // then:
    assertThat(dictionary.isEmpty()).isTrue();
    assertThat(dictionary.get("key1")).isNull();
    assertThat(dictionary.containsKey("key1")).isFalse();
  }

  @ParameterizedTest
  @MethodSource("generateDictionaries")
  void shouldAppendDictionary(MutableRefToRefDictionary<String, String> dictionary) {
    // given:
    RefToRefDictionary<String, String> source = RefToRefDictionary.ofEntries(
        entry("key1", "val1"),
        entry("key2", "val2"),
        entry("key3", "val3"),
        entry("key4", "val4"),
        entry("key5", "val5"));

    // when:
    dictionary.append(source);

    // then:
    assertThat(dictionary.get("key1")).isEqualTo("val1");
    assertThat(dictionary.get("key4")).isEqualTo("val4");
    assertThat(dictionary.get("key5")).isEqualTo("val5");
    assertThat(dictionary.containsKey("key3")).isTrue();
    assertThat(dictionary.containsKey("key55")).isFalse();
    assertThat(dictionary.get("key10")).isNull();
    assertThat(dictionary.size()).isEqualTo(5);
  }

  private static Stream<Arguments> generateDictionaries() {
    return Stream.of(
        Arguments.of(MutableRefToRefDictionary.ofTypes(String.class, String.class)),
        Arguments.of(DictionaryFactory.mutableRefToRefDictionary()),
        Arguments.of(DictionaryFactory.stampedLockBasedRefToRefDictionary()));
  }
}
