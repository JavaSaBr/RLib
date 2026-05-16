package javasabr.rlib.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

public class ArrayUtilsTest {

  // ==================== length tests ====================

  @Test
  void shouldReturnCorrectLength() {
    // when/then:
    assertThat(ArrayUtils.length((byte[]) null)).isEqualTo(0);
    assertThat(ArrayUtils.length((short[]) null)).isEqualTo(0);
    assertThat(ArrayUtils.length((int[]) null)).isEqualTo(0);
    assertThat(ArrayUtils.length((long[]) null)).isEqualTo(0);
    assertThat(ArrayUtils.length((float[]) null)).isEqualTo(0);
    assertThat(ArrayUtils.length((double[]) null)).isEqualTo(0);
    assertThat(ArrayUtils.length((Object[]) null)).isEqualTo(0);
    // when/then:
    assertThat(ArrayUtils.length(new byte[]{1, 2})).isEqualTo(2);
    assertThat(ArrayUtils.length(new short[]{1, 2})).isEqualTo(2);
    assertThat(ArrayUtils.length(new int[]{1, 2})).isEqualTo(2);
    assertThat(ArrayUtils.length(new long[]{1, 2})).isEqualTo(2);
    assertThat(ArrayUtils.length(new float[]{1, 2})).isEqualTo(2);
    assertThat(ArrayUtils.length(new double[]{1, 2})).isEqualTo(2);
    assertThat(ArrayUtils.length(new Object[]{1, 2})).isEqualTo(2);
  }

  // ==================== isEmpty tests ====================

  @Test
  void shouldCheckIfArrayIsEmpty() {
    // when/then: null arrays
    assertThat(ArrayUtils.isEmpty((byte[]) null)).isTrue();
    assertThat(ArrayUtils.isEmpty((short[]) null)).isTrue();
    assertThat(ArrayUtils.isEmpty((char[]) null)).isTrue();
    assertThat(ArrayUtils.isEmpty((int[]) null)).isTrue();
    assertThat(ArrayUtils.isEmpty((long[]) null)).isTrue();
    assertThat(ArrayUtils.isEmpty((float[]) null)).isTrue();
    assertThat(ArrayUtils.isEmpty((double[]) null)).isTrue();
    assertThat(ArrayUtils.isEmpty((Object[]) null)).isTrue();
    // when/then: non-empty arrays
    assertThat(ArrayUtils.isEmpty(new byte[]{1, 2})).isFalse();
    assertThat(ArrayUtils.isEmpty(new short[]{1, 2})).isFalse();
    assertThat(ArrayUtils.isEmpty(new char[]{'a', 'b'})).isFalse();
    assertThat(ArrayUtils.isEmpty(new int[]{1, 2})).isFalse();
    assertThat(ArrayUtils.isEmpty(new long[]{1, 2})).isFalse();
    assertThat(ArrayUtils.isEmpty(new float[]{1, 2})).isFalse();
    assertThat(ArrayUtils.isEmpty(new double[]{1, 2})).isFalse();
    assertThat(ArrayUtils.isEmpty(new Object[]{1, 2})).isFalse();
    // when/then: empty arrays
    assertThat(ArrayUtils.isEmpty(new byte[]{})).isTrue();
    assertThat(ArrayUtils.isEmpty(new short[]{})).isTrue();
    assertThat(ArrayUtils.isEmpty(new char[]{})).isTrue();
    assertThat(ArrayUtils.isEmpty(new int[]{})).isTrue();
    assertThat(ArrayUtils.isEmpty(new long[]{})).isTrue();
    assertThat(ArrayUtils.isEmpty(new float[]{})).isTrue();
    assertThat(ArrayUtils.isEmpty(new double[]{})).isTrue();
    assertThat(ArrayUtils.isEmpty(new Object[]{})).isTrue();
  }

  // ==================== isNotEmpty tests ====================

