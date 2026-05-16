package javasabr.rlib.io.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.stream.ImageOutputStreamImpl;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@Setter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class RedirectImageOutputStream extends ImageOutputStreamImpl {

  @Nullable OutputStream out;
  @Nullable InputStream in;

  @Override
  public void close() {}

  @Override
  public int read() throws IOException {
    return in.read();
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return in.read(b, off, len);
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    out.write(b, off, len);
  }

  @Override
  public void write(int b) throws IOException {
    out.write(b);
  }
}
