package javasabr.rlib.common.io.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.stream.ImageOutputStreamImpl;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The implementation of a redirector image input stream.
 *
 * @author JavaSaBr
 */
@NullMarked
public class RedirectImageOutputStream extends ImageOutputStreamImpl {

  /**
   * The output stream.
   */
  private @Nullable OutputStream out;

  /**
   * The input stream.
   */
  private @Nullable InputStream in;

  @Override
  public void close() throws IOException {
  }

  @Override
  public int read() throws IOException {
    return in.read();
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return in.read(b, off, len);
  }

  /**
   * Sets in.
   *
   * @param in the input stream.
   */
  public void setIn(InputStream in) {
    this.in = in;
  }

  /**
   * Sets out.
   *
   * @param out the output stream.
   */
  public void setOut(OutputStream out) {
    this.out = out;
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
