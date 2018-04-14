package com.ss.rlib.common.logging.impl;

import com.ss.rlib.common.function.TripleFunction;
import com.ss.rlib.common.function.TripleFunction;
import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerLevel;
import com.ss.rlib.common.logging.LoggerManager;
import com.ss.rlib.common.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The base implementation of the logger.
 *
 * @author JavaSaBr
 */
public final class LoggerImpl implements Logger {

    @NotNull
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
    public void setName(@NotNull final String name) {
        this.name = name;
    }

    @Override
    public void info(@NotNull final Object owner, @NotNull final String message) {
        if (!isEnabled(LoggerLevel.INFO)) return;
        LoggerManager.write(LoggerLevel.INFO, owner.getClass().getSimpleName(), message);
    }

    @Override
    public void info(@NotNull final String message) {
        if (!isEnabled(LoggerLevel.INFO)) return;
        LoggerManager.write(LoggerLevel.INFO, getName(), message);
    }

    @Override
    public boolean isEnabled(@NotNull final LoggerLevel level) {
        final Boolean value = override[level.ordinal()];
        return value != null && value || level.isEnabled();
    }

    @Override
    public boolean setEnabled(@NotNull final LoggerLevel level, final boolean enabled) {
        override[level.ordinal()] = enabled;
        return true;
    }

    @Override
    public boolean applyDefault(@NotNull final LoggerLevel level) {
        override[level.ordinal()] = null;
        return true;
    }

    @Override
    public void print(@NotNull final LoggerLevel level, @NotNull final String message) {
        if (!isEnabled(level)) return;
        LoggerManager.write(level, getName(), message);
    }

    @Override
    public void print(@NotNull final LoggerLevel level, @NotNull final Object owner, @NotNull final String message) {
        if (!isEnabled(level)) return;
        LoggerManager.write(level, owner.getClass().getSimpleName(), message);
    }

    @Override
    public void print(@NotNull final LoggerLevel level, @NotNull final Object owner,
                      @NotNull final Throwable exception) {
        if (!isEnabled(level)) return;
        LoggerManager.write(level, owner.getClass().getSimpleName(), StringUtils.toString(exception));
    }

    @Override
    public void print(@NotNull final LoggerLevel level, @NotNull final Throwable exception) {
        if (!isEnabled(level)) return;
        LoggerManager.write(level, getName(), StringUtils.toString(exception));
    }

    @Override
    public <T> void print(@NotNull final LoggerLevel level, @NotNull final Object owner, @NotNull final T arg,
                          @NotNull final Function<@NotNull T, String> messageFactory) {
        if (!isEnabled(level)) return;
        LoggerManager.write(level, owner.getClass().getSimpleName(), arg, messageFactory);
    }

    @Override
    public <F, S> void print(@NotNull final LoggerLevel level, @NotNull final Object owner, @NotNull final F first,
                             @NotNull final S second,
                             @NotNull final BiFunction<@NotNull F, @NotNull S, String> messageFactory) {
        if (!isEnabled(level)) return;
        LoggerManager.write(level, owner.getClass().getSimpleName(), first, second, messageFactory);

    }

    @Override
    public <F, S, T> void print(@NotNull final LoggerLevel level, @NotNull final Object owner, @NotNull final F first,
                                @NotNull final S second, @NotNull final T third,
                                @NotNull final TripleFunction<@NotNull F, @NotNull S, @NotNull T, String> messageFactory) {
        if (!isEnabled(level)) return;
        LoggerManager.write(level, owner.getClass().getSimpleName(), first, second, third, messageFactory);

    }

    @Override
    public <T> void print(@NotNull final LoggerLevel level, @NotNull final T arg,
                          @NotNull final Function<@NotNull T, String> messageFactory) {
        LoggerManager.write(level, getName(), arg, messageFactory);

    }

    @Override
    public <F, S> void print(@NotNull final LoggerLevel level, @NotNull final F first, @NotNull final S second,
                             @NotNull final BiFunction<@NotNull F, @NotNull S, String> messageFactory) {
        LoggerManager.write(level, getName(), first, second, messageFactory);
    }

    @Override
    public <F, S, T> void print(@NotNull final LoggerLevel level, @NotNull final F first, @NotNull final S second,
                                @NotNull final T third,
                                @NotNull final TripleFunction<@NotNull F, @NotNull S, @NotNull T, String> messageFactory) {
        LoggerManager.write(level, getName(), first, second, third, messageFactory);
    }
}
