package javasabr.rlib.network.exception;

public class ConnectionClosedException extends NetworkException {

  public ConnectionClosedException(String remoteAddress) {
    super("Connection closed: " + remoteAddress);
  }

  public ConnectionClosedException(String remoteAddress, Throwable cause) {
    super("Connection closed: " + remoteAddress, cause);
  }
}