  @Test
  void shouldCheckIfArrayIsNotEmpty() {
    // when/then: null arrays
    assertThat(ArrayUtils.isNotEmpty((byte[]) null)).isFalse();
    assertThat(ArrayUtils.isNotEmpty((short[]) null)).isFalse();
    assertThat(ArrayUtils.isNotEmpty((char[]) null)).isFalse();
    assertThat(ArrayUtils.isNotEmpty((int[]) null)).isFalse();
    assertThat(ArrayUtils.isNotEmpty((long[]) null)).isFalse();
    assertThat(ArrayUtils.isNotEmpty((float[]) null)).isFalse();
    assertThat(ArrayUtils.isNotEmpty((double[]) null)).isFalse();
    assertThat(ArrayUtils.isNotEmpty((Object[]) null)).isFalse();
    // when/then: non-empty arrays
    assertThat(ArrayUtils.isNotEmpty(new byte[]{1, 2})).isTrue();
    assertThat(ArrayUtils.isNotEmpty(new short[]{1, 2})).isTrue();
    assertThat(ArrayUtils.isNotEmpty(new char[]{'a', 'b'})).isTrue();
    assertThat(ArrayUtils.isNotEmpty(new int[]{1, 2})).isTrue();
    assertThat(ArrayUtils.isNotEmpty(new long[]{1, 2})).isTrue();
    assertThat(ArrayUtils.isNotEmpty(new float[]{1, 2})).isTrue();
    assertThat(ArrayUtils.isNotEmpty(new double[]{1, 2})).isTrue();
    assertThat(ArrayUtils.isNotEmpty(new Object[]{1, 2})).isTrue();
    // when/then: empty arrays
    assertThat(ArrayUtils.isNotEmpty(new byte[]{})).isFalse();
    assertThat(ArrayUtils.isNotEmpty(new short[]{})).isFalse();
    assertThat(ArrayUtils.isNotEmpty(new char[]{})).isFalse();
    assertThat(ArrayUtils.isNotEmpty(new int[]{})).isFalse();
    assertThat(ArrayUtils.isNotEmpty(new long[]{})).isFalse();
    assertThat(ArrayUtils.isNotEmpty(new float[]{})).isFalse();
    assertThat(ArrayUtils.isNotEmpty(new double[]{})).isFalse();
    assertThat(ArrayUtils.isNotEmpty(new Object[]{})).isFalse();
  }

  // ==================== array creation tests ====================

  @Test
  void shouldCreateArrayFromVarargs() {
    // when:
    String[] result = ArrayUtils.array("a", "b", "c");
    // then:
    assertThat(result).containsExactly("a", "b", "c");
  }

  @Test
  void shouldCreateArrayByExample() {
    // given:
    String[] example = new String[]{"x"};
    // when:
    String[] result = ArrayUtils.create(example, 3);
    // then:
    assertThat(result).hasSize(3);
    assertThat(result).containsOnlyNulls();
  }

  @Test
  void shouldCreateArrayByType() {
    // when:
    String[] result = ArrayUtils.create(String.class, 5);
    // then:
    assertThat(result).hasSize(5);
    assertThat(result).containsOnlyNulls();
  }

  @Test
  void shouldResolveComponentType() {
    // given:
    String[] array = new String[]{"a", "b"};
    // when:
    Class<String> componentType = ArrayUtils.resolveComponentType(array);
    // then:
    assertThat(componentType).isEqualTo(String.class);
  }

  // ==================== toIntArray tests ====================

  @Test
  void shouldConvertIntegerArrayToIntArray() {
    // given:
    Integer[] integers = {1, 2, 3, 4, 5};
    // when:
    int[] result = ArrayUtils.toIntArray(integers);
    // then:
    assertThat(result).containsExactly(1, 2, 3, 4, 5);
  }

  @Test
  void shouldReturnEmptyIntArrayForEmptyIntegerArray() {
    // when:
    int[] result = ArrayUtils.toIntArray(new Integer[]{});
    // then:
    assertThat(result).isEmpty();
  }

  @Test
  void shouldParseStringToIntArray() {
    // when:
    int[] result = ArrayUtils.toIntArray("1, 2, 3, 4", ",");
    // then:
    assertThat(result).containsExactly(1, 2, 3, 4);
  }

