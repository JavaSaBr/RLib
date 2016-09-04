package rlib.util.ref;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static rlib.util.ref.ReferenceType.OBJECT;

/**
 * The reference to object.
 *
 * @author JavaSaBr
 */
final class TLObjectReference extends AbstractThreadLocalReference {

    /**
     * The object of this reference.
     */
    private Object ref;

    @Nullable
    @Override
    public Object getObject() {
        return ref;
    }

    @Override
    public void setObject(@Nullable final Object object) {
        this.ref = object;
    }

    @NotNull
    @Override
    public ReferenceType getType() {
        return OBJECT;
    }

    @Override
    public boolean equals(@Nullable final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final TLObjectReference that = (TLObjectReference) object;
        return ref == that.ref;
    }

    @Override
    public int hashCode() {
        return ref != null ? ref.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TLObjectReference{" +
                "ref=" + ref +
                "} " + super.toString();
    }
}
