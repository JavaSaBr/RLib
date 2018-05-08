package com.ss.rlib.common.util.ref;

import static com.ss.rlib.common.util.ref.ReferenceType.BYTE;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The reference to byte value.
 *
 * @author JavaSaBr
 */
final class ByteReference extends AbstractReference {

    /**
     * The value of this reference.
     */
    private byte value;

    @Override
    public byte getByte() {
        return value;
    }

    @Override
    public short getShort() {
        return value;
    }

    @Override
    public int getInt() {
        return value;
    }

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public void setByte(final byte value) {
        this.value = value;
    }

    @Override
    public @NotNull ReferenceType getType() {
        return ReferenceType.BYTE;
    }

    @Override
    public boolean equals(@Nullable final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final ByteReference that = (ByteReference) object;
        return value == that.value;
    }

    @Override
    public void free() {
        this.value = 0;
    }

    @Override
    public int hashCode() {
        return (int) value;
    }

    @Override
    public String toString() {
        return "ByteReference{" +
                "value=" + value +
                "} " + super.toString();
    }
}
