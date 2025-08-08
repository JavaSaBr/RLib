package javasabr.rlib.network.packet.impl;

import java.nio.ByteBuffer;
import javasabr.rlib.network.packet.WritablePacket;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The writable packet wrapper with additional attachment.
 *
 * @param <A> the attachment type.
 * @param <W> the Writable packet.
 */
@Getter
@RequiredArgsConstructor
public class WritablePacketWrapper<A, W extends WritablePacket> implements WritablePacket {

  private final A attachment;
  private final W packet;

  @Override
  public boolean write(ByteBuffer buffer) {
    return packet.write(buffer);
  }

  @Override
  public int getExpectedLength() {
    return packet.getExpectedLength();
  }

  @Override
  public String getName() {
    return "WritablePacketWrapper";
  }
}
