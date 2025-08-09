package javasabr.rlib.fx.control.input;

import javasabr.rlib.fx.util.converter.IntegerArrayStringConverter;

/**
 * The implementation of a text field control to edit integer values.
 *
 * @author JavaSaBr
 */
public final class IntegerArrayTextField extends TypedTextField<int[]> {

  @Override
  protected IntegerArrayStringConverter createValueConverter() {
    return new IntegerArrayStringConverter();
  }
}