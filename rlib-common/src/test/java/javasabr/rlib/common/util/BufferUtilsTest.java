package javasabr.rlib.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
class BufferUtilsTest {

  @Test
  void shouldLoadDataFromOtherBuffer() {

    var data = ByteBuffer.allocate(32);
    data.putInt(1);
    data.flip();

    var source = BufferUtils.loadFrom(data, ByteBuffer.allocate(12));

    assertThat(source.position()).isEqualTo(0);
    assertThat(source.limit()).isEqualTo(4);
    assertThat(source.getInt()).isEqualTo(1);
    assertThat(data.hasRemaining()).isFalse();

    data.clear();
    data
        .putInt(1)
        .putInt(2)
        .putInt(3)
        .putInt(4);
    data
        .putInt(5)
        .putInt(6)
        .putInt(7)
        .putInt(8);
    data.flip();

    BufferUtils.loadFrom(data, source);

    assertThat(data.position()).isEqualTo(source.capacity());
    assertThat(source.position()).isEqualTo(0);
    assertThat(source.remaining()).isEqualTo(source.capacity());
    assertThat(source.getInt()).isEqualTo(1);
    assertThat(source.getInt()).isEqualTo(2);
    assertThat(source.getInt()).isEqualTo(3);
  }

  @Test
  void shouldPrepareBuffer() {

    var result = BufferUtils.prepareBuffer(
        512, buffer -> {
          buffer.put((byte) 1);
          buffer.put((byte) 2);
          buffer.put((byte) 3);
        });

    assertThat(result.limit()).isEqualTo(3);
    assertThat(result.position()).isEqualTo(0);
    assertThat(result.get()).isEqualTo((byte) 1);
    assertThat(result.get()).isEqualTo((byte) 2);
    assertThat(result.get()).isEqualTo((byte) 3);
  }
}
