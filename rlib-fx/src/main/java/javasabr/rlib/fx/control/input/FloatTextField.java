package javasabr.rlib.fx.control.input;

import javasabr.rlib.fx.util.converter.LimitedFloatStringConverter;
import javasabr.rlib.fx.util.converter.LimitedNumberStringConverter;
import javafx.scene.input.ScrollEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of a text field control to edit float values.
 *
 * @author JavaSaBr
 */
public final class FloatTextField extends NumberTextField<Float> {

    public FloatTextField() {
        setValue(0F);
    }

    @Override
    protected @NotNull LimitedNumberStringConverter<Float> createValueConverter() {
        return new LimitedFloatStringConverter();
    }

    @Override
    protected void scrollValueImpl(@NotNull ScrollEvent event) {
        super.scrollValueImpl(event);

        var value = getValue();

        var longValue = (long) (value * 1000);
        longValue += event.getDeltaY() * (getScrollPower() * (event.isShiftDown() ? 0.5F : 1F));

        var resultValue = longValue / 1000F;
        var stringValue = String.valueOf(resultValue);

        var textFormatter = getTextFormatter();
        var valueConverter = textFormatter.getValueConverter();
        try {
            valueConverter.fromString(stringValue);
        } catch (RuntimeException e) {
            return;
        }

        setText(stringValue);
        positionCaret(stringValue.length());
    }

    /**
     * Gets a primitive current value.
     *
     * @return the current value or 0.
     */
    public float getPrimitiveValue() {
        var textFormatter = getTypedTextFormatter();
        var value = textFormatter.getValue();
        return value == null ? 0F : value;
    }
}
