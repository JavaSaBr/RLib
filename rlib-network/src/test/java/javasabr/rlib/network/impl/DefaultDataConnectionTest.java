package javasabr.rlib.network.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Network;
import javasabr.rlib.network.NetworkConfig;
import javasabr.rlib.network.ServerNetworkConfig.SimpleServerNetworkConfig;
import javasabr.rlib.network.packet.ReadableNetworkPacket;
import javasabr.rlib.network.packet.impl.DefaultNetworkPacketReader;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultDataConnectionTest {

  private static class TestDataConnection extends DefaultDataConnection<TestDataConnection> {

    public TestDataConnection(
        Network<TestDataConnection> network,
        AsynchronousSocketChannel channel,
        BufferAllocator bufferAllocator,
        int maxPacketsByRead,
        int packetLengthHeaderSize) {
      super(network, channel, bufferAllocator, maxPacketsByRead, packetLengthHeaderSize);
    }

    @Override
    protected ReadableNetworkPacket<TestDataConnection> createReadablePacket() {
      throw new UnsupportedOperationException();
    }
  }

  @Mock
  Network<@NonNull TestDataConnection> network;

  @Mock
  AsynchronousSocketChannel channel;

  @Test
  @DisplayName("should not allow to read too big packet")
  void shouldNotAllowToReadTooBigPacket() {
    // given:
    NetworkConfig networkConfig = SimpleServerNetworkConfig
        .builder()
        .build();

    var bufferAllocator = new DefaultBufferAllocator(networkConfig);
    var connection = new TestDataConnection(network, channel, bufferAllocator, 100, 4);
    var packetReader = (DefaultNetworkPacketReader<?, ?>) connection.packetReader;

    int nextPacketSize = 250_000_000;

    var incomingBuffer = ByteBuffer.allocate(100);
    incomingBuffer.putInt(nextPacketSize);
    incomingBuffer.put("Test data".getBytes(StandardCharsets.UTF_8));

    Mockito
        .when(network.config())
        .thenReturn(networkConfig);

    Mockito
        .doAnswer(invocationOnMock -> {
          CompletionHandler<Integer, ByteBuffer> readChannelHandler = invocationOnMock.getArgument(2);
          readChannelHandler.completed(incomingBuffer.remaining(), incomingBuffer);
          return null;
        })
        .when(channel)
        .read(Mockito.any(), Mockito.any(), Mockito.any());

    // then:
    assertThat(connection.closed()).isFalse();

    // when:
    packetReader.startRead();

    // then:
    assertThat(connection.closed()).isTrue();
  }
}
