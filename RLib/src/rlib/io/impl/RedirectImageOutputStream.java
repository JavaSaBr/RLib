package rlib.io.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.stream.ImageOutputStreamImpl;

/**
 * Реализация перенаправителя стрима.
 *
 * @author Ronn
 */
public class RedirectImageOutputStream extends ImageOutputStreamImpl {

    /**
     * Перенаправляющий выходящий поток.
     */
    private OutputStream out;

    /**
     * Перенаправляющий входящий поток.
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
    public int read(final byte[] b, final int off, final int len) throws IOException {
        return in.read(b, off, len);
    }

    /**
     * @param in перенаправляющий входящий поток.
     */
    public void setIn(final InputStream in) {
        this.in = in;
    }

    /**
     * @param out перенаправляющий выходящий поток.
     */
    public void setOut(final OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        out.write(b, off, len);
    }

    @Override
    public void write(final int b) throws IOException {
        out.write(b);
    }
}
