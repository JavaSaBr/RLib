package rlib.network.packets.impl;

import java.nio.ByteBuffer;

import rlib.network.packets.Packet;

/**
 * Базовая модель реализации сетевого пакета.
 * 
 * @author Ronn
 */
public abstract class AbstractPacket<C> implements Packet<C> {

	/** владелец пакета */
	protected C owner;

	/** буфер данных */
	protected ByteBuffer buffer;

	/** название пакета */
	protected String name;

	@Override
	public final ByteBuffer getBuffer() {
		return buffer;
	}

	@Override
	public final String getName() {

		if(name == null) {
			name = getClass().getSimpleName();
		}

		return name;
	}

	@Override
	public C getOwner() {
		return owner;
	}

	@Override
	public final void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void setOwner(C owner) {
		this.owner = owner;
	}
}