  @Test
  void shouldReturnEmptyIntArrayForBlankString() {
    // when:
    int[] result = ArrayUtils.toIntArray("   ", ",");
    // then:
    assertThat(result).isEmpty();
  }

  @Test
  void shouldThrowExceptionForInvalidIntegerString() {
    // when/then:
    assertThatThrownBy(() -> ArrayUtils.toIntArray("1, abc, 3", ","))
        .isInstanceOf(NumberFormatException.class);
  }

  // ==================== addToArray tests ====================

  @Test
  void shouldAddElementToNullArray() {
    // when:
    String[] result = ArrayUtils.addToArray(null, "element", String.class);
    // then:
    assertThat(result).containsExactly("element");
  }

  @Test
  void shouldAddElementToExistingArray() {
    // given:
    String[] array = {"a", "b"};
    // when:
    String[] result = ArrayUtils.addToArray(array, "c", String.class);
    // then:
    assertThat(result).containsExactly("a", "b", "c");
  }

  // ==================== clear tests ====================

  @Test
  void shouldClearArray() {
    // given:
    Object[] array = {"a", "b", "c"};
    // when:
    ArrayUtils.clear(array);
    // then:
    assertThat(array).containsOnlyNulls();
  }

  // ==================== fill tests ====================

  @Test
  void shouldFillArrayWithSupplier() {
    // given:
    String[] array = new String[3];
    AtomicInteger counter = new AtomicInteger(0);
    // when:
    ArrayUtils.fill(array, () -> "item" + counter.incrementAndGet());
    // then:
    assertThat(array).containsExactly("item1", "item2", "item3");
  }

  @Test
  void shouldFillCharArrayWithSupplier() {
    // given:
    char[] array = new char[3];
    AtomicInteger counter = new AtomicInteger('a' - 1);
    // when:
    ArrayUtils.fill(array, () -> (char) counter.incrementAndGet());
    // then:
    assertThat(array).containsExactly('a', 'b', 'c');
  }

  @Test
  void shouldFillArrayWithIntFunction() {
    // given:
    Integer[] array = new Integer[4];
    // when:
    ArrayUtils.fill(array, i -> i * 2);
    // then:
    assertThat(array).containsExactly(0, 2, 4, 6);
  }

  @Test
  void shouldFillArrayWithFunctionAndArgument() {
    // given:
    String[] array = new String[3];
    // when:
    ArrayUtils.fill(array, "prefix_", arg -> arg + "value");
    // then:
    assertThat(array).containsExactly("prefix_value", "prefix_value", "prefix_value");
  }

  // ==================== combine tests ====================

  @Test
  void shouldCombineIntArrays() {
    // when:
    int[] result = ArrayUtils.combine(new int[]{1, 2}, new int[]{3, 4});
    // then:
    assertThat(result).containsExactly(1, 2, 3, 4);
  }

  @Test
  void shouldHandleNullBaseIntArray() {
    // when:
    int[] result = ArrayUtils.combine(null, new int[]{1, 2});
    // then:
    assertThat(result).containsExactly(1, 2);
  }

  @Test
  void shouldHandleNullAddedIntArray() {
    // when:
    int[] result = ArrayUtils.combine(new int[]{1, 2}, null);
    // then:
    assertThat(result).containsExactly(1, 2);
  }

  @Test
  void shouldCombineObjectArrays() {
    // when:
    String[] result = ArrayUtils.combine(new String[]{"a", "b"}, new String[]{"c", "d"});
    // then:
    assertThat(result).containsExactly("a", "b", "c", "d");
  }

  @Test
  void shouldCombineObjectArraysWithType() {
    // when:
    String[] result = ArrayUtils.combine(
        new String[]{"a"},
        new String[]{"b"},
        String.class
    );
    // then:
    assertThat(result).containsExactly("a", "b");
  }

  @Test
  void shouldCombineArraysWithUniqElements() {
    // when:
    String[] result = ArrayUtils.combineUniq(
        new String[]{"a", "b", "c"},
        new String[]{"b", "c", "d"}
    );
    // then:
    assertThat(result).containsExactlyInAnyOrder("a", "b", "c", "d");
  }

