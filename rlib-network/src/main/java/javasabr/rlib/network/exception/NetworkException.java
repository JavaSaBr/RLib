package javasabr.rlib.network.exception;

/**
 * Base exception class for network-related errors.
 *
 * @since 10.0.0
 */
public class NetworkException extends RuntimeException {

  /**
   * Constructs a new network exception with the specified message.
   *
   * @param message the error message
   * @since 10.0.0
   */
  protected NetworkException(String message) {
    super(message);
  }

  /**
   * Constructs a new network exception with the specified message and cause.
   *
   * @param message the error message
   * @param cause the cause of the exception
   * @since 10.0.0
   */
  protected NetworkException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new network exception with the specified cause.
   *
   * @param cause the cause of the exception
   * @since 10.0.0
   */
  protected NetworkException(Throwable cause) {
    super(cause);
  }
}
