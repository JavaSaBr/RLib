package com.ss.rlib.fx.util.converter;

import javasabr.rlib.common.util.StringUtils;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of limited integer string converter.
 *
 * @author JavaSaBR
 */
public class LimitedIntegerStringConverter extends LimitedNumberStringConverter<Integer> {

    @Override
    public @Nullable Integer fromString(@Nullable String value) {

        if (StringUtils.isEmpty(value)) {
            return null;
        }

        var result = Integer.valueOf(value);
        var minValue = getMinValue();
        var maxValue = getMaxValue();

        if (minValue != null && result < minValue) {
            throw new IllegalArgumentException();
        } else if (maxValue != null && result > getMaxValue()) {
            throw new IllegalArgumentException();
        }

        return result;
    }

    @Override
    public @Nullable String toString(@Nullable Integer value) {

        if (value == null) {
            return StringUtils.EMPTY;
        }

        return Integer.toString(value);
    }
}
