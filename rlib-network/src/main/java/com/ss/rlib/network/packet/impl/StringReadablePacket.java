package com.ss.rlib.network.packet.impl;

import com.ss.rlib.network.Connection;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

@ToString
@Getter(onMethod_ = @Nullable)
public class StringReadablePacket extends AbstractReadablePacket {

    /**
     * Read data.
     */
    private volatile String data;

    @Override
    protected void readImpl(@NotNull Connection<?, ?> connection, @NotNull ByteBuffer buffer) {
        this.data = readString(buffer);
    }
}
