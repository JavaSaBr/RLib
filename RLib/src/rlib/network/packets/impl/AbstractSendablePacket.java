package rlib.network.packets.impl;

import java.nio.ByteBuffer;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.packets.SendablePacket;
import rlib.util.StringUtils;
import rlib.util.Util;
import rlib.util.pools.Foldable;

/**
 * Базовая модель записываемого пакета на стороне сервера.
 * 
 * @author Ronn
 */
public abstract class AbstractSendablePacket<C> extends AbstractPacket<C> implements SendablePacket<C>, Foldable {

	protected static final Logger LOGGER = LoggerManager.getLogger(SendablePacket.class);

	/** счетчик добавлений на отправку экземпляра пакета */
	protected final AtomicInteger counter;

	public AbstractSendablePacket() {
		this.counter = new AtomicInteger();
	}

	@Override
	public abstract void complete();

	@Override
	public final void decreaseSends() {
		counter.decrementAndGet();
	}

	@Override
	public void decreaseSends(final int count) {
		counter.subAndGet(count);
	}

	@Override
	public final void increaseSends() {
		counter.incrementAndGet();
	}

	@Override
	public void increaseSends(final int count) {
		counter.addAndGet(count);
	}

	@Override
	public boolean isSynchronized() {
		return true;
	}

	@Override
	public void reinit() {
		counter.getAndSet(0);
	}

	@Override
	public void write(final ByteBuffer buffer) {

		if(counter.get() < 0) {
			LOGGER.warning(this, "write pooled packet");
			return;
		}

		try {
			writeImpl(buffer);
		} catch(final Exception e) {
			LOGGER.warning(this, e);
			LOGGER.warning(this, "Buffer " + buffer + "\n" + Util.hexdump(buffer.array(), buffer.position()));
		}
	}

	public final void writeByte(final ByteBuffer buffer, final int value) {
		buffer.put((byte) value);
	}

	protected final void writeByte(final int value) {
		writeByte(buffer, value);
	}

	public final void writeChar(final ByteBuffer buffer, final char value) {
		buffer.putChar(value);
	}

	public final void writeChar(final ByteBuffer buffer, final int value) {
		buffer.putChar((char) value);
	}

	protected final void writeChar(final char value) {
		writeChar(buffer, value);
	}

	protected final void writeChar(final int value) {
		writeChar(buffer, value);
	}

	public final void writeFloat(final ByteBuffer buffer, final float value) {
		buffer.putFloat(value);
	}

	protected final void writeFloat(final float value) {
		writeFloat(buffer, value);
	}

	/**
	 * Процесс записи пакета.
	 */
	protected void writeImpl() {
		LOGGER.warning(this, new Exception("unsupperted method"));
	}

	/**
	 * Процесс записи пакета в указанный буффер.
	 */
	protected void writeImpl(final ByteBuffer buffer) {
		LOGGER.warning(this, new Exception("unsupperted method"));
	}

	public final void writeInt(final ByteBuffer buffer, final int value) {
		buffer.putInt(value);
	}

	protected final void writeInt(final int value) {
		writeInt(buffer, value);
	}

	@Override
	public void writeLocal() {

		if(counter.get() < 0) {
			LOGGER.warning(this, "write local pooled packet");
			return;
		}

		try {
			writeImpl();
		} catch(final Exception e) {
			LOGGER.warning(this, e);
			LOGGER.warning(this, "Buffer " + buffer + "\n" + Util.hexdump(buffer.array(), buffer.position()));
		}
	}

	public final void writeLong(final ByteBuffer buffer, final long value) {
		buffer.putLong(value);
	}

	protected final void writeLong(final long value) {
		writeLong(buffer, value);
	}

	public void writeShort(final ByteBuffer buffer, final int value) {
		buffer.putShort((short) value);
	}

	protected void writeShort(final int value) {
		writeShort(buffer, value);
	}

	public void writeString(final ByteBuffer buffer, String string) {

		if(string == null) {
			string = StringUtils.EMPTY;
		}

		for(int i = 0, length = string.length(); i < length; i++) {
			buffer.putChar(string.charAt(i));
		}
	}

	protected void writeString(final String string) {
		writeString(buffer, string);
	}
}
