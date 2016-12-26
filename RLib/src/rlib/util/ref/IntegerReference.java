package rlib.util.ref;

import static rlib.util.ref.ReferenceType.INTEGER;

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
    public void setInt(final int value) {
        this.value = value;
    }

    @NotNull
    @Override
    public ReferenceType getType() {
        return INTEGER;
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
    public String toString() {
        return "IntegerReference{" +
                "value=" + value +
                "} " + super.toString();
    }
}