  @Test
  void shouldCombineArraysWithUniqElementsAndType() {
    // when:
    String[] result = ArrayUtils.combineUniq(
        new String[]{"a", "a", "b"},
        new String[]{"b", "c"},
        String.class
    );
    // then:
    assertThat(result).containsExactlyInAnyOrder("a", "b", "c");
  }

  // ==================== contains tests ====================

  @Test
  void shouldCheckIfIntArrayContainsValue() {
    // given:
    int[] array = {1, 2, 3, 4, 5};
    // when/then:
    assertThat(ArrayUtils.contains(array, 3)).isTrue();
    assertThat(ArrayUtils.contains(array, 10)).isFalse();
  }

  @Test
  void shouldCheckIfObjectArrayContainsElement() {
    // given:
    String[] array = {"a", "b", "c"};
    // when/then:
    assertThat(ArrayUtils.contains(array, "b")).isTrue();
    assertThat(ArrayUtils.contains(array, "x")).isFalse();
  }

  // ==================== copyOf tests ====================

  @Test
  void shouldCopyAndExtendByteArray() {
    // given:
    byte[] original = {1, 2, 3};
    // when:
    byte[] result = ArrayUtils.copyOf(original, 2);
    // then:
    assertThat(result).hasSize(5);
    assertThat(result).containsExactly((byte) 1, (byte) 2, (byte) 3, (byte) 0, (byte) 0);
  }

  @Test
  void shouldCopyAndExtendIntArray() {
    // given:
    int[] original = {1, 2, 3};
    // when:
    int[] result = ArrayUtils.copyOf(original, 2);
    // then:
    assertThat(result).hasSize(5);
    assertThat(result).containsExactly(1, 2, 3, 0, 0);
  }

  @Test
  void shouldCopyAndExtendLongArray() {
    // given:
    long[] original = {1L, 2L, 3L};
    // when:
    long[] result = ArrayUtils.copyOf(original, 2);
    // then:
    assertThat(result).hasSize(5);
    assertThat(result).containsExactly(1L, 2L, 3L, 0L, 0L);
  }

  @Test
  void shouldCopyObjectArray() {
    // given:
    String[] original = {"a", "b", "c"};
    // when:
    String[] result = ArrayUtils.copyOf(original);
    // then:
    assertThat(result).containsExactly("a", "b", "c");
    assertThat(result).isNotSameAs(original);
  }

  @Test
  void shouldCopyAndExtendObjectArray() {
    // given:
    String[] original = {"a", "b"};
    // when:
    String[] result = ArrayUtils.copyOfAndExtend(original, 2);
    // then:
    assertThat(result).hasSize(4);
    assertThat(result).containsExactly("a", "b", null, null);
  }

  @Test
  void shouldCopyObjectArrayWithOffset() {
    // given:
    String[] original = {"a", "b", "c"};
    // when:
    String[] result = ArrayUtils.copyOf(original, 1, 2);
    // then:
    assertThat(result).hasSize(5);
    assertThat(result[0]).isNull();
    assertThat(result[1]).isEqualTo("a");
    assertThat(result[2]).isEqualTo("b");
    assertThat(result[3]).isEqualTo("c");
  }

  // ==================== copyTo tests ====================

  @Test
  void shouldCopyToTargetArray() {
    // given:
    int[] source = {1, 2, 3};
    int[] target = new int[5];
    // when:
    ArrayUtils.copyTo(source, target);
    // then:
    assertThat(target).containsExactly(1, 2, 3, 0, 0);
  }

  @Test
  void shouldCopyToTargetArrayWithOffsets() {
    // given:
    int[] source = {1, 2, 3, 4, 5};
    int[] target = new int[5];
    // when:
    ArrayUtils.copyTo(source, target, 1, 2, 2);
    // then:
    assertThat(target).containsExactly(0, 0, 2, 3, 0);
  }

  // ==================== copyOfRange tests ====================

