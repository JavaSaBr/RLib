package javasabr.rlib.common.util;

import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

@CustomLog
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NumberedEnumMap<T extends Enum<T> & NumberedEnum<T>> {

  T[] values;

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

  @Nullable
  public T resolve(int number) {
    try {
      return values[number];
    } catch (IndexOutOfBoundsException e) {
      log.warning(e.getMessage());
      return null;
    }
  }

  public T resolve(int number, T def) {
    T resolved = resolve(number);
    return resolved == null ? def : resolved;
  }

  public T require(int number) {
    T constant = resolve(number);
    if (constant == null) {
      throw new IllegalArgumentException("Unknown enum constant for number:" + number);
    }
    return constant;
  }
}
