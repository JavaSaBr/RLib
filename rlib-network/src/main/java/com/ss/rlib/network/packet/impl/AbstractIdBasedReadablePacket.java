package com.ss.rlib.network.packet.impl;

import com.ss.rlib.network.Connection;
import com.ss.rlib.network.packet.IdBasedReadablePacket;

/**
 * @author JavaSaBr
 */
public abstract class AbstractIdBasedReadablePacket<C extends Connection<?, ?>, S extends AbstractIdBasedReadablePacket<C, S>> extends
    AbstractReadablePacket<C> implements IdBasedReadablePacket<S> {
}
