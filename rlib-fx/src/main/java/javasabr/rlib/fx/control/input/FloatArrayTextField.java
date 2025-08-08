package javasabr.rlib.fx.control.input;

import javasabr.rlib.fx.util.converter.FloatArrayStringConverter;

/**
 * The implementation of a text field control to edit float values.
 *
 * @author JavaSaBr
 */
public final class FloatArrayTextField extends TypedTextField<float[]> {

  @Override
  protected FloatArrayStringConverter createValueConverter() {
    return new FloatArrayStringConverter();
  }
}
