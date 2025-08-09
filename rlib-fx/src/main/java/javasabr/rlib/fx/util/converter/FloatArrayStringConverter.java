package javasabr.rlib.fx.util.converter;

import javafx.util.StringConverter;
import javasabr.rlib.common.util.ArrayUtils;
import javasabr.rlib.common.util.StringUtils;
import org.jspecify.annotations.Nullable;

/**
 * The implementation of limited float string converter.
 *
 * @author JavaSaBr
 */
public class FloatArrayStringConverter extends StringConverter<float[]> {

  @Override
  public float[] fromString(@Nullable String string) {

    float[] newValue = null;

    if (!StringUtils.isEmpty(string)) {

      var splitter = string.contains(" ") ? " " : ",";
      var split = string.split(splitter);

      newValue = new float[split.length];

      for (var i = 0; i < split.length; i++) {
        newValue[i] = Float.parseFloat(split[i]);
      }
    }

    return newValue;
  }

  @Override
  public @Nullable String toString(@Nullable float[] value) {
    if (ArrayUtils.isEmpty(value)) {
      return StringUtils.EMPTY;
    } else {
      return ArrayUtils.toString(value, " ", false, false);
    }
  }
}
