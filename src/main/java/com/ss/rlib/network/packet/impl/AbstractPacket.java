package com.ss.rlib.network.packet.impl;

import static com.ss.rlib.util.ClassUtils.unsafeCast;

import com.ss.rlib.logging.Logger;
import com.ss.rlib.network.ConnectionOwner;
import com.ss.rlib.network.packet.Packet;
import com.ss.rlib.util.ClassUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ss.rlib.logging.LoggerManager;

/**
 * The base implementation of the {@link Packet}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractPacket implements Packet {

    /**
     * The constant LOGGER.
     */
    @NotNull
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
    protected volatile ConnectionOwner owner;

    /**
     * Instantiates a new Abstract packet.
     */
    public AbstractPacket() {
        this.name = getNameImpl();
    }

    /**
     * Gets name.
     *
     * @return the name
     */
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
    public ConnectionOwner getOwner() {
        return owner;
    }

    @Override
    public void setOwner(@Nullable final Object owner) {
        this.owner = owner == null ? null : ClassUtils.unsafeCast(owner);
    }

    @Override
    public String toString() {
        return "AbstractPacket{" +
                "owner=" + owner +
                ", name='" + name + '\'' +
                '}';
    }
}
