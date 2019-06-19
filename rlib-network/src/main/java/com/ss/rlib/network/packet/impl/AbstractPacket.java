package com.ss.rlib.network.packet.impl;

import static com.ss.rlib.network.util.NetworkUtils.hexDump;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.packet.Packet;
import com.ss.rlib.network.util.NetworkUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The base implementation of {@link Packet}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractPacket implements Packet {

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
     * Handle the exception.
     *
     * @param buffer    the data buffer.
     * @param exception the exception.
     */
    protected void handleException(@NotNull ByteBuffer buffer, @NotNull Exception exception) {
        LOGGER.warning(this, exception);

        if (buffer.isDirect()) {
            byte[] array = new byte[buffer.limit()];
            buffer.get(array, 0, buffer.limit());
            LOGGER.warning(this, "buffer " + buffer + "\n" + NetworkUtils.hexDump(array, array.length));
        } else {
            LOGGER.warning(this, "buffer " + buffer + "\n" + NetworkUtils.hexDump(buffer.array(), buffer.limit()));
        }
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
