package rlib.network.packets;

import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.util.Util;
import rlib.util.pools.Foldable;
import rlib.util.pools.FoldablePool;

/**
 * Базовая модель сетевого читаемого пакета.
 * 
 * @author Ronn
 */
public abstract class AbstractReadeablePacket<C> extends AbstractPacket<C> implements ReadeablePacket<C>, Foldable {

	protected static final Logger LOGGER = Loggers.getLogger(ReadeablePacket.class);

	@Override
	public void finalyze() {
	}

	@Override
	public final int getAvaliableBytes() {
		return buffer.remaining();
	}

	/**
	 * @return пул этого класса пакетов.
	 */
	@SuppressWarnings("rawtypes")
	protected abstract FoldablePool getPool();

	@Override
	public boolean isSynchronized() {
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ReadeablePacket<C> newInstance() {
		return (ReadeablePacket<C>) getPool().take();
	}

	@Override
	public final boolean read() {

		try {
			readImpl();
			return true;
		} catch(Exception e) {
			LOGGER.warning(this, e);
			LOGGER.warning(this, "buffer " + buffer + "\n" + Util.hexdump(buffer.array(), buffer.limit()));
		}

		return false;
	}

	/**
	 * Чтение одного байта из буфера.
	 */
	public final int readByte() {
		return buffer.get() & 0xFF;
	}

	/**
	 * Наполнение указанного массива байтов, байтами из буфера.
	 * 
	 * @param array наполняемый массив байтов.
	 */
	public final void readBytes(byte[] array) {
		buffer.get(array);
	}

	/**
	 * Наполнение указанного массива байтов, байтами из буфера.
	 * 
	 * @param array наполняемый массив байтов.
	 * @param offset отступ в массиве байтов.
	 * @param length кол-во записываемых байтов в массив.
	 */
	public final void readBytes(byte[] array, int offset, int length) {
		buffer.get(array, offset, length);
	}

	/**
	 * Чтение 4х байтов в виде float из буфера.
	 */
	public final float readFloat() {
		return buffer.getFloat();
	}

	/**
	 * Процесс чтения пакета.
	 */
	protected abstract void readImpl();

	/**
	 * Чтение 4х байтов в виде int из буфера.
	 */
	public final int readInt() {
		return buffer.getInt();
	}

	/**
	 * Чтение 8ми байтов в виде long из буфера.
	 */
	public final long readLong() {
		return buffer.getLong();
	}

	/**
	 * Чтение 2х байтов в виде short из буфера.
	 */
	public final int readShort() {
		return buffer.getShort() & 0xFFFF;
	}

	/**
	 * Чтение строки из буфера.
	 */
	public final String readString() {

		StringBuilder builder = new StringBuilder();

		char cha;

		while(buffer.remaining() > 1) {

			cha = buffer.getChar();

			if(cha == 0) {
				break;
			}

			builder.append(cha);
		}

		return builder.toString();
	}

	/**
	 * Чтение строки из буфера указанной длинны.
	 */
	public final String readString(int length) {

		char[] array = new char[length];

		for(int i = 0; i < length && buffer.remaining() > 1; i++) {
			array[i] = buffer.getChar();
		}

		return new String(array);
	}

	@Override
	public void reinit() {
	}

	@Override
	@SuppressWarnings("unchecked")
	public void run() {
		try {
			runImpl();
		} catch(Exception e) {
			LOGGER.warning(this, e);
		} finally {
			getPool().put(this);
		}
	}

	/**
	 * Процесс выполнение пакета.
	 */
	protected abstract void runImpl();
}
