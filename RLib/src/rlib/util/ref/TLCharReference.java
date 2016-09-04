package rlib.util.ref;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static rlib.util.ref.ReferenceType.CHAR;

/**
 * The reference to char value.
 *
 * @author JavaSaBr
 */
final class TLCharReference extends AbstractThreadLocalReference {

    /**
     * The value of this reference.
     */
    private char value;

    @Override
    public char getChar() {
        return value;
    }

    @Override
    public void setChar(final char value) {
        this.value = value;
    }

    @NotNull
    @Override
    public ReferenceType getType() {
        return CHAR;
    }

    @Override
    public boolean equals(@Nullable final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final TLCharReference that = (TLCharReference) object;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) value;
    }

    @Override
    public String toString() {
        return "TLCharReference{" +
                "value=" + value +
                "} " + super.toString();
    }
}
