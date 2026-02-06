package javasabr.rlib.network.exception;

/**
 * Exception thrown when a network protocol is malformed or invalid.
 *
 * @since 10.0.0
 */
public class MalformedProtocolException extends NetworkException {

  /**
   * Constructs a new malformed protocol exception with the specified message.
   *
   * @param message the error message
   * @since 10.0.0
   */
  public MalformedProtocolException(String message) {
    super(message);
  }
}
