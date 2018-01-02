package com.ss.rlib.network.packet.impl;

import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerManager;
import com.ss.rlib.network.ConnectionOwner;
import com.ss.rlib.network.packet.Packet;
import com.ss.rlib.util.ClassUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The base implementation of {@link Packet}.
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

    public AbstractPacket() {
        this.name = getNameImpl();
    }

    /**
     * Get the name.
     *
     * @return the name
     */
    protected @NotNull String getNameImpl() {
        return getClass().getName();
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable ConnectionOwner getOwner() {
        return owner;
    }

    @Override
    public void setOwner(@Nullable final Object owner) {
        this.owner = owner == null ? null : ClassUtils.unsafeCast(owner);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "owner=" + owner + ", name='" + name + '\'' + '}';
    }
}
