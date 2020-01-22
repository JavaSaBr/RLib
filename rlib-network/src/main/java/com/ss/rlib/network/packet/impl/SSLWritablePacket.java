package com.ss.rlib.network.packet.impl;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Packet marker.
 */
public class SSLWritablePacket extends AbstractWritablePacket {

    private static final SSLWritablePacket INSTANCE = new SSLWritablePacket();

    public static SSLWritablePacket getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean write(@NotNull ByteBuffer buffer) {
        return true;
    }
}
