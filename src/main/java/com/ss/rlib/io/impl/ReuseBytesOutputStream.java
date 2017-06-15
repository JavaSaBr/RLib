package com.ss.rlib.io.impl;

import com.ss.rlib.io.ReusableStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * The implementation of reusable output stream.
 *
 * @author JavaSaBr
 */
public final class ReuseBytesOutputStream extends OutputStream implements ReusableStream {

    /**
     * The data buffer.
     */
    @NotNull
    protected byte[] data;

    /**
     * The size of stream.
     */
    protected int size;

    /**
     * Instantiates a new Reuse bytes output stream.
     */
    public ReuseBytesOutputStream() {
        this(32);
    }

    /**
     * Instantiates a new Reuse bytes output stream.
     *
     * @param size the size
     */
    public ReuseBytesOutputStream(final int size) {
        if (size < 0) throw new IllegalArgumentException("Negative initial size: " + size);
        data = new byte[size];
    }

    @Override
    public void initFor(@NotNull final byte[] buffer, final int offset, final int length) {

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
    private void checkLength(final int minCapacity) {
        if (minCapacity - data.length > 0) {
            resizeData(minCapacity);
        }
    }

    @Override
    public void close() throws IOException {
    }

    /**
     * Get data byte [ ].
     *
     * @return the data buffer.
     */
    @NotNull
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
    private void resizeData(final int minCapacity) {

        final int oldCapacity = data.length;

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
     * Size int.
     *
     * @return the size of this stream.
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
    public String toString(final String charsetName) throws UnsupportedEncodingException {
        return new String(data, 0, size, charsetName);
    }

    @Override
    public void write(@NotNull final byte[] buffer, final int offset, final int length) {

        if (offset < 0 || offset > buffer.length || length < 0 || offset + length - buffer.length > 0) {
            throw new IndexOutOfBoundsException();
        }

        checkLength(size + length);
        System.arraycopy(buffer, offset, data, size, length);
        size += length;
    }

    @Override
    public void write(final int b) {
        checkLength(size + 1);
        data[size] = (byte) b;
        size += 1;
    }
}
