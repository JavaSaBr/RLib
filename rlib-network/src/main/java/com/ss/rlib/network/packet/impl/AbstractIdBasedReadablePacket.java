package com.ss.rlib.network.packet.impl;

import com.ss.rlib.network.packet.IdBasedReadablePacket;

/**
 * @author JavaSaBr
 */
public abstract class AbstractIdBasedReadablePacket<S extends AbstractIdBasedReadablePacket<S>> extends
    AbstractReadablePacket implements IdBasedReadablePacket<S> {
}
