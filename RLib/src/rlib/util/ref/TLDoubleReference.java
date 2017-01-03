package rlib.util.ref;

import static rlib.util.ref.ReferenceType.DOUBLE;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The reference to double value.
 *
 * @author JavaSaBr
 */
final class TLDoubleReference extends AbstractThreadLocalReference {

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

    @NotNull
    @Override
    public ReferenceType getType() {
        return DOUBLE;
    }

    @Override
    public boolean equals(@Nullable final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final TLDoubleReference that = (TLDoubleReference) object;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }

    @Override
    public String toString() {
        return "TLDoubleReference{" +
                "value=" + value +
                "} " + super.toString();
    }
}
