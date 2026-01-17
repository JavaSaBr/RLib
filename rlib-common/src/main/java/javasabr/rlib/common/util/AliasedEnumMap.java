package javasabr.rlib.common.util;

import java.util.HashMap;
import java.util.Map;
import javasabr.rlib.common.AliasedEnum;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

@CustomLog
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AliasedEnumMap<T extends Enum<T> & AliasedEnum<T>> {

  Map<String, T> aliasToValue;

  public AliasedEnumMap(Class<T> enumClass) {
    var enumConstants = enumClass.getEnumConstants();
    var aliasToValue = new HashMap<String, T>();

    for (T enumConstant : enumConstants) {
      for (String alias : enumConstant.aliases()) {
        T previous = aliasToValue.put(alias, enumConstant);
        if (previous != null) {
          throw new IllegalArgumentException("Detected duplicated alias:[%s] for [%s] and [%s]".formatted(
              alias,
              previous.name(),
              enumConstant.name()));
        }
      }
    }
    this.aliasToValue = Map.copyOf(aliasToValue);
  }

  @Nullable
  public T resolve(String alias) {
    return aliasToValue.get(alias);
  }

  public T resolve(String alias, T def) {
    T resolved = resolve(alias);
    return resolved == null ? def : resolved;
  }

  public T require(String alias) {
    T constant = resolve(alias);
    if (constant == null) {
      throw new IllegalArgumentException("Unknown enum constant for alias:[%s]".formatted(alias));
    }
    return constant;
  }
}
