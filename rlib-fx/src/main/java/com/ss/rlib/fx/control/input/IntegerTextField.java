package com.ss.rlib.fx.control.input;

import com.ss.rlib.fx.util.converter.LimitedIntegerStringConverter;
import com.ss.rlib.fx.util.converter.LimitedNumberStringConverter;
import javafx.scene.input.ScrollEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of a text field control to edit integer values.
 *
 * @author JavaSaBr
 */
public final class IntegerTextField extends NumberTextField<Integer> {

    public IntegerTextField() {
        setValue(0);
    }

    @Override
    protected @NotNull LimitedNumberStringConverter<Integer> createValueConverter() {
        return new LimitedIntegerStringConverter();
    }

    @Override
    protected void scrollValueImpl(@NotNull ScrollEvent event) {
        super.scrollValueImpl(event);

        var value = getValue();

        var longValue = (long) (value * 1000);
        longValue += event.getDeltaY() * (getScrollPower() * (event.isShiftDown() ? 0.5F : 1F));

        var resultValue = (int) (longValue / 1000F);
        var stringValue = String.valueOf(resultValue);

        var textFormatter = getTextFormatter();
        var valueConverter = textFormatter.getValueConverter();
        try {
            valueConverter.fromString(stringValue);
        } catch (final RuntimeException e) {
            return;
        }

        setText(stringValue);
        positionCaret(stringValue.length());
    }

    /**
     * Get the current primitive value.
     *
     * @return the current value or 0.
     */
    public int getPrimitiveValue() {

        var value = getTypedTextFormatter()
                .getValue();

        return value == null ? 0 : value;
    }
}