  @Test
  void shouldCopyRangeOfIntArray() {
    // given:
    int[] original = {1, 2, 3, 4, 5};
    // when:
    int[] result = ArrayUtils.copyOfRange(original, 1, 4);
    // then:
    assertThat(result).containsExactly(2, 3, 4);
  }

  @Test
  void shouldCopyRangeOfLongArray() {
    // given:
    long[] original = {1L, 2L, 3L, 4L, 5L};
    // when:
    long[] result = ArrayUtils.copyOfRange(original, 2, 5);
    // then:
    assertThat(result).containsExactly(3L, 4L, 5L);
  }

  @Test
  void shouldCopyRangeOfObjectArray() {
    // given:
    String[] original = {"a", "b", "c", "d", "e"};
    // when:
    String[] result = ArrayUtils.copyOfRange(original, 1, 3);
    // then:
    assertThat(result).containsExactly("b", "c");
  }

  // ==================== indexOf tests ====================

  @Test
  void shouldFindIndexOfObject() {
    // given:
    String[] array = {"a", "b", "c", "d"};
    // when/then:
    assertThat(ArrayUtils.indexOf(array, "c")).isEqualTo(2);
    assertThat(ArrayUtils.indexOf(array, "x")).isEqualTo(-1);
    assertThat(ArrayUtils.indexOf(array, null)).isEqualTo(-1);
  }

  @Test
  void shouldFindIndexOfNullObject() {
    // given:
    String[] array = {"a", null, "c"};
    // when:
    int index = ArrayUtils.indexOf(array, null);
    // then:
    assertThat(index).isEqualTo(1);
  }

  @Test
  void shouldFindIndexWithCondition() {
    // given:
    String[] array = {"apple", "banana", "cherry"};
    // when:
    int index = ArrayUtils.indexOf(array, "an", String::contains);
    // then:
    assertThat(index).isEqualTo(1);
  }

  @Test
  void shouldFindIndexWithConditionInRange() {
    // given:
    String[] array = {"apple", "banana", "avocado", "apricot"};
    // when:
    int index = ArrayUtils.indexOf(array, "a", String::startsWith, 1, 4);
    // then:
    assertThat(index).isEqualTo(2);
  }

  // ==================== sort tests ====================

  @Test
  void shouldSortComparableArray() {
    // given:
    String[] array = {"c", "a", "b"};
    // when:
    ArrayUtils.sort(array);
    // then:
    assertThat(array).containsExactly("a", "b", "c");
  }

  @Test
  void shouldSortIntArray() {
    // given:
    int[] array = {3, 1, 4, 1, 5};
    // when:
    ArrayUtils.sort(array);
    // then:
    assertThat(array).containsExactly(1, 1, 3, 4, 5);
  }

  @Test
  void shouldSortIntArrayInRange() {
    // given:
    int[] array = {5, 3, 1, 4, 2};
    // when:
    ArrayUtils.sort(array, 1, 4);
    // then:
    assertThat(array).containsExactly(5, 1, 3, 4, 2);
  }

  @Test
  void shouldSortLongArrayInRange() {
    // given:
    long[] array = {5L, 3L, 1L, 4L, 2L};
    // when:
    ArrayUtils.sort(array, 1, 4);
    // then:
    assertThat(array).containsExactly(5L, 1L, 3L, 4L, 2L);
  }

  @Test
  void shouldSortArrayWithComparator() {
    // given:
    String[] array = {"aaa", "b", "cc"};
    // when:
    ArrayUtils.sort(array, Comparator.comparingInt(String::length));
    // then:
    assertThat(array).containsExactly("b", "cc", "aaa");
  }

  @Test
  void shouldSortArrayInRangeWithComparator() {
    // given:
    String[] array = {"ddd", "a", "cc", "bb"};
    // when:
    ArrayUtils.sort(array, 1, 4, Comparator.comparingInt(String::length));
    // then:
    assertThat(array).containsExactly("ddd", "a", "cc", "bb");
  }

  // ==================== toString tests ====================

