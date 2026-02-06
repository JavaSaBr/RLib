package javasabr.rlib.network.exception;

/**
 * Base exception class for user-defined network errors.
 *
 * @since 10.0.0
 */
public class UserDefinedNetworkException extends NetworkException {

  /**
   * Constructs a new user-defined network exception with the specified message.
   *
   * @param message the error message
   * @since 10.0.0
   */
  protected UserDefinedNetworkException(String message) {
    super(message);
  }

  /**
   * Constructs a new user-defined network exception with the specified message and cause.
   *
   * @param message the error message
   * @param cause the cause of the exception
   * @since 10.0.0
   */
  protected UserDefinedNetworkException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new user-defined network exception with the specified cause.
   *
   * @param cause the cause of the exception
   * @since 10.0.0
   */
  protected UserDefinedNetworkException(Throwable cause) {
    super(cause);
  }
}
