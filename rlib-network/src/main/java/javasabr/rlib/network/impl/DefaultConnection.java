package javasabr.rlib.network.impl;

import java.nio.channels.AsynchronousSocketChannel;
import javasabr.rlib.network.BufferAllocator;
import javasabr.rlib.network.Connection;
import javasabr.rlib.network.Network;
import javasabr.rlib.network.packet.impl.DefaultReadablePacket;
import javasabr.rlib.network.packet.impl.DefaultWritablePacket;
import javasabr.rlib.network.packet.registry.ReadablePacketRegistry;
import org.jetbrains.annotations.NotNull;

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
