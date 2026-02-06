package javasabr.rlib.network.util;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import lombok.experimental.UtilityClass;

/**
 * Utility class for SSL/TLS operations.
 *
 * @since 10.0.0
 */
@UtilityClass
public class SslUtils {

  /**
   * Checks if the SSL handshake is complete and ready for encryption.
   *
   * @param status the handshake status
   * @return true if ready to encrypt/decrypt
   * @since 10.0.0
   */
  public static boolean isReadyToCrypt(HandshakeStatus status) {
    return status == HandshakeStatus.FINISHED || status == HandshakeStatus.NOT_HANDSHAKING;
  }

  /**
   * Checks if the SSL handshake needs further processing.
   *
   * @param status the handshake status
   * @return true if processing is needed
   * @since 10.0.0
   */
  public static boolean needToProcess(HandshakeStatus status) {
    return status != HandshakeStatus.FINISHED && status != HandshakeStatus.NOT_HANDSHAKING;
  }

  /**
   * Executes all delegated SSL tasks and returns the new handshake status.
   *
   * @param engine the SSL engine
   * @return the handshake status after executing tasks
   * @since 10.0.0
   */
  public static HandshakeStatus executeSslTasks(SSLEngine engine) {
    for (Runnable task = engine.getDelegatedTask(); task != null; task = engine.getDelegatedTask()) {
      task.run();
    }
    return engine.getHandshakeStatus();
  }
}
