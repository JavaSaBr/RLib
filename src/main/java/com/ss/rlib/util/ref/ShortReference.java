package com.ss.rlib.util.ref;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The reference to short value.
 *
 * @author JavaSaBr
 */
final class ShortReference extends AbstractReference {

    /**
     * The value of this reference.
     */
    private short value;

    @Override
    public @NotNull ReferenceType getType() {
        return ReferenceType.SHORT;
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
    public void setShort(final short value) {
        this.value = value;
    }

    @Override
    public void setByte(final byte value) {
        this.value = value;
    }

    @Override
    public boolean equals(@Nullable final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final ShortReference that = (ShortReference) object;
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
        return "ShortReference{" + "value=" + value + '}';
    }
}
