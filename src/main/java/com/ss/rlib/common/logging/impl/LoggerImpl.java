package com.ss.rlib.common.logging.impl;

import static com.ss.rlib.common.logging.LoggerManager.write;
import com.ss.rlib.common.function.TripleFunction;
import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerLevel;
import com.ss.rlib.common.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The base implementation of the logger.
 *
 * @author JavaSaBr
 */
public final class LoggerImpl implements Logger {

    private static final LoggerLevel[] VALUES = LoggerLevel.values();

    /**
     * The table of override enabled statuses.
     */
    private final Boolean[] override;

    /**
     * The logger name.
     */
    @NotNull
    private String name;

    public LoggerImpl() {
        this.name = "<empty name>";
        this.override = new Boolean[VALUES.length];
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public boolean isEnabled(@NotNull LoggerLevel level) {
        Boolean value = override[level.ordinal()];
        return value != null && value || level.isEnabled();
    }

    @Override
    public boolean setEnabled(@NotNull LoggerLevel level, boolean enabled) {
        override[level.ordinal()] = enabled;
        return true;
    }

    @Override
    public boolean applyDefault(@NotNull LoggerLevel level) {
        override[level.ordinal()] = null;
        return true;
    }

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull String message) {
        if (isEnabled(level)) {
            write(level, getName(), message);
        }
    }

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull Object owner, @NotNull String message) {
        if (isEnabled(level)) {
            write(level, owner.getClass().getSimpleName(), message);
        }
    }

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull Object owner, @NotNull Throwable exception) {
        if (isEnabled(level)) {
            write(level, owner.getClass().getSimpleName(), StringUtils.toString(exception));
        }
    }

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull Throwable exception) {
        if (isEnabled(level)) {
            write(level, getName(), StringUtils.toString(exception));
        }
    }

    @Override
    public <T> void print(
            @NotNull LoggerLevel level,
            @NotNull Object owner,
            @Nullable T arg,
            @NotNull Function<T, @NotNull String> messageFactory
    ) {
        if (isEnabled(level)) {
            write(level, owner.getClass().getSimpleName(), arg, messageFactory);
        }
    }

    @Override
    public <F, S> void print(
            @NotNull LoggerLevel level,
            @NotNull Object owner,
            @Nullable F first,
            @Nullable S second,
            @NotNull BiFunction<F, S, @NotNull String> messageFactory
    ) {
        if (isEnabled(level)) {
            write(level, owner.getClass().getSimpleName(), first, second, messageFactory);
        }
    }

    @Override
    public <F, S, T> void print(
            @NotNull LoggerLevel level,
            @NotNull Object owner,
            @Nullable F first,
            @Nullable S second,
            @Nullable T third,
            @NotNull TripleFunction<F, S, T, @NotNull String> messageFactory
    ) {
        if (isEnabled(level)) {
            write(level, owner.getClass().getSimpleName(), first, second, third, messageFactory);
        }
    }

    @Override
    public <T> void print(
            @NotNull LoggerLevel level,
            @Nullable T arg,
            @NotNull Function<T, @NotNull String> messageFactory
    ) {
        if (isEnabled(level)) {
            write(level, getName(), arg, messageFactory);
        }
    }

    @Override
    public <F, S> void print(
            @NotNull LoggerLevel level,
            @Nullable F first,
            @Nullable S second,
            @NotNull BiFunction<F, S, @NotNull String> messageFactory
    ) {
        if (isEnabled(level)) {
            write(level, getName(), first, second, messageFactory);
        }
    }

    @Override
    public <F, S, T> void print(
            @NotNull LoggerLevel level,
            @Nullable F first,
            @Nullable S second,
            @Nullable T third,
            @NotNull TripleFunction<F, S, T, @NotNull String> messageFactory
    ) {
        if (isEnabled(level)) {
            write(level, getName(), first, second, third, messageFactory);
        }
    }
}
