package rlib.util.ref;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static rlib.util.ref.ReferenceType.SHORT;

/**
 * The reference to short value.
 *
 * @author JavaSaBr
 */
final class TLShortReference extends AbstractThreadLocalReference {

    /**
     * The value of this reference.
     */
    private short value;

    @Override
    public short getShort() {
        return value;
    }

    @Override
    public void setShort(final short value) {
        this.value = value;
    }

    @NotNull
    @Override
    public ReferenceType getType() {
        return SHORT;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TLShortReference that = (TLShortReference) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "TLShortReference{" +
                "value=" + value +
                "} " + super.toString();
    }
}
