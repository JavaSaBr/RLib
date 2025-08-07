package com.ss.rlib.fx.util.converter;

import javasabr.rlib.common.util.ArrayUtils;
import javasabr.rlib.common.util.StringUtils;
import javafx.util.StringConverter;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of limited float string converter.
 *
 * @author JavaSaBr
 */
public class IntegerArrayStringConverter extends StringConverter<int[]> {

    @Override
    public int[] fromString(@Nullable String string) {

        int[] newValue = null;

        if (!StringUtils.isEmpty(string)) {

            var splitter = string.contains(" ") ? " " : ",";
            var split = string.split(splitter);

            newValue = new int[split.length];

            for (var i = 0; i < split.length; i++) {
                newValue[i] = Integer.parseInt(split[i]);
            }
        }

        return newValue;
    }

    @Override
    public @Nullable String toString(@Nullable int[] value) {
        if (ArrayUtils.isEmpty(value)) {
            return StringUtils.EMPTY;
        } else {
            return ArrayUtils.toString(value, " ", false, false);
        }
    }
}
