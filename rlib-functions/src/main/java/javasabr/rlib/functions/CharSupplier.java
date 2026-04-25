package javasabr.rlib.functions;

/**
 * Represents a supplier of char-valued results.
 *
 * @since 10.0.0
 */
@FunctionalInterface
public interface CharSupplier {

  /**
   * Gets a result.
   *
   * @return the char value
   * @since 10.0.0
   */
  char getAsChar();
}
