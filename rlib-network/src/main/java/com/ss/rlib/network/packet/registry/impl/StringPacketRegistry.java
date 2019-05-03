package com.ss.rlib.network.packet.registry.impl;

import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.impl.simple.StringReadablePacket;
import com.ss.rlib.network.packet.registry.ReadablePacketRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * The simple string readable packet registry.
 *
 * @author JavaSaBr
 */
public class StringPacketRegistry implements ReadablePacketRegistry {

    private static final StringPacketRegistry INSTANCE = new StringPacketRegistry();

    public static @NotNull StringPacketRegistry getInstance() {
        return INSTANCE;
    }

    @Override
    public @NotNull ReadablePacket findById(int id) {
        return new StringReadablePacket();
    }
}
