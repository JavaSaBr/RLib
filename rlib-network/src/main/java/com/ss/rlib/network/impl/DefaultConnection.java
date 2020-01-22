package com.ss.rlib.network.impl;

import com.ss.rlib.network.BufferAllocator;
import com.ss.rlib.network.Connection;
import com.ss.rlib.network.Network;
import com.ss.rlib.network.packet.impl.DefaultReadablePacket;
import com.ss.rlib.network.packet.impl.DefaultWritablePacket;
import com.ss.rlib.network.packet.registry.ReadablePacketRegistry;
import org.jetbrains.annotations.NotNull;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author JavaSaBr
 */
public class DefaultConnection extends IdBasedPacketConnection<DefaultReadablePacket, DefaultWritablePacket> {

    public DefaultConnection(
        @NotNull Network<? extends Connection<DefaultReadablePacket, DefaultWritablePacket>> network,
        @NotNull AsynchronousSocketChannel channel,
        @NotNull BufferAllocator bufferAllocator,
        @NotNull ReadablePacketRegistry<DefaultReadablePacket> packetRegistry
    ) {
        super(network, channel, bufferAllocator, packetRegistry, 100, 2, 2);
    }
}
