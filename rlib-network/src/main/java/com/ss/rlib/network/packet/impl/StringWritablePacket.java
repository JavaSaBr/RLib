package com.ss.rlib.network.packet.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * @author JavaSaBr
 */
@RequiredArgsConstructor
public class StringWritablePacket extends AbstractWritablePacket {

    @NotNull
    private final String data;

    @Override
    protected void writeImpl(@NotNull ByteBuffer buffer) {
        super.writeImpl(buffer);
        writeString(buffer, data);
    }

    @Override
    public int getExpectedLength() {
        return 4 + data.length() * 2;
    }
}
