package com.ss.rlib.fx.util.converter;

import com.ss.rlib.common.util.StringUtils;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of limited float string converter.
 *
 * @author JavaSaBr
 */
public class LimitedFloatStringConverter extends LimitedNumberStringConverter<Float> {

    @Override
    public @Nullable Float fromString(@Nullable String value) {

        if (StringUtils.isEmpty(value)) {
            return null;
        }

        var result = Float.valueOf(value);
        var minValue = getMinValue();
        var maxValue = getMaxValue();

        if (minValue != null && result < minValue) {
            throw new IllegalArgumentException();
        } else if (maxValue != null && result > maxValue) {
            throw new IllegalArgumentException();
        }

        return result;
    }
}
