package javasabr.rlib.collections.deque;

import static javasabr.rlib.common.util.ArrayUtils.array;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.common.util.ReflectionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DequeTest {

  @ParameterizedTest
  @MethodSource("generateDeque")
  void shouldAddFirst(Deque<String> deque) {
    // when:
    deque.addFirst("val1");
    deque.addFirst("val2");

    // then:
    assertThat(deque.toArray())
        .isEqualTo(array("val2", "val1"));

    // when:
    deque.addFirst("val3");
    deque.addFirst("val4");

    // then:
    assertThat(deque.toArray())
        .isEqualTo(array("val4", "val3", "val2", "val1"));

    // when:
    deque.addFirst("val5");
    deque.addFirst("val6");
    deque.addFirst("val7");
    deque.addFirst("val8");
    deque.addFirst("val9");
    deque.addFirst("val10");
    deque.addFirst("val11");
    deque.addFirst("val12");

    // then:
    assertThat(deque.toArray())
        .isEqualTo(array(
            "val12",
            "val11", 
            "val10", 
            "val9", 
            "val8", 
            "val7", 
            "val6", 
            "val5", 
            "val4", 
            "val3", 
            "val2", 
            "val1"));
  }

  @ParameterizedTest
  @MethodSource("generateDeque")
  void shouldAddFirstManyElements(Deque<String> deque) {
    // when:
    IntStream.range(1, 200)
        .mapToObj(value -> "val_" + value)
        .forEach(deque::addFirst);

    // then:
    assertThat(deque.size()).isEqualTo(199);
  }

  @ParameterizedTest
  @MethodSource("generateDeque")
  void shouldAddLastManyElements(Deque<String> deque) {
    // when:
    IntStream.range(1, 200)
        .mapToObj(value -> "val_" + value)
        .forEach(deque::addLast);

    // then:
    assertThat(deque.size()).isEqualTo(199);
  }

  @ParameterizedTest
  @MethodSource("generateDeque")
  void shouldAddFirstLastManyElements(Deque<String> deque) {
    // when:
    IntStream
        .range(1, 500)
        .mapToObj(value -> "val_" + value)
        .forEach(value -> {
          if (ThreadLocalRandom.current().nextBoolean()) {
            deque.addFirst(value);
          } else {
            deque.addLast(value);
          }
        });

    // then:
    assertThat(deque.size()).isEqualTo(499);
  }

  @ParameterizedTest
  @MethodSource("generateDeque")
  void shouldAddLastRemoveFirstManyElements(Deque<String> deque) {
    int count = 10;
    // when/then:
    IntStream
        .range(1, count)
        .forEach(value -> deque.addLast("value_%d".formatted(value)));

    // when/then:
    IntStream
        .range(1, count)
        .forEach(value -> deque.removeFirst());

    // when/then:
    IntStream
        .range(1, count)
        .forEach(value -> deque.addLast("value_%d".formatted(value)));

    // when/then:
    IntStream
        .range(1, count)
        .forEach(value -> deque.removeFirst());

    // when/then:
    IntStream
        .range(1, count)
        .forEach(value -> deque.addLast("value_%d".formatted(value)));
  }

  @ParameterizedTest
  @MethodSource("generateDeque")
  void shouldAddLast(Deque<String> deque) {
    // when:
    deque.addLast("val1");
    deque.addLast("val2");

    // then:
    assertThat(deque.toArray())
        .isEqualTo(array("val1", "val2"));

    // when:
    deque.addLast("val3");
    deque.addLast("val4");

    // then:
    assertThat(deque.toArray())
        .isEqualTo(array("val1", "val2", "val3", "val4"));

    // when:
    deque.addLast("val5");
    deque.addLast("val6");
    deque.addLast("val7");
    deque.addLast("val8");
    deque.addLast("val9");
    deque.addLast("val10");
    deque.addLast("val11");
    deque.addLast("val12");

    // then:
    assertThat(deque.toArray())
        .isEqualTo(array(
            "val1",
            "val2", 
            "val3",
            "val4",
            "val5", 
            "val6",
            "val7",
            "val8",
            "val9", 
            "val10", 
            "val11", 
            "val12"));
  }

  @ParameterizedTest
  @MethodSource("generateDeque")
  void shouldAddFirstAndAddLastInMixMode(Deque<String> deque) {
    // when:
    deque.addFirst("val1");
    deque.addFirst("val2");

    // then:
    assertThat(deque.toArray())
        .isEqualTo(array("val2", "val1"));

    // when:
    deque.addLast("val3");
    deque.addLast("val4");

    // then:
    assertThat(deque.toArray())
        .isEqualTo(array("val2", "val1", "val3", "val4"));

    // when:
    deque.addFirst("val5");
    deque.addFirst("val6");
    deque.addFirst("val7");
    deque.addLast("val8");
    deque.addLast("val9");
    deque.addLast("val10");
    deque.addFirst("val11");
    deque.addLast("val12");

    // then:
    assertThat(deque.toArray())
        .isEqualTo(array(
            "val11",
            "val7", 
            "val6",
            "val5",
            "val2", 
            "val1",
            "val3",
            "val4", 
            "val8", 
            "val9", 
            "val10", 
            "val12"));
  }

  @ParameterizedTest
  @MethodSource("generateDeque")
  void shouldRemoveFirst(Deque<String> deque) {
    // given:
    deque.addAll(List.of("val1", "val2", "val3", "val4", "val5"));

    // when:
    var removed1 = deque.removeFirst();
    var removed2 = deque.removeFirst();

    // then:
    assertThat(removed1).isEqualTo("val1");
    assertThat(removed2).isEqualTo("val2");
    assertThat(deque.toArray())
        .isEqualTo(array("val3", "val4", "val5"));

    // when:
    var removed3 = deque.removeFirst();
    var removed4 = deque.removeFirst();

    // then:
    assertThat(removed3).isEqualTo("val3");
    assertThat(removed4).isEqualTo("val4");
    assertThat(deque.toArray()).isEqualTo(array("val5"));

    // when:
    var removed5 = deque.removeFirst();

    // then:
    assertThat(removed5).isEqualTo("val5");
    assertThat(deque.toArray()).isEqualTo(array());

    // when/then:
    assertThatThrownBy(deque::removeFirst).isInstanceOf(NoSuchElementException.class);
  }

  @ParameterizedTest
  @MethodSource("generateDeque")
  void shouldRemoveLast(Deque<String> deque) {

    // given:
    deque.addAll(List.of("val1", "val2", "val3", "val4", "val5"));

    // when:
    var removed1 = deque.removeLast();
    var removed2 = deque.removeLast();

    // then:
    assertThat(removed1).isEqualTo("val5");
    assertThat(removed2).isEqualTo("val4");
    assertThat(deque.toArray())
        .isEqualTo(array("val1", "val2", "val3"));

    // when:
    var removed3 = deque.removeLast();
    var removed4 = deque.removeLast();

    // then:
    assertThat(removed3).isEqualTo("val3");
    assertThat(removed4).isEqualTo("val2");
    assertThat(deque.toArray()).isEqualTo(array("val1"));

    // when:
    var removed5 = deque.removeLast();

    // then:
    assertThat(removed5).isEqualTo("val1");
    assertThat(deque.toArray()).isEqualTo(array());

    // when/then:
    assertThatThrownBy(deque::removeLast).isInstanceOf(NoSuchElementException.class);
  }

  @ParameterizedTest
  @MethodSource("generateDeque")
  void shouldRemoveFirstAndLast(Deque<String> deque) {

    // given:
    deque.addAll(List.of("val1", "val2", "val3", "val4", "val5", "val6", "val7", "val8"));

    // when:
    var removed1 = deque.removeFirst();
    var removed2 = deque.removeLast();

    // then:
    assertThat(removed1).isEqualTo("val1");
    assertThat(removed2).isEqualTo("val8");
    assertThat(deque.toArray())
        .isEqualTo(array("val2", "val3", "val4", "val5", "val6", "val7"));

    // when:
    var removed3 = deque.removeFirst();
    var removed4 = deque.removeLast();

    // then:
    assertThat(removed3).isEqualTo("val2");
    assertThat(removed4).isEqualTo("val7");
    assertThat(deque.toArray())
        .isEqualTo(array("val3", "val4", "val5", "val6"));

    // when:
    var removed5 = deque.removeFirst();
    var removed6 = deque.removeFirst();
    var removed7 = deque.removeLast();
    var removed8 = deque.removeLast();

    // then:
    assertThat(removed5).isEqualTo("val3");
    assertThat(removed6).isEqualTo("val4");
    assertThat(removed7).isEqualTo("val6");
    assertThat(removed8).isEqualTo("val5");
    assertThat(deque.toArray()).isEqualTo(array());

    // when/then:
    assertThatThrownBy(deque::removeFirst)
        .isInstanceOf(NoSuchElementException.class);
    assertThatThrownBy(deque::removeLast)
        .isInstanceOf(NoSuchElementException.class);
  }

  @ParameterizedTest
  @MethodSource("generateDeque")
  void shouldRemoveElement(Deque<String> deque) {

    // given:
    deque.addAll(List.of("val1", "val2", "val3", "val4", "val5", "val6", "val7", "val8", "val9", "val10"));

    // when:
    deque.remove("val9");

    // then:
    assertThat(deque.toArray())
        .isEqualTo(array("val1", "val2", "val3", "val4", "val5", "val6", "val7", "val8", "val10"));

    // when:
    deque.remove("val2");

    // then:
    assertThat(deque.toArray())
        .isEqualTo(array("val1", "val3", "val4", "val5", "val6", "val7", "val8", "val10"));

    // when:
    deque.remove("val5");

    // then:
    assertThat(deque.toArray())
        .isEqualTo(array("val1", "val3", "val4", "val6", "val7", "val8", "val10"));

    // when:
    deque.remove("val6");

    // then:
    assertThat(deque.toArray())
        .isEqualTo(array("val1", "val3", "val4", "val7", "val8", "val10"));

    // when:
    deque.remove("val1");

    // then:
    assertThat(deque.toArray())
        .isEqualTo(array("val3", "val4", "val7", "val8", "val10"));

    // when:
    deque.remove("val10");

    // then:
    assertThat(deque.toArray())
        .isEqualTo(array("val3", "val4", "val7", "val8"));
  }

  @ParameterizedTest
  @MethodSource("generateDeque")
  void shouldCheckContains(Deque<String> deque) {
    // given:
    deque.addAll(List.of("val1", "val2", "val3", "val4", "val5", "val6", "val7", "val8", "val9", "val10"));

    // when/then:
    assertThat(deque.contains("val1")).isTrue();
    assertThat(deque.contains("val10")).isTrue();
    assertThat(deque.contains("val3")).isTrue();
    assertThat(deque.contains("val8")).isTrue();
    assertThat(deque.contains("val55")).isFalse();
  }

  @Test
  void shouldRebalanceIndexesAddLastRemoveFirst() {
    // given:
    Deque<String> deque = DequeFactory.arrayBasedBased(String.class, 15);
    Field head = ReflectionUtils.getUnsafeField(deque, "head");
    Field tail = ReflectionUtils.getUnsafeField(deque, "tail");
    var generator = new AtomicInteger();

    // when:
    for (int i = 1; i < 14; i++) {
      deque.addLast("val%s".formatted(generator.incrementAndGet()));
    }
    for (int i = 1; i < 12; i++) {
      deque.removeFirst();
    }

    int headValue = ReflectionUtils.getFieldValue(deque, head);
    int tailValue = ReflectionUtils.getFieldValue(deque, tail);

    // then:
    assertThat(headValue).isEqualTo(18);
    assertThat(tailValue).isEqualTo(19);
    assertThat(deque.size()).isEqualTo(2);

    // when:
    for (int i = 1; i < 14; i++) {
      deque.addLast("val%s".formatted(generator.incrementAndGet()));
    }
    for (int i = 1; i < 12; i++) {
      deque.removeFirst();
    }

    headValue = ReflectionUtils.getFieldValue(deque, head);
    tailValue = ReflectionUtils.getFieldValue(deque, tail);

    // then:
    assertThat(headValue).isEqualTo(29);
    assertThat(tailValue).isEqualTo(32);
    assertThat(deque.size()).isEqualTo(4);

    // when:
    for (int i = 1; i < 14; i++) {
      deque.addLast("val%s".formatted(generator.incrementAndGet()));
    }
    for (int i = 1; i < 12; i++) {
      deque.removeFirst();
    }

    headValue = ReflectionUtils.getFieldValue(deque, head);
    tailValue = ReflectionUtils.getFieldValue(deque, tail);

    // then:
    assertThat(headValue).isEqualTo(40);
    assertThat(tailValue).isEqualTo(45);
    assertThat(deque.size()).isEqualTo(6);

    // when:
    for (int i = 1; i < 14; i++) {
      deque.addLast("val%s".formatted(generator.incrementAndGet()));
    }
    for (int i = 1; i < 12; i++) {
      deque.removeFirst();
    }

    headValue = ReflectionUtils.getFieldValue(deque, head);
    tailValue = ReflectionUtils.getFieldValue(deque, tail);

    // then:
    assertThat(headValue).isEqualTo(31);
    assertThat(tailValue).isEqualTo(38);
    assertThat(deque.size()).isEqualTo(8);

    // when:
    for (int i = 1; i < 14; i++) {
      deque.addLast("val%s".formatted(generator.incrementAndGet()));
    }
    for (int i = 1; i < 12; i++) {
      deque.removeFirst();
    }

    headValue = ReflectionUtils.getFieldValue(deque, head);
    tailValue = ReflectionUtils.getFieldValue(deque, tail);

    // then:
    assertThat(headValue).isEqualTo(42);
    assertThat(tailValue).isEqualTo(51);
    assertThat(deque.size()).isEqualTo(10);

    // when:
    for (int i = 1; i < 14; i++) {
      deque.addLast("val%s".formatted(generator.incrementAndGet()));
    }
    for (int i = 1; i < 12; i++) {
      deque.removeFirst();
    }

    headValue = ReflectionUtils.getFieldValue(deque, head);
    tailValue = ReflectionUtils.getFieldValue(deque, tail);

    // then:
    assertThat(headValue).isEqualTo(30);
    assertThat(tailValue).isEqualTo(41);
    assertThat(deque.size()).isEqualTo(12);

    // when:
    for (int i = 1; i < 14; i++) {
      deque.addLast("val%s".formatted(generator.incrementAndGet()));
    }
    for (int i = 1; i < 12; i++) {
      deque.removeFirst();
    }

    headValue = ReflectionUtils.getFieldValue(deque, head);
    tailValue = ReflectionUtils.getFieldValue(deque, tail);

    // then:
    assertThat(headValue).isEqualTo(41);
    assertThat(tailValue).isEqualTo(54);
    assertThat(deque.size()).isEqualTo(14);
  }

  @Test
  void shouldRebalanceIndexesAddFirstRemoveLast() {
    // given:
    Deque<String> deque = DequeFactory.arrayBasedBased(String.class, 15);
    Field head = ReflectionUtils.getUnsafeField(deque, "head");
    Field tail = ReflectionUtils.getUnsafeField(deque, "tail");
    var generator = new AtomicInteger();

    // when:
    for (int i = 1; i < 14; i++) {
      deque.addFirst("val%s".formatted(generator.incrementAndGet()));
    }
    for (int i = 1; i < 12; i++) {
      deque.removeLast();
    }

    int headValue = ReflectionUtils.getFieldValue(deque, head);
    int tailValue = ReflectionUtils.getFieldValue(deque, tail);

    // then:
    assertThat(headValue).isEqualTo(5);
    assertThat(tailValue).isEqualTo(6);
    assertThat(deque.size()).isEqualTo(2);

    // when:
    for (int i = 1; i < 14; i++) {
      deque.addFirst("val%s".formatted(generator.incrementAndGet()));
    }
    for (int i = 1; i < 12; i++) {
      deque.removeLast();
    }

    headValue = ReflectionUtils.getFieldValue(deque, head);
    tailValue = ReflectionUtils.getFieldValue(deque, tail);

    // then:
    assertThat(headValue).isEqualTo(2);
    assertThat(tailValue).isEqualTo(5);
    assertThat(deque.size()).isEqualTo(4);

    // when:
    for (int i = 1; i < 14; i++) {
      deque.addFirst("val%s".formatted(generator.incrementAndGet()));
    }
    for (int i = 1; i < 12; i++) {
      deque.removeLast();
    }

    headValue = ReflectionUtils.getFieldValue(deque, head);
    tailValue = ReflectionUtils.getFieldValue(deque, tail);

    // then:
    assertThat(headValue).isEqualTo(9);
    assertThat(tailValue).isEqualTo(14);
    assertThat(deque.size()).isEqualTo(6);

    // when:
    for (int i = 1; i < 14; i++) {
      deque.addFirst("val%s".formatted(generator.incrementAndGet()));
    }
    for (int i = 1; i < 12; i++) {
      deque.removeLast();
    }

    headValue = ReflectionUtils.getFieldValue(deque, head);
    tailValue = ReflectionUtils.getFieldValue(deque, tail);

    // then:
    assertThat(headValue).isEqualTo(25);
    assertThat(tailValue).isEqualTo(32);
    assertThat(deque.size()).isEqualTo(8);

    // when:
    for (int i = 1; i < 14; i++) {
      deque.addFirst("val%s".formatted(generator.incrementAndGet()));
    }
    for (int i = 1; i < 12; i++) {
      deque.removeLast();
    }

    headValue = ReflectionUtils.getFieldValue(deque, head);
    tailValue = ReflectionUtils.getFieldValue(deque, tail);

    // then:
    assertThat(headValue).isEqualTo(12);
    assertThat(tailValue).isEqualTo(21);
    assertThat(deque.size()).isEqualTo(10);

    // when:
    for (int i = 1; i < 14; i++) {
      deque.addFirst("val%s".formatted(generator.incrementAndGet()));
    }
    for (int i = 1; i < 12; i++) {
      deque.removeLast();
    }

    headValue = ReflectionUtils.getFieldValue(deque, head);
    tailValue = ReflectionUtils.getFieldValue(deque, tail);

    // then:
    assertThat(headValue).isEqualTo(31);
    assertThat(tailValue).isEqualTo(42);
    assertThat(deque.size()).isEqualTo(12);

    // when:
    for (int i = 1; i < 14; i++) {
      deque.addFirst("val%s".formatted(generator.incrementAndGet()));
    }
    for (int i = 1; i < 12; i++) {
      deque.removeLast();
    }

    headValue = ReflectionUtils.getFieldValue(deque, head);
    tailValue = ReflectionUtils.getFieldValue(deque, tail);

    // then:
    assertThat(headValue).isEqualTo(18);
    assertThat(tailValue).isEqualTo(31);
    assertThat(deque.size()).isEqualTo(14);
  }

  @ParameterizedTest
  @MethodSource("generateDeque")
  void shouldCorrectlyIterateDeque(Deque<String> deque) {
    // given:
    deque.addAll(List.of("val1", "val2", "val3", "val4", "val5", "val6"));
    var container = MutableArray.ofType(String.class);

    // when:
    for (Iterator<String> iterator = deque.iterator(); iterator.hasNext(); ) {
      String value = iterator.next();
      container.add(value);
      if (value.equals("val2") || value.equals("val4")) {
        iterator.remove();
      }
    }

    // then:
    assertThat(container.toArray())
        .isEqualTo(array("val1", "val2", "val3", "val4", "val5", "val6"));
    assertThat(deque.toArray())
        .isEqualTo(array("val1", "val3", "val5", "val6"));
  }

  @ParameterizedTest
  @MethodSource("generateDeque")
  void shouldCorrectlyIterate2Deque(Deque<String> deque) {
    // given:
    deque.addAll(List.of("val1", "val2", "val3", "val4", "val5", "val6"));
    var container = MutableArray.ofType(String.class);

    // when:
    for (Iterator<String> iterator = deque.descendingIterator(); iterator.hasNext(); ) {
      String value = iterator.next();
      container.add(value);
      if (value.equals("val2") || value.equals("val4")) {
        iterator.remove();
      }
    }

    // then:
   // assertThat(container.toArray()).isEqualTo(array("val6", "val5", "val4", "val3", "val2", "val1"));
    assertThat(deque.toArray())
        .isEqualTo(array("val1", "val3", "val5", "val6"));
  }

  private static Stream<Arguments> generateDeque() {
    return Stream.of(
        Arguments.of(DequeFactory.linkedListBased()),
        Arguments.of(DequeFactory.arrayBasedBased(String.class, 15)));
  }
}
