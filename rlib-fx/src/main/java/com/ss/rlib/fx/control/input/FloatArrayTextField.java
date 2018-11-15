package com.ss.rlib.fx.control.input;

import com.ss.rlib.fx.util.converter.FloatArrayStringConverter;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of a text field control to edit float values.
 *
 * @author JavaSaBr
 */
public final class FloatArrayTextField extends TypedTextField<float[]> {

    @Override
    protected @NotNull FloatArrayStringConverter createValueConverter() {
        return new FloatArrayStringConverter();
    }
}
