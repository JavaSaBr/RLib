package javasabr.rlib.common.util;

import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

/**
 * Maps numeric identifiers to enum constants for enums implementing {@link NumberedEnum}.
 * <p>
 * Provides efficient lookup of enum constants by their number.
 *
 * @param <T> the enum type that implements NumberedEnum
 * @since 10.0.0
 */
@CustomLog
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NumberedEnumMap<T extends Enum<T> & NumberedEnum<T>> {

  T[] values;

  /**
   * Creates a new number map for the specified enum class.
   *
   * @param enumClass the enum class to create the map for
   */
  public NumberedEnumMap(Class<T> enumClass) {

    T[] enumConstants = enumClass.getEnumConstants();
    int maxIndex = Stream
        .of(enumConstants)
        .mapToInt(NumberedEnum::number)
        .max()
        .orElse(0);

    T[] indexedConstants = ArrayUtils.create(enumClass, maxIndex + 1);
    for (T enumConstant : enumConstants) {
      indexedConstants[enumConstant.number()] = enumConstant;
    }

    values = indexedConstants;
  }

  /**
   * Resolves an enum constant by its number.
   *
   * @param number the number to look up
   * @return the enum constant, or null if not found
   */
  @Nullable
  public T resolve(int number) {
    try {
      return values[number];
    } catch (IndexOutOfBoundsException e) {
      log.warn(e);
      return null;
    }
  }

  /**
   * Resolves an enum constant by its number, returning a default if not found.
   *
   * @param number the number to look up
   * @param def the default value to return if not found
   * @return the enum constant, or the default value if not found
   */
  public T resolve(int number, T def) {
    T resolved = resolve(number);
    return resolved == null ? def : resolved;
  }

  /**
   * Resolves an enum constant by its number, throwing if not found.
   *
   * @param number the number to look up
   * @return the enum constant
   * @throws IllegalArgumentException if no constant matches the number
   */
  public T require(int number) {
    T constant = resolve(number);
    if (constant == null) {
      throw new IllegalArgumentException("Unknown enum constant for number:" + number);
    }
    return constant;
  }
}
