package javasabr.rlib.common.util;

import java.nio.ByteBuffer;
import org.junit.jupiter.api.Assertions;
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

    Assertions.assertEquals(0, source.position());
    Assertions.assertEquals(4, source.limit());
    Assertions.assertEquals(1, source.getInt());
    Assertions.assertFalse(data.hasRemaining());

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

    Assertions.assertEquals(source.capacity(), data.position());
    Assertions.assertEquals(0, source.position());
    Assertions.assertEquals(source.capacity(), source.remaining());
    Assertions.assertEquals(1, source.getInt());
    Assertions.assertEquals(2, source.getInt());
    Assertions.assertEquals(3, source.getInt());
  }

  @Test
  void shouldPrepareBuffer() {

    var result = BufferUtils.prepareBuffer(
        512, buffer -> {
          buffer.put((byte) 1);
          buffer.put((byte) 2);
          buffer.put((byte) 3);
        });

    Assertions.assertEquals(3, result.limit());
    Assertions.assertEquals(0, result.position());
    Assertions.assertEquals(1, result.get());
    Assertions.assertEquals(2, result.get());
    Assertions.assertEquals(3, result.get());
  }
}
