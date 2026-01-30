package javasabr.rlib.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

/**
 * Test methods in {@link NumberUtils}
 *
 * @author JavaSaBr
 */
class NumberUtilsTest {

  @Test
  void shouldCheckStringIsLong() {
    assertThat(NumberUtils.isLong("1")).isTrue();
    assertThat(NumberUtils.isLong("123123213123")).isTrue();
    assertThat(NumberUtils.isLong("notlong")).isFalse();
    assertThat(NumberUtils.isLong(null)).isFalse();
    assertThat(NumberUtils.isLong("2.1234")).isFalse();
  }

  @Test
  void shouldSafetyConvertStringToLong() {
    assertThat(NumberUtils.safeToLong("1")).isNotNull();
    assertThat(NumberUtils.safeToLong("123123213123")).isNotNull();
    assertThat(NumberUtils.safeToLong("notlong")).isNull();
    assertThat(NumberUtils.safeToLong(null)).isNull();
    assertThat(NumberUtils.safeToLong("2.1234")).isNull();
  }

  @Test
  void shouldConvertStringToOptionalLong() {
    assertThat(NumberUtils
        .toOptionalLong("1")
        .isPresent()).isTrue();
    assertThat(NumberUtils
        .toOptionalLong("123123213123")
        .isPresent()).isTrue();
    assertThat(NumberUtils
        .toOptionalLong("notlong")
        .isPresent()).isFalse();
    assertThat(NumberUtils
        .toOptionalLong(null)
        .isPresent()).isFalse();
    assertThat(NumberUtils
        .toOptionalLong("2.1234")
        .isPresent()).isFalse();
  }

  @Test
  void shouldSafetyConvertStringToInt() {
    assertThat(NumberUtils.safeToInt("1")).isNotNull();
    assertThat(NumberUtils.safeToInt("123124")).isNotNull();
    assertThat(NumberUtils.safeToInt("notlong")).isNull();
    assertThat(NumberUtils.safeToInt(null)).isNull();
    assertThat(NumberUtils.safeToInt("2.1234")).isNull();
  }

