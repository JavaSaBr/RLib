package javasabr.rlib.fx.control.input;

import javasabr.rlib.fx.util.converter.IntegerArrayStringConverter;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of a text field control to edit integer values.
 *
 * @author JavaSaBr
 */
public final class IntegerArrayTextField extends TypedTextField<int[]> {

    @Override
    protected @NotNull IntegerArrayStringConverter createValueConverter() {
        return new IntegerArrayStringConverter();
    }
}