package rlib.util.ref;

import static rlib.util.ref.ReferenceType.LONG;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The reference to long value.
 *
 * @author JavaSaBr
 */
final class TLLongReference extends AbstractThreadLocalReference {

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

    @NotNull
    @Override
    public ReferenceType getType() {
        return LONG;
    }

    @Override
    public boolean equals(@Nullable final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final TLLongReference that = (TLLongReference) object;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    @Override
    public String toString() {
        return "TLLongReference{" +
                "value=" + value +
                "} " + super.toString();
    }
}
