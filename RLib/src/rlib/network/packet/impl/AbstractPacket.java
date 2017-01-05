package rlib.network.packet.impl;

import static rlib.util.ClassUtils.unsafeCast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.packet.Packet;

/**
 * The base implementation of the {@link Packet}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractPacket<C> implements Packet {

    protected static final Logger LOGGER = LoggerManager.getLogger(Packet.class);

    /**
     * The name of this packet.
     */
    @NotNull
    protected final String name;

    /**
     * The owner of this packet.
     */
    @Nullable
    protected volatile C owner;

    public AbstractPacket() {
        this.name = getNameImpl();
    }

    @NotNull
    protected String getNameImpl() {
        return getClass().getName();
    }

    @NotNull
    @Override
    public final String getName() {
        return name;
    }

    @Nullable
    @Override
    public C getOwner() {
        return owner;
    }

    @Override
    public void setOwner(@Nullable final Object owner) {
        this.owner = owner == null ? null : unsafeCast(owner);
    }

    @Override
    public String toString() {
        return "AbstractPacket{" +
                "owner=" + owner +
                ", name='" + name + '\'' +
                '}';
    }
}
