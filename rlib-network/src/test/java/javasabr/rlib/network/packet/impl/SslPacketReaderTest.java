package javasabr.rlib.network.packet.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import java.time.Duration;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Network;
import javasabr.rlib.network.NetworkConfig;
import javasabr.rlib.network.UnsafeConnection;
import javasabr.rlib.network.impl.DefaultBufferAllocator;
import javasabr.rlib.network.packet.ReadableNetworkPacket;
import javasabr.rlib.network.packet.WritableNetworkPacket;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import javax.net.ssl.SSLSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/**
 * The tests of SSL packet reader
 *
 * @author crazyrokr
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SslPacketReaderTest {

  private interface TestConnection extends UnsafeConnection<TestConnection> {}

  @Mock
  private TestConnection connection;

  @Mock
  private Network<TestConnection> network;

  @Mock
  private SSLEngine sslEngine;

  @Mock
  private SSLSession sslSession;

  @Mock
  private Consumer<ReadableNetworkPacket<TestConnection>> packetHandler;

  @Mock
  private Consumer<WritableNetworkPacket<TestConnection>> packetWriter;

  private BufferAllocator bufferAllocator;

  @BeforeEach
  void setUp() {
    bufferAllocator = new DefaultBufferAllocator(NetworkConfig.DEFAULT_CLIENT);
    when(connection.bufferAllocator()).thenReturn(bufferAllocator);
    when(connection.network()).thenReturn((Network) network);
    when(connection.remoteAddress()).thenReturn("test-address");
    when(network.config()).thenReturn(NetworkConfig.DEFAULT_CLIENT);
    when(sslEngine.getSession()).thenReturn(sslSession);
    when(sslSession.getApplicationBufferSize()).thenReturn(1024);
    when(sslSession.getPacketBufferSize()).thenReturn(1024);
  }

  private static class TestSslPacketReader extends
      AbstractSslNetworkPacketReader<ReadableNetworkPacket<TestConnection>, TestConnection> {

    private final AtomicInteger readPacketsCount = new AtomicInteger();

    protected TestSslPacketReader(
        TestConnection connection,
        Consumer<? super ReadableNetworkPacket<TestConnection>> packetHandler,
        SSLEngine sslEngine,
        Consumer<WritableNetworkPacket<TestConnection>> packetWriter) {
      super(connection, () -> {}, packetHandler, packetHandler, sslEngine, packetWriter, 100);
    }

    @Override
    protected boolean canStartReadPacket(ByteBuffer buffer) {
      return buffer.remaining() >= 1;
    }

    @Override
    protected int readFullPacketLength(ByteBuffer buffer) {
      return 1;
    }

    @Override
    protected ReadableNetworkPacket<TestConnection> createPacketFor(
        ByteBuffer buffer,
        int startPacketPosition,
        int packetFullLength,
        int packetDataLength) {
      buffer.get(); // consume 1 byte
      readPacketsCount.incrementAndGet();
      return mock(ReadableNetworkPacket.class);
    }
  }

  @Test
  void testShouldNotLoseDataOnNeedWrapDuringHandshake() throws Exception {
    // given
    var reader = new TestSslPacketReader(connection, packetHandler, sslEngine, packetWriter);

    // Initial state: NEED_UNWRAP
    when(sslEngine.getHandshakeStatus()).thenReturn(HandshakeStatus.NEED_UNWRAP);

    // First unwrap will result in NEED_WRAP and status OK, consuming some data.
    // Simulate a single network buffer containing 5 bytes of handshake data followed by
    // 5 bytes of application data, so the remaining bytes can still be processed afterward.
    ByteBuffer networkData = ByteBuffer.allocate(10);
    networkData.put(new byte[10]);
    networkData.flip();

    // doHandshake calls unwrap in NEED_UNWRAP, consumes first 5 bytes, then returns OK
    when(sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer[].class))).thenAnswer(invocation -> {
      ByteBuffer in = invocation.getArgument(0);
      in.position(in.position() + 5); // consume 5 bytes of handshake
      // Change status to NEED_WRAP for next getHandshakeStatus() call
      when(sslEngine.getHandshakeStatus()).thenReturn(HandshakeStatus.NEED_WRAP);
      return new SSLEngineResult(Status.OK, HandshakeStatus.NEED_WRAP, 5, 0);
    });

    // decryptAndRead calls unwrap, consumes the remaining 5 bytes, then return FINISHED or NOT_HANDSHAKING
    when(sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class))).thenAnswer(invocation -> {
      ByteBuffer in = invocation.getArgument(0);
      ByteBuffer out = invocation.getArgument(1);
      int remaining = in.remaining();
      in.position(in.limit()); // consume all
      out.put(new byte[remaining]); // put decrypted data (mocked)
      when(sslEngine.getHandshakeStatus()).thenReturn(HandshakeStatus.NOT_HANDSHAKING);
      return new SSLEngineResult(Status.OK, HandshakeStatus.NOT_HANDSHAKING, remaining, remaining);
    });

    // when
    reader.readPackets(networkData);

    // then
    // readPackets should have been called for the remaining 5 bytes,
    // since each packet is 1 byte, it should have read 5 packets
    assertThat(reader.readPacketsCount.get()).isEqualTo(5);
    verify(packetWriter).accept(any(SslWrapRequestNetworkPacket.class));
  }

  @Test
  void testShouldNotDeadLoopWhenNeedWrapAndNoProgress() throws Exception {
    // given
    var reader = new TestSslPacketReader(connection, packetHandler, sslEngine, packetWriter);

    // Initial state: NEED_WRAP
    when(sslEngine.getHandshakeStatus()).thenReturn(HandshakeStatus.NEED_WRAP);

    // Network buffer has data
    ByteBuffer networkData = ByteBuffer.allocate(10);
    networkData.put(new byte[10]);
    networkData.flip();

    // Mock unwrap in decryptAndRead to return OK with 0 progress
    // This happens if engine is in NEED_WRAP and can't decrypt application data
    when(sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class))).thenReturn(
        new SSLEngineResult(Status.OK, HandshakeStatus.NEED_WRAP, 0, 0)
    );

    // when
    // We expect this NOT to hang indefinitely.
    // If it dead-loops, the test will fail by timeout.
    assertTimeoutPreemptively(Duration.ofSeconds(5), () ->
        reader.readPackets(networkData)
    );

    // then
    // Should have requested wrap
    verify(packetWriter).accept(any(SslWrapRequestNetworkPacket.class));
  }
}