  @Test
  void shouldConvertArrayToStringWithFunction() {
    // given:
    Integer[] array = {1, 2, 3};
    // when:
    String result = ArrayUtils.toString(array, 3, n -> "N" + n);
    // then:
    assertThat(result).isEqualTo("[N1, N2, N3]");
  }

  @Test
  void shouldConvertEmptyArrayToString() {
    // given:
    Integer[] array = {};
    // when:
    String result = ArrayUtils.toString(array, 0, Object::toString);
    // then:
    assertThat(result).isEqualTo("[]");
  }

  @Test
  void shouldConvertIntArrayToString() {
    // given:
    int[] array = {1, 2, 3};
    // when:
    String result = ArrayUtils.toString(array);
    // then:
    assertThat(result).isEqualTo("int[1, 2, 3]");
  }

  @Test
  void shouldConvertIntArrayToStringWithOptions() {
    // given:
    int[] array = {1, 2, 3};
    // when:
    String result = ArrayUtils.toString(array, "-", false, false);
    // then:
    assertThat(result).isEqualTo("1-2-3");
  }

  @Test
  void shouldConvertIntArrayToStringWithRange() {
    // given:
    int[] array = {1, 2, 3, 4, 5};
    // when:
    String result = ArrayUtils.toString(array, 1, 3, ", ", false, true);
    // then:
    assertThat(result).isEqualTo("[2, 3, 4]");
  }

  @Test
  void shouldConvertNullIntArrayToString() {
    // when:
    String result = ArrayUtils.toString((int[]) null);
    // then:
    assertThat(result).isEqualTo("int[]");
  }

  @Test
  void shouldConvertLongArrayToStringWithRange() {
    // given:
    long[] array = {10L, 20L, 30L, 40L};
    // when:
    String result = ArrayUtils.toString(array, 1, 2, "-", false, true);
    // then:
    assertThat(result).isEqualTo("[20-30]");
  }

  @Test
  void shouldConvertFloatArrayToString() {
    // given:
    float[] array = {1.5f, 2.5f};
    // when:
    String result = ArrayUtils.toString(array);
    // then:
    assertThat(result).isEqualTo("float[1.5, 2.5]");
  }

  @Test
  void shouldConvertFloatArrayToStringWithOptions() {
    // given:
    float[] array = {1.0f, 2.0f};
    // when:
    String result = ArrayUtils.toString(array, "|", false, false);
    // then:
    assertThat(result).isEqualTo("1.0|2.0");
  }

  @Test
  void shouldConvertObjectArrayToString() {
    // given:
    String[] array = {"a", "b"};
    // when:
    String result = ArrayUtils.toString(array);
    // then:
    assertThat(result).isEqualTo("String[][a, b]");
  }

  @Test
  void shouldConvertObjectArrayToStringWithOptions() {
    // given:
    String[] array = {"a", "b", "c"};
    // when:
    String result = ArrayUtils.toString(array, "-", false, true);
    // then:
    assertThat(result).isEqualTo("[a-b-c]");
  }

  @Test
  void shouldConvertObjectArrayToStringWithRange() {
    // given:
    String[] array = {"a", "b", "c", "d"};
    // when:
    String result = ArrayUtils.toString(array, 1, 2, ", ", false, true);
    // then:
    assertThat(result).isEqualTo("[b, c]");
  }

  // ==================== forEach tests ====================

  @Test
  void shouldApplyForEachToArray() {
    // given:
    String[] array = {"a", "b", "c"};
    var collected = new ArrayList<String>();
    // when:
    ArrayUtils.forEach(array, collected::add);
    // then:
    assertThat(collected).containsExactly("a", "b", "c");
  }

  @Test
  void shouldHandleNullArrayInForEach() {
    // given:
    var collected = new ArrayList<String>();
    // when:
    ArrayUtils.forEach((String[]) null, collected::add);
    // then:
    assertThat(collected).isEmpty();
  }

  @Test
  void shouldApplyForEachWithCondition() {
    // given:
    Integer[] array = {1, 2, 3, 4, 5};
    var collected = new ArrayList<Integer>();
    // when:
    Predicate<Integer> condition = n -> n % 2 == 0;
    Consumer<Integer> consumer = collected::add;
    ArrayUtils.forEach(array, condition, consumer);
    // then:
    assertThat(collected).containsExactly(2, 4);
  }

