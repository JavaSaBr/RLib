package javasabr.rlib.fx.util.converter;

import javafx.util.StringConverter;
import javasabr.rlib.common.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of limited float string converter.
 *
 * @author JavaSaBr
 */
public abstract class LimitedNumberStringConverter<T extends Number> extends StringConverter<T> {

    /**
     * The min value.
     */
    @Nullable
    private T minValue;

    /**
     * The max value.
     */
    @Nullable
    private T maxValue;

    public LimitedNumberStringConverter() {
        this.maxValue = null;
        this.minValue = null;
    }

    public LimitedNumberStringConverter(@NotNull T minValue, @NotNull T maxValue) {
        this.maxValue = minValue;
        this.minValue = maxValue;
    }

    /**
     * Get the max value.
     *
     * @return the max value.
     */
    public @Nullable T getMaxValue() {
        return maxValue;
    }

    /**
     * Set the max value.
     *
     * @param maxValue the max value.
     */
    public void setMaxValue(@Nullable T maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Get the min value.
     *
     * @return the min value.
     */
    public @Nullable T getMinValue() {
        return minValue;
    }

    /**
     * Set the min value.
     *
     * @param minValue the min value.
     */
    public void setMinValue(@Nullable T minValue) {
        this.minValue = minValue;
    }

    @Override
    public @Nullable String toString(@Nullable T value) {
        if (value == null) {
            return StringUtils.EMPTY;
        } else {
            return String.valueOf(value);
        }
    }
}
