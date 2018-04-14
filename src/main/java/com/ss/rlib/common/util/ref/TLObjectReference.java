package com.ss.rlib.common.util.ref;

import static com.ss.rlib.common.util.ClassUtils.unsafeCast;
import static com.ss.rlib.common.util.ref.ReferenceType.OBJECT;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The reference to object.
 *
 * @author JavaSaBr
 */
final class TLObjectReference<T> extends AbstractThreadLocalReference {

    /**
     * The object of this reference.
     */
    private T object;

    @Override
    public @Nullable T getObject() {
        return object;
    }

    @Override
    public void setObject(@Nullable final Object object) {
        this.object = unsafeCast(object);
    }

    @Override
    public @NotNull ReferenceType getType() {
        return OBJECT;
    }

    @Override
    public boolean equals(@Nullable final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final TLObjectReference that = (TLObjectReference) object;
        return this.object == that.object;
    }

    @Override
    public int hashCode() {
        return object != null ? object.hashCode() : 0;
    }

    @Override
    public void free() {
        this.object = null;
    }

    @Override
    public String toString() {
        return "TLObjectReference{" +
                "object=" + object +
                "} " + super.toString();
    }
}
