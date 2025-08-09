package javasabr.rlib.network.impl;

import java.nio.channels.AsynchronousSocketChannel;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.Network;
import javasabr.rlib.network.packet.ReadablePacket;
import javasabr.rlib.network.packet.WritablePacket;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;

public abstract class AbstractSSLConnection<R extends ReadablePacket, W extends WritablePacket> extends
    AbstractConnection<R, W> {

  protected final SSLEngine sslEngine;

  public AbstractSSLConnection(
      Network<? extends Connection<R, W>> network,
      AsynchronousSocketChannel channel,
      BufferAllocator bufferAllocator,
      SSLContext sslContext,
      int maxPacketsByRead,
      boolean clientMode) {
    super(network, channel, bufferAllocator, maxPacketsByRead);
    this.sslEngine = sslContext.createSSLEngine();
    this.sslEngine.setUseClientMode(clientMode);
    try {
      this.sslEngine.beginHandshake();
    } catch (SSLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void sendImpl(WritablePacket packet) {
    super.sendImpl(packet);
    getPacketReader().startRead();
  }
}
