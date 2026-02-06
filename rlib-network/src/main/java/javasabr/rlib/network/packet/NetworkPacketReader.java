package javasabr.rlib.network.packet;

/**
 * Interface for reading network packets from a connection.
 *
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface NetworkPacketReader {

  /**
   * Activates the process of receiving packets.
   *
   * @since 10.0.0
   */
  void startRead();

  /**
   * Closes all used resources.
   *
   * @since 10.0.0
   */
  void close();
}
