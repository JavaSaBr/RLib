package javasabr.rlib.network.packet.impl;

import java.nio.ByteBuffer;

/**
 * Packet marker.
 */
public class SSLWritablePacket extends AbstractWritablePacket {

  private static final SSLWritablePacket INSTANCE = new SSLWritablePacket();

  public static SSLWritablePacket getInstance() {
    return INSTANCE;
  }

  @Override
  public boolean write(ByteBuffer buffer) {
    return true;
  }
}
