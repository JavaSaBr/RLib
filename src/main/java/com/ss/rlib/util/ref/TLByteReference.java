package com.ss.rlib.util.ref;

import static com.ss.rlib.util.ref.ReferenceType.BYTE;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The reference to byte value.
 *
 * @author JavaSaBr
 */
final class TLByteReference extends AbstractThreadLocalReference {

    /**
     * The value of this reference.
     */
    private byte value;

    @Override
    public byte getByte() {
        return value;
    }

    @Override
    public void setByte(final byte value) {
        this.value = value;
    }

    @NotNull
    @Override
    public ReferenceType getType() {
        return BYTE;
    }

    @Override
    public boolean equals(@Nullable final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final TLByteReference that = (TLByteReference) object;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) value;
    }

    @Override
    public String toString() {
        return "TLByteReference{" +
                "value=" + value +
                "} " + super.toString();
    }
}
