package javasabr.rlib.fx.control.input;

import java.util.Objects;
import javafx.scene.input.ScrollEvent;
import javasabr.rlib.fx.util.converter.LimitedFloatStringConverter;
import javasabr.rlib.fx.util.converter.LimitedNumberStringConverter;

/**
 * The implementation of a text field control to edit float values.
 *
 * @author JavaSaBr
 */
public final class FloatTextField extends NumberTextField<Float> {

  public static final Float DEFAULT_VALUE = 0F;

  public FloatTextField() {
    setValue(DEFAULT_VALUE);
  }

  @Override
  protected LimitedNumberStringConverter<Float> createValueConverter() {
    return new LimitedFloatStringConverter();
  }

  @Override
  protected void scrollValueImpl(ScrollEvent event) {
    super.scrollValueImpl(event);

    double toAdd = event.getDeltaY() * (getScrollPower() * (event.isShiftDown() ? 0.5F : 1F));
    float value = Objects.requireNonNullElse(getValue(), DEFAULT_VALUE);
    long longValue = ((long) (value * 1000L)) + (long) toAdd;
    float resultValue = longValue / 1000F;

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
    return value == null ? DEFAULT_VALUE : value;
  }
}
