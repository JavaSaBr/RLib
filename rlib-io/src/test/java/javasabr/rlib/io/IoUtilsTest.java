package javasabr.rlib.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import javasabr.rlib.common.util.StringUtils;
import javasabr.rlib.io.util.IoUtils;
import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
class IoUtilsTest {

  @Test
  void shouldConvertInputStreamToString() {
    var original = StringUtils.generate(2048);
    var source = new ByteArrayInputStream(original.getBytes(StandardCharsets.UTF_8));

    assertThat(IoUtils.toString(source))
        .as("result string should be the same")
        .isEqualTo(original);
  }

  @Test
  void shouldConvertSupplierOfInputStreamToString() {
    var original = StringUtils.generate(2048);
    assertThat(IoUtils.toString(() -> new ByteArrayInputStream(original.getBytes(StandardCharsets.UTF_8))))
        .as("result string should be the same")
        .isEqualTo(original);
  }

  @Test
  void shouldThrowUncheckedIOExceptionDuringConvertingInputStreamToString() {
    assertThatThrownBy(() -> IoUtils.toString(new InputStream() {

          @Override
          public int read() throws IOException {
            throw new IOException("test");
          }
        }))
        .isInstanceOf(UncheckedIOException.class);
  }

  @Test
  void shouldThrowUncheckedIOExceptionDuringConvertingSupplierOfInputStreamToString() {
    assertThatThrownBy(() -> IoUtils.toString(() -> new InputStream() {

          @Override
          public int read() throws IOException {
            throw new IOException("test");
          }
        }))
        .isInstanceOf(UncheckedIOException.class);
  }

  @Test
  void shouldThrowRuntimeExceptionDuringConvertingSupplierOfInputStreamToString() {
    assertThatThrownBy(() -> IoUtils.toString(() -> {
          throw new RuntimeException("test");
        }))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  void shouldConvertReaderToStrungUsingTLB() {
    var original = StringUtils.generate(2048);
    assertThat(IoUtils.toStringUsingTlb(new StringReader(original)))
        .isEqualTo(original);
  }

  @Test
  void shouldThrownUncheckedIOExceptionDuringConvertingReaderToStrungUsingTLB() {
    assertThatThrownBy(() -> IoUtils.toStringUsingTlb(new Reader() {
          @Override
          public int read(char[] cbuf, int off, int len) throws IOException {
            throw new IOException("test");
          }

          @Override
          public void close() {}
        }))
        .isInstanceOf(UncheckedIOException.class);
  }
}