  @Test
  void shouldApplyForEachWithArgument() {
    // given:
    String[] array = {"a", "b"};
    var collected = new ArrayList<String>();
    // when:
    ArrayUtils.forEach(array, "_suffix", (element, arg) -> collected.add(element + arg));
    // then:
    assertThat(collected).containsExactly("a_suffix", "b_suffix");
  }

  @Test
  void shouldApplyForEachToDoubleArray() {
    // given:
    double[] array = {1.0, 2.0, 3.0};
    var collected = new ArrayList<Double>();
    // when:
    ArrayUtils.forEach(array, collected, (element, list) -> list.add(element));
    // then:
    assertThat(collected).containsExactly(1.0, 2.0, 3.0);
  }

  @Test
  void shouldApplyForEachWithGetter() {
    // given:
    String[] array = {"hello", "world"};
    var collected = new ArrayList<Integer>();
    // when:
    ArrayUtils.forEach(array, collected, String::length, (len, list) -> list.add(len));
    // then:
    assertThat(collected).containsExactly(5, 5);
  }

  @Test
  void shouldApplyForEachWithConditionAndArgument() {
    // given:
    Integer[] array = {1, 2, 3, 4, 5};
    var collected = new ArrayList<Integer>();
    // when:
    Predicate<Integer> condition = n -> n > 2;
    BiConsumer<Integer, Integer> consumer = (n, multiplier) -> collected.add(n * multiplier);
    ArrayUtils.forEach(array, 10, condition, consumer);
    // then:
    assertThat(collected).containsExactly(30, 40, 50);
  }

  @Test
  void shouldApplyForEachWithConditionGetterAndConsumer() {
    // given:
    String[] array = {"ab", "abc", "abcd"};
    var collected = new ArrayList<Integer>();
    // when:
    ArrayUtils.forEach(
        array,
        2,
        s -> s.length() > 2,
        String::length,
        (len, multiplier) -> collected.add(len * multiplier)
    );
    // then:
    assertThat(collected).containsExactly(6, 8);
  }

  @Test
  void shouldApplyForEachWithTwoArguments() {
    // given:
    String[] array = {"a", "b"};
    var collected = new ArrayList<String>();
    // when:
    ArrayUtils.forEach(array, "[", "]", (e, prefix, suffix) -> collected.add(prefix + e + suffix));
    // then:
    assertThat(collected).containsExactly("[a]", "[b]");
  }

  @Test
  void shouldApplyForEachWithGetterAndTwoArguments() {
    // given:
    String[] array = {"hello", "world"};
    var collected = new ArrayList<String>();
    // when:
    ArrayUtils.forEach(
        array,
        "L=",
        "!",
        (e, arg1, arg2) -> e.length(),
        (len, prefix, suffix) -> collected.add(prefix + len + suffix)
    );
    // then:
    assertThat(collected).containsExactly("L=5!", "L=5!");
  }

  // ==================== count tests ====================

  @Test
  void shouldCountMatchingElements() {
    // given:
    Integer[] array = {1, 2, 3, 4, 5, 6};
    // when:
    int count = ArrayUtils.count(array, n -> n % 2 == 0);
    // then:
    assertThat(count).isEqualTo(3);
  }

  @Test
  void shouldReturnZeroForNullArray() {
    // when:
    int count = ArrayUtils.count(null, n -> true);
    // then:
    assertThat(count).isEqualTo(0);
  }

  @Test
  void shouldReturnZeroForEmptyArray() {
    // when:
    int count = ArrayUtils.count(new Integer[]{}, n -> true);
    // then:
    assertThat(count).isEqualTo(0);
  }

  // ==================== findAny tests ====================

  @Test
  void shouldFindAnyMatchingElement() {
    // given:
    String[] array = {"apple", "banana", "cherry"};
    // when:
    String result = ArrayUtils.findAny(array, s -> s.startsWith("b"));
    // then:
    assertThat(result).isEqualTo("banana");
  }

