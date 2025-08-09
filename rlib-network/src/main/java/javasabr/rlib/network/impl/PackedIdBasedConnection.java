package javasabr.rlib.network.impl;

import javasabr.rlib.network.Connection;

/**
 * The interface to implement a packet id based async connection.
 *
 * @author JavaSaBr
 */
public interface PackedIdBasedConnection extends Connection {

  /**
   * Get length of packet's header with packet's data length.
   *
   * @return the length of packet's header with packet's data length.
   */
  int getPacketLengthHeaderSize();

  /**
   * Get length of packet's header with packet's id.
   *
   * @return the length of packet's header with packet's id.
   */
  int getPacketIdHeaderSize();
}
