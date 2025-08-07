package javasabr.rlib.network.packet.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * @author JavaSaBr
 */
@RequiredArgsConstructor
public class StringWritablePacket extends AbstractWritablePacket {

    private final @NotNull String data;

    @Override
    protected void writeImpl(@NotNull ByteBuffer buffer) {
        super.writeImpl(buffer);
        writeString(buffer, data);
    }

    @Override
    public int getExpectedLength() {
        return 4 + data.length() * 2;
    }

    @Override
    public @NotNull String toString() {
        return "StringWritablePacket {\n" + "\"\tdataLength\":" + data.length() + "\n}";
    }
}
