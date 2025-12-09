package javasabr.rlib.collections.array;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArrayBuilderTest {

  @Test
  void shouldBuildArray() {
    // when:
    Array<String> result = Array
        .builder(String.class)
        .add("first")
        .add("second")
        .add(Array.of("third", "fourth"))
        .add("fifth", "sixth")
        .add(List.of("seventh", "eight"))
        .build();

    // then:
    Assertions
        .assertThat(result)
        .isEqualTo(Array.of("first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eight"));
  }
}
