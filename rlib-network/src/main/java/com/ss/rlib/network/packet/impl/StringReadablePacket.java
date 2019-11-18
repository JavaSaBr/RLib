package com.ss.rlib.network.packet.impl;

import com.ss.rlib.network.impl.StringDataConnection;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * @author JavaSaBr
 */
@Getter
public class StringReadablePacket extends AbstractReadablePacket<StringDataConnection> {

    /**
     * Read data.
     */
    private volatile @Nullable String data;

    @Override
    protected void readImpl(@NotNull StringDataConnection connection, @NotNull ByteBuffer buffer) {
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
