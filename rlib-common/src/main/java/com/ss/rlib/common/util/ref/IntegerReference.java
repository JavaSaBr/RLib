package com.ss.rlib.common.util.ref;

import static com.ss.rlib.common.util.ref.ReferenceType.INTEGER;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The reference to integer value.
 *
 * @author JavaSaBr
 */
final class IntegerReference extends AbstractReference {

    /**
     * The value of this reference.
     */
    private int value;

    @Override
    public int getInt() {
        return value;
    }

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public void setInt(final int value) {
        this.value = value;
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
    public @NotNull ReferenceType getType() {
        return ReferenceType.INTEGER;
    }

    @Override
    public boolean equals(@Nullable final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final IntegerReference that = (IntegerReference) object;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public void free() {
        this.value = 0;
    }

    @Override
    public String toString() {
        return "IntegerReference{" +
                "value=" + value +
                "} " + super.toString();
    }
}
