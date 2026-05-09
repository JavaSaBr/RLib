package javasabr.rlib.common.util;

import java.util.HashMap;
import java.util.Map;
import javasabr.rlib.common.AliasedEnum;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

/**
 * Maps string aliases to enum constants for enums implementing {@link AliasedEnum}.
 * <p>
 * Provides efficient lookup of enum constants by their aliases. Throws an exception
 * during construction if duplicate aliases are detected.
 *
 * @param <T> the enum type that implements AliasedEnum
 * @since 10.0.0
 */
@CustomLog
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AliasedEnumMap<T extends Enum<T> & AliasedEnum<T>> {

  Map<String, T> aliasToValue;

  /**
   * Creates a new alias map for the specified enum class.
   *
   * @param enumClass the enum class to create the map for
   * @throws IllegalArgumentException if duplicate aliases are detected
   */
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

  /**
   * Resolves an enum constant by its alias.
   *
   * @param alias the alias to look up
   * @return the enum constant, or null if not found
   */
  @Nullable
  public T resolve(String alias) {
    return aliasToValue.get(alias);
  }

  /**
   * Resolves an enum constant by its alias, returning a default if not found.
   *
   * @param alias the alias to look up
   * @param def the default value to return if not found
   * @return the enum constant, or the default value if not found
   */
  public T resolve(String alias, T def) {
    T resolved = resolve(alias);
    return resolved == null ? def : resolved;
  }

  /**
   * Resolves an enum constant by its alias, throwing if not found.
   *
   * @param alias the alias to look up
   * @return the enum constant
   * @throws IllegalArgumentException if no constant matches the alias
   */
  public T require(String alias) {
    T constant = resolve(alias);
    if (constant == null) {
      throw new IllegalArgumentException("Unknown enum constant for alias:[%s]".formatted(alias));
    }
    return constant;
  }
}
