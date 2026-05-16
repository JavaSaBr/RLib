package javasabr.rlib.io.impl;

import java.io.InputStream;
import javasabr.rlib.common.util.ArrayUtils;
import javasabr.rlib.io.ReusableStream;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * @author JavaSaBr
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
public final class ReuseBytesInputStream extends InputStream implements ReusableStream {

  byte[] buffer;
  int position;
  int count;

  public ReuseBytesInputStream() {
    this.buffer = ArrayUtils.EMPTY_BYTE_ARRAY;
  }

  public ReuseBytesInputStream(byte[] buffer) {
    this.buffer = buffer;
    this.position = 0;
    this.count = buffer.length;
  }

  public ReuseBytesInputStream(byte[] buffer, int offset, int length) {
    this.buffer = buffer;
    this.position = offset;
    this.count = Math.min(offset + length, buffer.length);
  }

  @Override
  public void initFor(byte[] buffer, int offset, int length) {
    this.buffer = buffer;
    this.position = offset;
    this.count = length;
  }

  @Override
  public synchronized int read() {
    return (position < count) ? (buffer[position++] & 0xff) : -1;
  }

  @Override
  public synchronized int read(byte[] buffer, int offset, int length) {

    if (offset < 0 || length < 0 || length > buffer.length - offset) {
      throw new IndexOutOfBoundsException();
    }

    if (position >= count) {
      return -1;
    }

    int available = count - position;

    if (length > available) {
      length = available;
    }

    if (length <= 0) {
      return 0;
    }

    System.arraycopy(this.buffer, position, buffer, offset, length);
    position += length;

    return length;
  }

  @Override
  public synchronized int available() {
    return count - position;
  }

  @Override
  public void close() {}

  @Override
  public synchronized void reset() {
    this.position = 0;
  }
}
