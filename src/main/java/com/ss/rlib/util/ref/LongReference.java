package com.ss.rlib.util.ref;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The reference to long value.
 *
 * @author JavaSaBr
 */
final class LongReference extends AbstractReference {

    /**
     * The value of this reference.
     */
    private long value;

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public void setLong(final long value) {
        this.value = value;
    }

    @Override
    public void setByte(final byte value) {
        this.value = value;
    }

    @Override
    public void setShort(final short value) {
        this.value = value;
    }

    @Override
    public void setInt(final int value) {
        this.value = value;
    }

    @Override
    public @NotNull ReferenceType getType() {
        return ReferenceType.LONG;
    }

    @Override
    public boolean equals(@Nullable final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final LongReference that = (LongReference) object;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    @Override
    public void free() {
        this.value = 0;
    }

    @Override
    public String toString() {
        return "LongReference{" +
                "value=" + value +
                "} " + super.toString();
    }
}
