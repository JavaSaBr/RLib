package javasabr.rlib.network.impl;

import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Network;
import javasabr.rlib.network.packet.impl.StringReadableNetworkPacket;

import javax.net.ssl.SSLContext;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author crazyrokr
 */
public class StringDataMtlsServerConnection extends DefaultDataSslConnection<StringDataMtlsServerConnection> {

  public StringDataMtlsServerConnection(
      Network<StringDataMtlsServerConnection> network,
      AsynchronousSocketChannel channel,
      BufferAllocator bufferAllocator,
      SSLContext sslContext) {
    super(network, channel, bufferAllocator, sslContext, 100, 2, false);
    sslEngine.setNeedClientAuth(true);
  }

  @Override
  protected StringReadableNetworkPacket<StringDataMtlsServerConnection> createReadablePacket() {
    return new StringReadableNetworkPacket<>();
  }
}
