package com.ss.rlib.common.util.ref;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The reference to double value.
 *
 * @author JavaSaBr
 */
final class DoubleReference extends AbstractReference {

    /**
     * The value of this reference.
     */
    private double value;

    @Override
    public double getDouble() {
        return value;
    }

    @Override
    public void setDouble(final double value) {
        this.value = value;
    }

    @Override
    public void setFloat(final float value) {
        this.value = value;
    }

    @Override
    public @NotNull ReferenceType getType() {
        return ReferenceType.DOUBLE;
    }

    @Override
    public boolean equals(@Nullable final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final DoubleReference that = (DoubleReference) object;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }

    @Override
    public void free() {
        this.value = 0;
    }

    @Override
    public String toString() {
        return "DoubleReference{" +
                "value=" + value +
                "} " + super.toString();
    }
}
