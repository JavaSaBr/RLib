package com.ss.rlib.network.packet.impl;

import com.ss.rlib.network.Connection;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

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

    @Override
    public @NotNull String toString() {

        var data = getData();

        if (data != null && data.length() > 20) {
            data = data.substring(0, 9) + "..." + data.substring(9, 19);
        }

        return "StringReadablePacket(" + "data='" + data + '\'' + ')';
    }
}
