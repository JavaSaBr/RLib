package com.ss.rlib.util.ref;

import static com.ss.rlib.util.ref.ReferenceType.OBJECT;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The reference to object.
 *
 * @author JavaSaBr
 */
final class ObjectReference extends AbstractReference {

    /**
     * The object of this reference.
     */
    private Object object;

    @Nullable
    @Override
    public Object getObject() {
        return object;
    }

    @Override
    public void setObject(@Nullable final Object object) {
        this.object = object;
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
        final ObjectReference that = (ObjectReference) object;
        return !(this.object != null ? !this.object.equals(that.object) : that.object != null);
    }

    @Override
    public int hashCode() {
        return object != null ? object.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ObjectReference{" +
                "object=" + object +
                "} " + super.toString();
    }
}
