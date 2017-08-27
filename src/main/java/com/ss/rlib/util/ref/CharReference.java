package com.ss.rlib.util.ref;

import static com.ss.rlib.util.ref.ReferenceType.CHAR;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The reference to char value.
 *
 * @author JavaSaBr
 */
final class CharReference extends AbstractReference {

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

    @Override
    public @NotNull ReferenceType getType() {
        return CHAR;
    }

    @Override
    public boolean equals(@Nullable final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final CharReference that = (CharReference) object;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) value;
    }

    @Override
    public void free() {
        this.value = 0;
    }

    @Override
    public String toString() {
        return "CharReference{" +
                "value=" + value +
                "} " + super.toString();
    }
}
