package com.ss.rlib.network.packet.impl;

import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.packet.IdBasedReadablePacket;
import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
public abstract class AbstractIdBasedReadablePacket<C extends Connection<?, ?>, S extends AbstractIdBasedReadablePacket<C, S>> extends
    AbstractReadablePacket<C> implements IdBasedReadablePacket<S> {

    private static final Logger LOGGER = LoggerManager.getLogger(AbstractIdBasedReadablePacket.class);

    @Override
    public void execute(@NotNull Connection<?, ?> connection) {
        try {
            executeImpl(ClassUtils.unsafeNNCast(connection));
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    protected void executeImpl(@NotNull C connection) {
    }
}
