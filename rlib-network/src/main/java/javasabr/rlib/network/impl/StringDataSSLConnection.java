package javasabr.rlib.network.impl;

import java.nio.channels.AsynchronousSocketChannel;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.Network;
import javasabr.rlib.network.packet.impl.StringReadablePacket;
import javasabr.rlib.network.packet.impl.StringWritablePacket;
import javax.net.ssl.SSLContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
public class StringDataSSLConnection extends DefaultDataSSLConnection<StringReadablePacket, StringWritablePacket> {

  public StringDataSSLConnection(
      @NotNull Network<? extends Connection<StringReadablePacket, StringWritablePacket>> network,
      @NotNull AsynchronousSocketChannel channel,
      @NotNull BufferAllocator bufferAllocator,
      @NotNull SSLContext sslContext,
      boolean clientMode) {
    super(network, channel, bufferAllocator, sslContext, 100, 2, clientMode);
  }

  @Override
  protected @NotNull StringReadablePacket createReadablePacket() {
    return new StringReadablePacket();
  }
}
