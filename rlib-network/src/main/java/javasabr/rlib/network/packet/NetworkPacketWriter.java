package javasabr.rlib.network.packet;

/**
 * Interface for writing network packets to a connection.
 *
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface NetworkPacketWriter {

  /**
   * Tries to send the next packet in the queue.
   *
   * @return true if the writer started writing new data to the channel
   * @since 10.0.0
   */
  boolean tryToSendNextPacket();

  /**
   * Closes all used resources.
   *
   * @since 10.0.0
   */
  void close();
}
