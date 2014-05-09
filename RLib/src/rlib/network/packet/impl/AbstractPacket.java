package rlib.network.packet.impl;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.packet.Packet;

/**
 * Базовая реализация сетевого пакета.
 * 
 * @author Ronn
 */
public abstract class AbstractPacket<C> implements Packet<C> {

	protected static final Logger LOGGER = LoggerManager.getLogger(Packet.class);

	/** владелец пакета */
	protected C owner;

	/** название пакета */
	protected String name;

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
	public void setOwner(final C owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "AbstractPacket [owner=" + owner + ", name=" + name + "]";
	}
}
