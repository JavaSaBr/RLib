package javasabr.rlib.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
class IOUtilsTest {

  @Test
  void shouldConvertInputStreamToString() {

    var original = StringUtils.generate(2048);
    var source = new ByteArrayInputStream(original.getBytes(StandardCharsets.UTF_8));

    Assertions.assertEquals(original, IOUtils.toString(source), "result string should be the same");
  }

  @Test
  void shouldConvertSupplierOfInputStreamToString() {

    var original = StringUtils.generate(2048);

    Assertions.assertEquals(
        original,
        IOUtils.toString(() -> new ByteArrayInputStream(original.getBytes(StandardCharsets.UTF_8))),
        "result string should be the same");
  }

  @Test
  void shouldThrowUncheckedIOExceptionDuringConvertingInputStreamToString() {
    Assertions.assertThrows(
        UncheckedIOException.class, () -> IOUtils.toString(new InputStream() {

          @Override
          public int read() throws IOException {
            throw new IOException("test");
          }
        }));
  }

  @Test
  void shouldThrowUncheckedIOExceptionDuringConvertingSupplierOfInputStreamToString() {
    Assertions.assertThrows(
        UncheckedIOException.class, () -> IOUtils.toString(() -> new InputStream() {

          @Override
          public int read() throws IOException {
            throw new IOException("test");
          }
        }));
  }

  @Test
  void shouldThrowRuntimeExceptionDuringConvertingSupplierOfInputStreamToString() {
    Assertions.assertThrows(
        RuntimeException.class, () -> {
          IOUtils.toString(() -> {
            throw new RuntimeException("test");
          });
        });
  }

  @Test
  void shouldConvertReaderToStrungUsingTLB() {

    var original = StringUtils.generate(2048);

    Assertions.assertEquals(original, IOUtils.toStringUsingTLB(new StringReader(original)));
  }

  @Test
  void shouldThrownUncheckedIOExceptionDuringConvertingReaderToStrungUsingTLB() {

    Assertions.assertThrows(
        UncheckedIOException.class, () -> IOUtils.toStringUsingTLB(new Reader() {
          @Override
          public int read(@NotNull char[] cbuf, int off, int len) throws IOException {
            throw new IOException("test");
          }

          @Override
          public void close() {
          }
        }));
  }
}
