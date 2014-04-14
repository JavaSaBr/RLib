package rlib.io.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import rlib.io.ReusableStream;

/**
 * Реализация переиспользуемого выходного стрима на массиве байтов.
 * 
 * @author Ronn
 */
public final class ReuseBytesOutputStream extends OutputStream implements ReusableStream {

	/** данные стрима */
	protected byte[] data;

	/** размер записи в стрим */
	protected int size;

	public ReuseBytesOutputStream() {
		this(32);
	}

	public ReuseBytesOutputStream(int size) {

		if(size < 0) {
			throw new IllegalArgumentException("Negative initial size: " + size);
		}
		data = new byte[size];
	}

	/**
	 * Проверка и при необходиомсти увеличение размера массива данных под нужную
	 * длинну.
	 * 
	 * @param minCapacity интересуемая длинна массива.
	 */
	private void checkLength(int minCapacity) {
		if(minCapacity - data.length > 0) {
			resizeData(minCapacity);
		}
	}

	/**
	 * Процесс увеличение размера массива данных.
	 * 
	 * @param minCapacity требуемаяд линна.
	 */
	private void resizeData(int minCapacity) {

		// overflow-conscious code
		int oldCapacity = data.length;
		int newCapacity = oldCapacity << 1;

		if(newCapacity - minCapacity < 0) {
			newCapacity = minCapacity;
		}

		if(newCapacity < 0) {

			if(minCapacity < 0) { // overflow
				throw new OutOfMemoryError();
			}

			newCapacity = Integer.MAX_VALUE;
		}

		data = Arrays.copyOf(data, newCapacity);
	}

	@Override
	public void write(int b) {
		checkLength(size + 1);
		data[size] = (byte) b;
		size += 1;
	}

	@Override
	public void write(byte[] buffer, int offset, int length) {

		if((offset < 0) || (offset > buffer.length) || (length < 0) || ((offset + length) - buffer.length > 0)) {
			throw new IndexOutOfBoundsException();
		}

		checkLength(size + length);
		System.arraycopy(buffer, offset, data, size, length);
		size += length;
	}

	@Override
	public void reset() {
		size = 0;
	}

	/**
	 * @return данные стрима.
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @return размер записи в стрим.
	 */
	public int size() {
		return size;
	}

	@Override
	public String toString() {
		return new String(data, 0, size);
	}

	public String toString(String charsetName) throws UnsupportedEncodingException {
		return new String(data, 0, size, charsetName);
	}

	@Override
	public void close() throws IOException {
	}
}