  @Test
  void shouldReadBitsCorrectly() {
    assertThat(NumberUtils.isSetBit((byte) 0b00000001, 0))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 0))
        .isTrue();

    assertThat(NumberUtils.isSetBit((byte) 0b00000010, 1))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 1))
        .isTrue();

    assertThat(NumberUtils.isSetBit((byte) 0b00000100, 2))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 2))
        .isTrue();

    assertThat(NumberUtils.isSetBit((byte) 0b00001000, 3))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 3))
        .isTrue();

    assertThat(NumberUtils.isSetBit((byte) 0b00010000, 4))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 4))
        .isTrue();

    assertThat(NumberUtils.isSetBit((byte) 0b00100000, 5))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 5))
        .isTrue();

    assertThat(NumberUtils.isSetBit((byte) 0b01000000, 6))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 6))
        .isTrue();

    assertThat(NumberUtils.isSetBit((byte) 0b10000000, 7))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 7))
        .isTrue();

    assertThat(NumberUtils.isSetBit((byte) 0b11111111, 0))
        .isTrue();
    assertThat(NumberUtils.isSetBit((byte) 0b11111111, 1))
        .isTrue();
    assertThat(NumberUtils.isSetBit((byte) 0b11111111, 2))
        .isTrue();
    assertThat(NumberUtils.isSetBit((byte) 0b11111111, 3))
        .isTrue();
    assertThat(NumberUtils.isSetBit((byte) 0b11111111, 4))
        .isTrue();
    assertThat(NumberUtils.isSetBit((byte) 0b11111111, 5))
        .isTrue();
    assertThat(NumberUtils.isSetBit((byte) 0b11111111, 6))
        .isTrue();
    assertThat(NumberUtils.isSetBit((byte) 0b11111111, 7))
        .isTrue();

    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 0))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 1))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 2))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 3))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 4))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 5))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 6))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit((byte) 0b00000000, 7))
        .isTrue();
  }

  @Test
  void shouldWriteHighLowBitsCorrectly() {
    for (int low = 0; low < 16; low++) {
      for (int high = 0; high < 16; high++) {
        var result = NumberUtils.setHighByteBits(low, high);
        assertThat(NumberUtils.getHighByteBits(result))
            .isEqualTo((byte) high);
        assertThat(NumberUtils.getLowByteBits(result))
            .isEqualTo((byte) low);
      }
    }

    for (int low = 0; low < 16; low++) {
      var result = NumberUtils.setLowByteBits(256, low);
      assertThat(NumberUtils.getHighByteBits(result))
          .isEqualTo((byte) 16);
      assertThat(NumberUtils.getLowByteBits(result))
          .isEqualTo((byte) low);
    }
  }

  @Test
  void shouldReadHighLowBitsCorrectly() {
    for (int i = 0; i < 16; i++) {
      assertThat(NumberUtils.getHighByteBits(i << 4))
          .isEqualTo((byte) i);
    }

    assertThat(NumberUtils.getHighByteBits(0b1000_0100))
        .isEqualTo((byte) 0b0000_1000);
    assertThat(NumberUtils.getHighByteBits(0b0100_1000))
        .isEqualTo((byte) 0b0000_0100);
    assertThat(NumberUtils.getHighByteBits(0b0010_0010))
        .isEqualTo((byte) 0b0000_0010);
    assertThat(NumberUtils.getHighByteBits(0b0001_0001))
        .isEqualTo((byte) 0b0000_0001);
    assertThat(NumberUtils.getHighByteBits(0b0101_1000))
        .isEqualTo((byte) 0b0000_0101);

    for (int i = 0; i < 16; i++) {
      assertThat(NumberUtils.getLowByteBits(i & 0x0F))
          .isEqualTo((byte) i);
    }

    assertThat(NumberUtils.getLowByteBits(0b1000_1000))
        .isEqualTo((byte) 0b0000_1000);
    assertThat(NumberUtils.getLowByteBits(0b0100_0100))
        .isEqualTo((byte) 0b0000_0100);
    assertThat(NumberUtils.getLowByteBits(0b0010_0010))
        .isEqualTo((byte) 0b0000_0010);
    assertThat(NumberUtils.getLowByteBits(0b0001_0001))
        .isEqualTo((byte) 0b0000_0001);
    assertThat(NumberUtils.getLowByteBits(0b0101_0101))
        .isEqualTo((byte) 0b0000_0101);
  }

  @Test
  void shouldChangeBitsInByteCorrectly() {
    assertThat(NumberUtils.isSetBit(NumberUtils.setBit(0, 0), 0))
        .isTrue();
    assertThat(NumberUtils.isSetBit(NumberUtils.setBit(0, 1), 1))
        .isTrue();
    assertThat(NumberUtils.isSetBit(NumberUtils.setBit(0, 2), 2))
        .isTrue();
    assertThat(NumberUtils.isSetBit(NumberUtils.setBit(0, 3), 3))
        .isTrue();
    assertThat(NumberUtils.isSetBit(NumberUtils.setBit(0, 4), 4))
        .isTrue();
    assertThat(NumberUtils.isSetBit(NumberUtils.setBit(0, 5), 5))
        .isTrue();
    assertThat(NumberUtils.isSetBit(NumberUtils.setBit(0, 6), 6))
        .isTrue();
    assertThat(NumberUtils.isSetBit(NumberUtils.setBit(0, 7), 7))
        .isTrue();

    assertThat(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 0), 0))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 1), 1))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 2), 2))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 3), 3))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 4), 4))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 5), 5))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 6), 6))
        .isTrue();
    assertThat(NumberUtils.isNotSetBit(NumberUtils.unsetBit(255, 7), 7))
        .isTrue();
  }

  @Test
  void shouldValidateIntegerCorrectly() {
    // int
    assertThat(NumberUtils.validate(10, 1, 20))
        .isEqualTo(10);
    assertThat(NumberUtils.validate(-20, -21, 10))
        .isEqualTo(-20);
    assertThat(NumberUtils.validate(-1000, -1050, -1000))
        .isEqualTo(-1000);

    assertThat(NumberUtils.validate(10, 1, 20, IllegalArgumentException::new))
        .isEqualTo(10);
    assertThat(NumberUtils.validate(-20, -21, 10, IllegalArgumentException::new))
        .isEqualTo(-20);
    assertThat(NumberUtils.validate(-1000, -1050, -1000, IllegalArgumentException::new))
        .isEqualTo(-1000);

    assertThatThrownBy(() -> NumberUtils.validate(10, 20, 50, IllegalArgumentException::new))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> NumberUtils.validate(-50, -70, -51, IllegalArgumentException::new))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> NumberUtils.validate(10, 20, 50))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> NumberUtils.validate(-50, -70, -51))
        .isInstanceOf(IllegalArgumentException.class);

    // long
    assertThat(NumberUtils.validate(10L, 1, 20))
        .isEqualTo(10);
    assertThat(NumberUtils.validate(-20L, -21, 10))
        .isEqualTo(-20);
    assertThat(NumberUtils.validate(-1000L, -1050, -1000))
        .isEqualTo(-1000);

    assertThat(NumberUtils.validate(10L, 1, 20, IllegalArgumentException::new))
        .isEqualTo(10);
    assertThat(NumberUtils.validate(-20L, -21, 10, IllegalArgumentException::new))
        .isEqualTo(-20);
    assertThat(NumberUtils.validate(-1000L, -1050, -1000, IllegalArgumentException::new))
        .isEqualTo(-1000);

    assertThatThrownBy(() -> NumberUtils.validate(10L, 20, 50, IllegalArgumentException::new))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> NumberUtils.validate(-50L, -70, -51, IllegalArgumentException::new))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> NumberUtils.validate(10L, 20, 50))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> NumberUtils.validate(-50L, -70, -51))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldValidateBooleanCorrectly() {
    assertThat(NumberUtils.toBoolean(1)).isTrue();
    assertThat(NumberUtils.toBoolean(0)).isFalse();
    assertThat(NumberUtils.toBoolean(1L)).isTrue();
    assertThat(NumberUtils.toBoolean(0L)).isFalse();
    assertThat(NumberUtils.toBoolean(1, IllegalArgumentException::new))
        .isTrue();
    assertThat(NumberUtils.toBoolean(0, IllegalArgumentException::new))
        .isFalse();
    assertThat(NumberUtils.toBoolean(1L, IllegalArgumentException::new))
        .isTrue();
    assertThat(NumberUtils.toBoolean(0L, IllegalArgumentException::new))
        .isFalse();

    assertThatThrownBy(() -> NumberUtils.toBoolean(2))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> NumberUtils.toBoolean(-1))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> NumberUtils.toBoolean(2L))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> NumberUtils.toBoolean(-1L))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldEqualsNumbersCorrectly() {
    assertThat(NumberUtils.equals((byte) 10, (byte) 10)).isTrue();
    assertThat(NumberUtils.equals((short) 10, (short) 10)).isTrue();
    assertThat(NumberUtils.equals(10, 10)).isTrue();
    assertThat(NumberUtils.equals(10L, 10L)).isTrue();
    assertThat(NumberUtils.equals(10F, 10F)).isTrue();
    assertThat(NumberUtils.equals(10D, 10D)).isTrue();

    assertThat(NumberUtils.equals((byte) -10, (byte) 10)).isFalse();
    assertThat(NumberUtils.equals((short) -10, (short) 10)).isFalse();
    assertThat(NumberUtils.equals(-10, 10)).isFalse();
    assertThat(NumberUtils.equals(-10L, 10L)).isFalse();
    assertThat(NumberUtils.equals(-10F, 10F)).isFalse();
    assertThat(NumberUtils.equals(-10D, 10D)).isFalse();
  }
}
