package javasabr.rlib.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ArrayUtilsTest {
  
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
    assertThat(ArrayUtils.length(new double[]{1, 2})).isEqualTo(2);
  }

  @Test
  void shouldCheckIfArrayIsEmpty() {
    // when/then:
    assertThat(ArrayUtils.isEmpty((byte[]) null)).isTrue();
    assertThat(ArrayUtils.isEmpty((int[]) null)).isTrue();
    assertThat(ArrayUtils.isEmpty((long[]) null)).isTrue();
    assertThat(ArrayUtils.isEmpty((float[]) null)).isTrue();
    assertThat(ArrayUtils.isEmpty((double[]) null)).isTrue();
    assertThat(ArrayUtils.isEmpty((Object[]) null)).isTrue();
    // when/then:
    assertThat(ArrayUtils.isEmpty(new byte[]{1, 2})).isFalse();
    assertThat(ArrayUtils.isEmpty(new int[]{1, 2})).isFalse();
    assertThat(ArrayUtils.isEmpty(new long[]{1, 2})).isFalse();
    assertThat(ArrayUtils.isEmpty(new float[]{1, 2})).isFalse();
    assertThat(ArrayUtils.isEmpty(new double[]{1, 2})).isFalse();
    assertThat(ArrayUtils.isEmpty(new Object[]{1, 2})).isFalse();
    // when/then:
    assertThat(ArrayUtils.isEmpty(new byte[]{})).isTrue();
    assertThat(ArrayUtils.isEmpty(new int[]{})).isTrue();
    assertThat(ArrayUtils.isEmpty(new long[]{})).isTrue();
    assertThat(ArrayUtils.isEmpty(new float[]{})).isTrue();
    assertThat(ArrayUtils.isEmpty(new double[]{})).isTrue();
    assertThat(ArrayUtils.isEmpty(new Object[]{})).isTrue();
  }
}
