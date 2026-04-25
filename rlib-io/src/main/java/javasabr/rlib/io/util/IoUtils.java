package javasabr.rlib.io.util;

import static java.lang.ThreadLocal.withInitial;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IoUtils {

  private static final ThreadLocal<char[]> LOCAL_CHAR_BUFFER = withInitial(() -> new char[1024]);

  public static void close(@Nullable Closeable closeable) {
    if (closeable == null) {
      return;
    }
    try {
      closeable.close();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Convert the input stream to a string using UTF-8 encoding.
   */
  public static String toString(InputStream in) {

    var result = new StringBuilder();

    try (var reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
      toString(result, reader);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return result.toString();
  }

  /**
   * Convert the input stream to a string using UTF-8 encoding.
   */
  public static String toString(Supplier<@NonNull InputStream> factory) {

    var result = new StringBuilder();

    try (var reader = new InputStreamReader(factory.get(), StandardCharsets.UTF_8)) {
      toString(result, reader);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return result.toString();
  }

  private static void toString(StringBuilder result, InputStreamReader reader) throws IOException {
    for (var buffer = CharBuffer.allocate(100); reader.read(buffer) != -1; ) {
      buffer.flip();
      result.append(buffer.array(), 0, buffer.limit());
    }
  }

  /**
   * Copy data from the source stream to the destination stream.
   */
  public static void copy(InputStream in, OutputStream out, byte[] buffer, boolean needClose)
      throws IOException {

    for (int i = in.read(buffer); i != -1; i = in.read(buffer)) {
      out.write(buffer, 0, i);
    }

    if (needClose) {
      close(in);
      close(out);
    }
  }

  /**
   * Reads the full string from the reader using a thread-local buffer.
   *
   * @param reader the reader to read from
   * @return the string content
   * @since 10.0.0
   */
  public static String toStringUsingTlb(Reader reader) {
    return toString(reader, LOCAL_CHAR_BUFFER.get());
  }

  private static String toString(Reader reader, char[] buffer) {

    var builder = new StringBuilder();
    try {

      for (int read = reader.read(buffer); read != -1; read = reader.read(buffer)) {
        builder.append(buffer, 0, read);
      }

    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return builder.toString();
  }
}
