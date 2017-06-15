package com.ss.rlib.util.ref;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The reference to float value.
 *
 * @author JavaSaBr
 */
final class FloatReference extends AbstractReference {

    /**
     * The value of this reference.
     */
    private float value;

    @Override
    public float getFloat() {
        return value;
    }

    @Override
    public void setFloat(final float value) {
        this.value = value;
    }

    @NotNull
    @Override
    public ReferenceType getType() {
        return ReferenceType.FLOAT;
    }

    @Override
    public boolean equals(@Nullable final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final FloatReference that = (FloatReference) object;
        return Float.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return (value != +0.0f ? Float.floatToIntBits(value) : 0);
    }

    @Override
    public String toString() {
        return "FloatReference{" +
                "value=" + value +
                "} " + super.toString();
    }
}
