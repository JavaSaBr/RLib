package com.ss.rlib.network.packet.impl;

import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;
import com.ss.rlib.network.packet.Packet;
import org.jetbrains.annotations.NotNull;

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
    public String toString() {
        return getClass().getSimpleName() + "{" + "name='" + name + '\'' + '}';
    }
}
