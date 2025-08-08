package javasabr.rlib.network.impl;

import java.nio.channels.AsynchronousSocketChannel;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.Network;
import javasabr.rlib.network.packet.impl.StringReadablePacket;
import javasabr.rlib.network.packet.impl.StringWritablePacket;

/**
 * @author JavaSaBr
 */
public class StringDataConnection extends DefaultDataConnection<StringReadablePacket, StringWritablePacket> {

  public StringDataConnection(
      Network<? extends Connection<StringReadablePacket, StringWritablePacket>> network,
      AsynchronousSocketChannel channel,
      BufferAllocator bufferAllocator) {
    super(network, channel, bufferAllocator, 100, 2);
  }

  @Override
  protected StringReadablePacket createReadablePacket() {
    return new StringReadablePacket();
  }
}