  @Test
  void shouldReturnNullWhenNoMatch() {
    // given:
    String[] array = {"apple", "banana"};
    // when:
    String result = ArrayUtils.findAny(array, s -> s.startsWith("z"));
    // then:
    assertThat(result).isNull();
  }

  @Test
  void shouldReturnNullForNullArray() {
    // when:
    String result = ArrayUtils.findAny(null, s -> true);
    // then:
    assertThat(result).isNull();
  }

  @Test
  void shouldFindAnyWithArgument() {
    // given:
    String[] array = {"apple", "banana", "cherry"};
    // when:
    String result = ArrayUtils.findAny(array, "an", String::contains);
    // then:
    assertThat(result).isEqualTo("banana");
  }

  @Test
  void shouldFindAnySubElementWithGetter() {
    // given:
    String[] array = {"ab", "abc", "abcd"};
    // when:
    Integer result = ArrayUtils.findAny(array, 3, String::length, Integer::equals);
    // then:
    assertThat(result).isEqualTo(3);
  }

  @Test
  void shouldFindAnyWithConditionAndGetter() {
    // given:
    String[] array = {"a", "bb", "ccc", "dddd"};
    // when:
    Integer result = ArrayUtils.findAny(
        array,
        3,
        s -> s.length() > 1,
        String::length,
        (len, target) -> len >= target
    );
    // then:
    assertThat(result).isEqualTo(3);
  }

  @Test
  void shouldFindAnyWithTwoArguments() {
    // given:
    Integer[] array = {1, 5, 10, 15};
    // when:
    Integer result = ArrayUtils.findAny(array, 5, 12, (n, min, max) -> n >= min && n <= max);
    // then:
    assertThat(result).isEqualTo(5);
  }

  // ==================== anyMatch tests ====================

  @Test
  void shouldReturnTrueWhenAnyMatches() {
    // given:
    String[] array = {"apple", "banana"};
    // when:
    boolean result = ArrayUtils.anyMatch(array, s -> s.length() > 5);
    // then:
    assertThat(result).isTrue();
  }

  @Test
  void shouldReturnFalseWhenNoneMatches() {
    // given:
    String[] array = {"a", "b", "c"};
    // when:
    boolean result = ArrayUtils.anyMatch(array, s -> s.length() > 5);
    // then:
    assertThat(result).isFalse();
  }

  @Test
  void shouldReturnFalseForNullArrayInAnyMatch() {
    // when:
    boolean result = ArrayUtils.anyMatch((String[]) null, s -> true);
    // then:
    assertThat(result).isFalse();
  }

  @Test
  void shouldCheckAnyMatchWithArgument() {
    // given:
    String[] array = {"hello", "world"};
    // when:
    boolean result = ArrayUtils.anyMatch(array, 5, (s, len) -> s.length() == len);
    // then:
    assertThat(result).isTrue();
  }

  // ==================== map tests ====================

  @Test
  void shouldMapArrayToNewType() {
    // given:
    String[] source = {"1", "2", "3"};
    // when:
    Integer[] result = ArrayUtils.map(source, Integer::parseInt, Integer.class);
    // then:
    assertThat(result).containsExactly(1, 2, 3);
  }

  @Test
  void shouldReturnEmptyArrayForEmptySource() {
    // given:
    String[] source = {};
    // when:
    Integer[] result = ArrayUtils.map(source, Integer::parseInt, Integer.class);
    // then:
    assertThat(result).isEmpty();
  }

  @Test
  void shouldMapArrayWithDefault() {
    // given:
    String[] source = {"a", "b"};
    Integer[] defaultArray = {};
    // when:
    Integer[] result = ArrayUtils.map(source, String::length, defaultArray);
    // then:
    assertThat(result).containsExactly(1, 1);
  }

  @Test
  void shouldReturnDefaultForNullSource() {
    // given:
    Integer[] defaultArray = {0};
    // when:
    Integer[] result = ArrayUtils.map(null, String::length, defaultArray);
    // then:
    assertThat(result).containsExactly(0);
  }
}
