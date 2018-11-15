package com.ss.rlib.fx.scene.input;

import static com.ss.rlib.common.util.ClassUtils.unsafeCast;
import com.ss.rlib.common.util.ref.Reference;
import javafx.scene.input.DataFormat;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * The implementation of a clipboard/dragboard content for internal using.
 *
 * @author JavaSaBr
 */
public class InternalContent extends HashMap<DataFormat, Object> {

    private static final long serialVersionUID = 2133513859087232216L;

    public static final DataFormat DATA_OBJECT = new DataFormat("internal/object");
    public static final DataFormat DATA_STRING = new DataFormat("internal/string");
    public static final DataFormat DATA_NUMBER = new DataFormat("internal/number");
    public static final DataFormat DATA_REFERENCE = new DataFormat("internal/reference");

    /**
     * Gets a number.
     *
     * @return the number or null.
     */
    public @Nullable Number getNumber() {
        return (Number) get(DATA_NUMBER);
    }

    /**
     * Gets an object.
     *
     * @param <T> the object's type.
     * @return the object or null.
     */
    public <T> @Nullable T getObject() {
        return unsafeCast(get(DATA_OBJECT));
    }

    /**
     * Gets a reference.
     *
     * @return the reference or null.
     */
    public @Nullable Reference getReference() {
        return (Reference) get(DATA_REFERENCE);
    }

    /**
     * Gets a string.
     *
     * @return the string or null.
     */
    public @Nullable String getString() {
        return (String) get(DATA_STRING);
    }

    /**
     * Puts a number.
     *
     * @param number the number or null.
     */
    public void putNumber(@Nullable Number number) {
        put(DATA_NUMBER, number);
    }

    /**
     * Puts an object.
     *
     * @param object the object or null.
     */
    public void putObject(@Nullable Object object) {
        put(DATA_OBJECT, object);
    }

    /**
     * Puts a reference.
     *
     * @param reference the reference or null.
     */
    public void putReference(@Nullable Reference reference) {
        put(DATA_REFERENCE, reference);
    }

    /**
     * Puts a string.
     *
     * @param string the string or null.
     */
    public void putString(@Nullable String string) {
        put(DATA_STRING, string);
    }
}