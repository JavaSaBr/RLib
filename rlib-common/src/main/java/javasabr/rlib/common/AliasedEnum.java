package javasabr.rlib.common;

import java.util.Collection;

/**
 * Marks an {@link Enum} whose constants expose one or more string aliases.
 * <p>
 * Implementing enums can provide alternative string representations for each constant,
 * which can be used, for example, for parsing user input, configuration values,
 * or external identifiers that do not necessarily match the constant {@code name()}.
 *
 * @param <T> the concrete enum type implementing this interface
 */
public interface AliasedEnum<T extends Enum<T>> {

  /**
   * Returns the string aliases associated with this enum constant.
   * <p>
   * An alias is an alternative textual identifier for the constant, such as a
   * short name, legacy name, or external code, that can be used for lookup
   * or serialization instead of the enum's {@link Enum#name()}.
   * <p>
   * Implementations should never return {@code null}; an empty collection
   * indicates that the constant has no aliases. Callers should not modify
   * the returned collection unless the implementation explicitly documents
   * that it is mutable.
   *
   * @return a non-{@code null} collection of aliases for this constant
   */
  Collection<String> aliases();
}
