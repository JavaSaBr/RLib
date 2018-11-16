package com.ss.rlib.fx.control.input;

import com.ss.rlib.fx.util.converter.LimitedNumberStringConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of a text field control to edit float values.
 *
 * @author JavaSaBr
 */
public abstract class NumberTextField<T extends Number> extends ScrollableTypedTextField<T> {

    public NumberTextField() {
        super();
        setScrollPower(30);
    }

    public NumberTextField(@NotNull String text) {
        super(text);
        setScrollPower(30);
    }

    @Override
    protected @NotNull LimitedNumberStringConverter<T> createValueConverter() {
        throw new UnsupportedOperationException();
    }

    /**
     * Set value limits for this field.
     *
     * @param min the min value.
     * @param max the max value.
     */
    public void setMinMax(@Nullable T min, @Nullable T max) {

        var converter = (LimitedNumberStringConverter<T>) getTypedTextFormatter()
                .getValueConverter();

        converter.setMaxValue(max);
        converter.setMinValue(min);
    }

    /**
     * Set the min value of this field.
     *
     * @param value the min value of this field.
     */
    public void setMinValue(@Nullable T value) {

        var converter = (LimitedNumberStringConverter<T>) getTypedTextFormatter()
                .getValueConverter();

        converter.setMinValue(value);
    }

    /**
     * Get a min value of this field.
     *
     * @return the min value of this field.
     */
    public @Nullable T getMinValue() {

        var converter = (LimitedNumberStringConverter<T>) getTypedTextFormatter()
                .getValueConverter();

        return converter.getMinValue();
    }

    /**
     * Set the max value of this field.
     *
     * @param value the max value of this field.
     */
    public void setMaxValue(@Nullable T value) {

        var converter = (LimitedNumberStringConverter<T>) getTypedTextFormatter()
                .getValueConverter();

        converter.setMaxValue(value);
    }

    /**
     * Get a max value of this field.
     *
     * @return the min value of this field.
     */
    public @Nullable T getMaxValue() {

        var converter = (LimitedNumberStringConverter<T>) getTypedTextFormatter()
                .getValueConverter();

        return converter.getMaxValue();
    }
}
