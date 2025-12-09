package javasabr.rlib.collections.dictionary;

import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class RefToRefDictionaryBuilderTest {

  @Test
  void shouldBuildDictionaryCorrectly() {
    // when:
    RefToRefDictionary<Integer, String> dictionary = RefToRefDictionary
        .<Integer, String>builder()
        .put(15, "15")
        .put(22, "22")
        .put(RefToRefDictionary.of(30, "30", 45, "45"))
        .build();

    // then:
    Assertions
        .assertThat(dictionary)
        .isEqualTo(RefToRefDictionary.of(15, "15", 22, "22", 30, "30", 45, "45"));
  }

  @Test
  void shouldBuildWithTypesDictionaryCorrectly() {
    // when:
    RefToRefDictionary<Integer, String> dictionary = RefToRefDictionary
        .builder(Integer.class, String.class)
        .put(15, "15")
        .put(22, "22")
        .put(RefToRefDictionary.of(30, "30", 45, "45"))
        .build();

    // then:
    Assertions
        .assertThat(dictionary)
        .isEqualTo(RefToRefDictionary.of(15, "15", 22, "22", 30, "30", 45, "45"));
  }

  @Test
  void shouldBuildDictionaryUsingStartWithCorrectly() {
    // when:
    RefToRefDictionary<Integer, String> dictionary = RefToRefDictionary
        .startWith(15, "15")
        .put(22, "22")
        .put(RefToRefDictionary.of(30, "30", 45, "45"))
        .build();

    // then:
    Assertions
        .assertThat(dictionary)
        .isEqualTo(RefToRefDictionary.of(15, "15", 22, "22", 30, "30", 45, "45"));
  }

  @Test
  void shouldBuildDictionaryBasedOnMap() {
    // when:
    RefToRefDictionary<Integer, String> dictionary = RefToRefDictionary
        .builder(Integer.class, String.class)
        .put(Map.of(15, "15", 22, "22"))
        .put(Map.of(30, "30", 45, "45"))
        .build();

    // then:
    Assertions
        .assertThat(dictionary)
        .isEqualTo(RefToRefDictionary.of(15, "15", 22, "22", 30, "30", 45, "45"));
  }
}
