package com.ss.rlib.common.util.ref;

import com.ss.rlib.common.util.pools.Reusable;
import com.ss.rlib.common.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface for implementing a reference.
 *
 * @author JavaSaBr
 */
public interface Reference extends Reusable {

    /**
     * Gets byte.
     *
     * @return the byte
     */
    default byte getByte() {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets byte.
     *
     * @param value the value
     */
    default void setByte(final byte value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets char.
     *
     * @return the char
     */
    default char getChar() {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets char.
     *
     * @param value the value
     */
    default void setChar(final char value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets double.
     *
     * @return the double
     */
    default double getDouble() {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets double.
     *
     * @param value the value
     */
    default void setDouble(final double value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets float.
     *
     * @return the float
     */
    default float getFloat() {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets float.
     *
     * @param value the value
     */
    default void setFloat(final float value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets int.
     *
     * @return the int
     */
    default int getInt() {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets int.
     *
     * @param value the value
     */
    default void setInt(final int value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets long.
     *
     * @return the long
     */
    default long getLong() {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets long.
     *
     * @param value the value
     */
    default void setLong(final long value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets object.
     *
     * @return the object
     */
    default @Nullable Object getObject() {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets object.
     *
     * @param object the object
     */
    default void setObject(@Nullable final Object object) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    default @NotNull ReferenceType getType() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets short.
     *
     * @return the short
     */
    default short getShort() {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets short.
     *
     * @param value the value
     */
    default void setShort(final short value) {
        throw new UnsupportedOperationException();
    }
}
