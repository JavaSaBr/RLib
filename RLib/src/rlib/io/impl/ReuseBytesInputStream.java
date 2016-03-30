package rlib.io.impl;

import java.io.IOException;
import java.io.InputStream;

import rlib.io.ReusableStream;

/**
 * Реализация переиспользуемого входящего стрима на массиве байтов.
 *
 * @author Ronn
 */
public final class ReuseBytesInputStream extends InputStream implements ReusableStream {

    /**
     * Буффер данных
     */
    protected byte buffer[];

    /**
     * Позиция для чтения.
     */
    protected int pos;

    /**
     * Длинна буффера.
     */
    protected int count;

    public ReuseBytesInputStream() {
    }

    public ReuseBytesInputStream(byte buffer[]) {
        this.buffer = buffer;
        this.pos = 0;
        this.count = buffer.length;
    }

    public ReuseBytesInputStream(byte buffer[], int offset, int length) {
        this.buffer = buffer;
        this.pos = offset;
        this.count = Math.min(offset + length, buffer.length);
    }

    @Override
    public void initFor(byte[] buffer, int offset, int length) {
        this.buffer = buffer;
        this.pos = offset;
        this.count = length;
    }

    @Override
    public synchronized int read() {
        return (pos < count) ? (buffer[pos++] & 0xff) : -1;
    }

    @Override
    public synchronized int read(byte buffer[], int offset, int length) {

        if (buffer == null) {
            throw new NullPointerException();
        } else if (offset < 0 || length < 0 || length > buffer.length - offset) {
            throw new IndexOutOfBoundsException();
        }

        if (pos >= count) {
            return -1;
        }

        int available = count - pos;

        if (length > available) {
            length = available;
        }

        if (length <= 0) {
            return 0;
        }

        System.arraycopy(this.buffer, pos, buffer, offset, length);
        pos += length;

        return length;
    }

    @Override
    public synchronized int available() {
        return count - pos;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public synchronized void reset() throws IOException {
        this.pos = 0;
    }
}
