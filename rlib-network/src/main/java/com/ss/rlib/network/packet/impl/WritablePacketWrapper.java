package com.ss.rlib.network.packet.impl;

import com.ss.rlib.network.packet.WritablePacket;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The writable packet wrapper with additional attachment.
 *
 * @param <A> the attachment type.
 * @param <W> the Writable packet.
 */
@Getter
@RequiredArgsConstructor
public class WritablePacketWrapper<A, W extends WritablePacket> implements WritablePacket {

    private final A attachment;
    private final W packet;

    @Override
    public boolean write(@NotNull ByteBuffer buffer) {
        return packet.write(buffer);
    }

    @Override
    public int getExpectedLength() {
        return packet.getExpectedLength();
    }

    @Override
    public @NotNull String getName() {
        return "WritablePacketWrapper";
    }
}
