package rlib.util.ref;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import rlib.util.pools.Reusable;

/**
 * The interface for implementing a reference.
 *
 * @author JavaSaBr
 */
public interface Reference extends Reusable {

    default byte getByte() {
        throw new UnsupportedOperationException();
    }

    default void setByte(final byte value) {
        throw new UnsupportedOperationException();
    }

    default char getChar() {
        throw new UnsupportedOperationException();
    }

    default void setChar(final char value) {
        throw new UnsupportedOperationException();
    }

    default double getDouble() {
        throw new UnsupportedOperationException();
    }

    default void setDouble(final double value) {
        throw new UnsupportedOperationException();
    }

    default float getFloat() {
        throw new UnsupportedOperationException();
    }

    default void setFloat(final float value) {
        throw new UnsupportedOperationException();
    }

    default int getInt() {
        throw new UnsupportedOperationException();
    }

    default void setInt(final int value) {
        throw new UnsupportedOperationException();
    }

    default long getLong() {
        throw new UnsupportedOperationException();
    }

    default void setLong(final long value) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    default Object getObject() {
        throw new UnsupportedOperationException();
    }

    default void setObject(@Nullable final Object object) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    default ReferenceType getType() {
        throw new UnsupportedOperationException();
    }

    default short getShort() {
        throw new UnsupportedOperationException();
    }

    default void setShort(final short value) {
        throw new UnsupportedOperationException();
    }
}
