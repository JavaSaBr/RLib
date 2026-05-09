package javasabr.rlib.network.exception;

/**
 * Thrown when a network connection has been closed
 *
 * @since 10.0.0
 */
public class ConnectionClosedException extends NetworkException {

  /**
   * Creates a new exception for a closed connection
   *
   * @param remoteAddress the remote address
   */
  public ConnectionClosedException(String remoteAddress) {
    super("Connection closed: %s".formatted(remoteAddress));
  }

  /**
   * Creates a new exception for a closed connection with a cause
   *
   * @param remoteAddress the remote address
   * @param cause the cause
   */
  public ConnectionClosedException(String remoteAddress, Throwable cause) {
    super("Connection closed: %s".formatted(remoteAddress), cause);
  }
}
