package rlib.network.packet.impl;

import java.nio.ByteBuffer;

import rlib.network.packet.ReadeablePacket;
import rlib.util.Util;
import rlib.util.pools.Foldable;

/**
 * Базовая реализация читаемого пакета.
 * 
 * @author Ronn
 */
public abstract class AbstractReadeablePacket<C> extends AbstractPacket<C> implements ReadeablePacket<C>, Foldable {

	/** буффер данных читаемого пакета */
	protected volatile ByteBuffer buffer;

	@Override
	public final int getAvaliableBytes() {
		return buffer.remaining();
	}

	@Override
	public ByteBuffer getBuffer() {
		return buffer;
	}

	@Override
	public final boolean read() {

		try {
			readImpl();
			return true;
		} catch(final Exception e) {
			LOGGER.warning(this, e);
			LOGGER.warning(this, "buffer " + buffer + "\n" + Util.hexdump(buffer.array(), buffer.limit()));
		}

		return false;
	}

	/**
	 * Процесс чтения пакета.
	 */
	protected abstract void readImpl();

	@Override
	public void setBuffer(final ByteBuffer buffer) {
		this.buffer = buffer;
	}
}
