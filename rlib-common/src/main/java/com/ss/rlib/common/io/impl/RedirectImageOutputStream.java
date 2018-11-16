package com.ss.rlib.common.io.impl;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.stream.ImageOutputStreamImpl;

/**
 * The implementation of a redirector image input stream.
 *
 * @author JavaSaBr
 */
public class RedirectImageOutputStream extends ImageOutputStreamImpl {

    /**
     * The output stream.
     */
    private OutputStream out;

    /**
     * The input stream.
     */
    private InputStream in;

    @Override
    public void close() throws IOException {
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    @Override
    public int read(@NotNull final byte[] b, final int off, final int len) throws IOException {
        return in.read(b, off, len);
    }

    /**
     * Sets in.
     *
     * @param in the input stream.
     */
    public void setIn(@NotNull final InputStream in) {
        this.in = in;
    }

    /**
     * Sets out.
     *
     * @param out the output stream.
     */
    public void setOut(@NotNull final OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(@NotNull final byte[] b, final int off, final int len) throws IOException {
        out.write(b, off, len);
    }

    @Override
    public void write(final int b) throws IOException {
        out.write(b);
    }
}
