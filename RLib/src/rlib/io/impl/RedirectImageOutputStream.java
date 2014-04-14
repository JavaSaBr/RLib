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

	/** перенаправляющий выходящий поток */
	private OutputStream out;
	/** перенаправляющий входящий поток */
	private InputStream in;

	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
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
	 * @param in перенаправляющий входящий поток.
	 */
	public void setIn(InputStream in) {
		this.in = in;
	}

	/**
	 * @param out перенаправляющий выходящий поток.
	 */
	public void setOut(OutputStream out) {
		this.out = out;
	}

	@Override
	public void close() throws IOException {
	}
}
