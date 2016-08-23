package rlib.network.packet.impl;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.packet.Packet;

import static rlib.util.ClassUtils.unsafeCast;

/**
 * The base implementation of the {@link Packet}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractPacket<C> implements Packet {

    protected static final Logger LOGGER = LoggerManager.getLogger(Packet.class);

    /**
     * The owner of this packet.
     */
    protected volatile C owner;

    /**
     * The name of this packet.
     */
    protected volatile String name;

    @Override
    public final String getName() {

        if (name == null) {
            name = getClass().getSimpleName();
        }

        return name;
    }

    @Override
    public C getOwner() {
        return owner;
    }

    @Override
    public void setOwner(final Object owner) {
        this.owner = unsafeCast(owner);
    }

    @Override
    public String toString() {
        return "AbstractPacket{" +
                "owner=" + owner +
                ", name='" + name + '\'' +
                '}';
    }
}
