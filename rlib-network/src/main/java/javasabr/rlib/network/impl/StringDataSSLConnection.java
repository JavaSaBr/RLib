package javasabr.rlib.network.impl;

import java.nio.channels.AsynchronousSocketChannel;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.Network;
import javasabr.rlib.network.packet.impl.StringReadablePacket;
import javasabr.rlib.network.packet.impl.StringWritablePacket;
import javax.net.ssl.SSLContext;

/**
 * @author JavaSaBr
 */
public class StringDataSSLConnection extends DefaultDataSSLConnection<StringReadablePacket, StringWritablePacket> {

  public StringDataSSLConnection(
      Network<? extends Connection<StringReadablePacket, StringWritablePacket>> network,
      AsynchronousSocketChannel channel,
      BufferAllocator bufferAllocator,
      SSLContext sslContext,
      boolean clientMode) {
    super(network, channel, bufferAllocator, sslContext, 100, 2, clientMode);
  }

  @Override
  protected StringReadablePacket createReadablePacket() {
    return new StringReadablePacket();
  }
}
