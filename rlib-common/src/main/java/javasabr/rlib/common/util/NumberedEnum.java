package javasabr.rlib.common.util;

/**
 * Marks an enum whose constants have a numeric identifier.
 *
 * @param <T> the concrete enum type implementing this interface
 * @since 10.0.0
 */
public interface NumberedEnum<T extends Enum<T>> {

  /**
   * Returns the numeric identifier for this enum constant.
   *
   * @return the number associated with this constant
   */
  int number();
}
