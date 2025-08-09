package javasabr.rlib.common.io.impl;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javasabr.rlib.common.io.ReusableStream;
import org.jspecify.annotations.NullMarked;

/**
 * The implementation of reusable output stream.
 *
 * @author JavaSaBr
 */
@NullMarked
public final class ReuseBytesOutputStream extends OutputStream implements ReusableStream {

  /**
   * The data buffer.
   */
  protected byte[] data;

  /**
   * The size of stream.
   */
  protected int size;

  public ReuseBytesOutputStream() {
    this(32);
  }

  public ReuseBytesOutputStream(int size) {
    if (size < 0) {
      throw new IllegalArgumentException("Negative initial size: " + size);
    }
    data = new byte[size];
  }

  @Override
  public void initFor(byte[] buffer, int offset, int length) {

    if (offset != 0) {
      throw new IllegalArgumentException("doesn't support offset.");
    }

    this.data = buffer;
    this.size = length;
  }

  /**
   * Check needing resizing the buffer.
   *
   * @param minCapacity the min capacity.
   */
  private void checkLength(int minCapacity) {
    if (minCapacity - data.length > 0) {
      resizeData(minCapacity);
    }
  }

  @Override
  public void close() {
  }

  /**
   * Get the wrapped data array.
   *
   * @return the wrapped data array.
   */
  public byte[] getData() {
    return data;
  }

  @Override
  public void reset() {
    size = 0;
  }

  /**
   * Resize the data buffer.
   *
   * @param minCapacity the min capacity.
   */
  private void resizeData(int minCapacity) {

    int oldCapacity = data.length;
    int newCapacity = oldCapacity << 1;

    if (newCapacity - minCapacity < 0) {
      newCapacity = minCapacity;
    }

    if (newCapacity < 0) {

      if (minCapacity < 0) {
        throw new OutOfMemoryError();
      }

      newCapacity = Integer.MAX_VALUE;
    }

    data = Arrays.copyOf(data, newCapacity);
  }

  /**
   * Get the current size.
   *
   * @return the current size.
   */
  public int size() {
    return size;
  }

  @Override
  public String toString() {
    return new String(data, 0, size);
  }

  /**
   * To string string.
   *
   * @param charsetName the charset name
   * @return the string
   * @throws UnsupportedEncodingException the unsupported encoding exception
   */
  public String toString(String charsetName) throws UnsupportedEncodingException {
    return new String(data, 0, size, charsetName);
  }

  @Override
  public void write(byte[] buffer, int offset, int length) {

    if (offset < 0 || offset > buffer.length || length < 0 || offset + length - buffer.length > 0) {
      throw new IndexOutOfBoundsException();
    }

    checkLength(size + length);
    System.arraycopy(buffer, offset, data, size, length);
    size += length;
  }

  @Override
  public void write(int b) {
    checkLength(size + 1);
    data[size] = (byte) b;
    size += 1;
  }
}
