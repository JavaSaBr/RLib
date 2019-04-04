package com.ss.rlib.logger.impl;

import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The base implementation of the logger.
 *
 * @author JavaSaBr
 */
public final class DefaultLogger implements Logger {

    private static final LoggerLevel[] VALUES = LoggerLevel.values();

    /**
     * The table of override enabled statuses.
     */
    private final Boolean[] override;

    /**
     * The logger name.
     */
    private final String name;

    /**
     * The default logger factory.
     */
    private final DefaultLoggerFactory loggerFactory;

    public DefaultLogger(@NotNull String name, @NotNull DefaultLoggerFactory loggerFactory) {
        this.name = name;
        this.loggerFactory = loggerFactory;
        this.override = new Boolean[VALUES.length];
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
            loggerFactory.write(level, name, message);
        }
    }

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull Object owner, @NotNull String message) {
        if (isEnabled(level)) {
            loggerFactory.write(level, owner.getClass().getSimpleName(), message);
        }
    }

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull Object owner, @NotNull Throwable exception) {
        if (isEnabled(level)) {
            loggerFactory.write(level, owner.getClass().getSimpleName(), StringUtils.toString(exception));
        }
    }

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull Throwable exception) {
        if (isEnabled(level)) {
            loggerFactory.write(level, name, StringUtils.toString(exception));
        }
    }

    @Override
    public <T> void print(
            @NotNull LoggerLevel level,
            @NotNull Object owner,
            @Nullable T arg,
            @NotNull Logger.SinFactory<T> messageFactory
    ) {
        if (isEnabled(level)) {
            loggerFactory.write(level, owner.getClass().getSimpleName(), arg, messageFactory);
        }
    }

    @Override
    public <F, S> void print(
            @NotNull LoggerLevel level,
            @NotNull Object owner,
            @Nullable F first,
            @Nullable S second,
            @NotNull Logger.BiFactory<F, S> messageFactory
    ) {
        if (isEnabled(level)) {
            loggerFactory.write(level, owner.getClass().getSimpleName(), first, second, messageFactory);
        }
    }

    @Override
    public <F, S, T> void print(
            @NotNull LoggerLevel level,
            @NotNull Object owner,
            @Nullable F first,
            @Nullable S second,
            @Nullable T third,
            @NotNull Logger.TriFactory<F, S, T> messageFactory
    ) {
        if (isEnabled(level)) {
            loggerFactory.write(level, owner.getClass().getSimpleName(), first, second, third, messageFactory);
        }
    }

    @Override
    public <T> void print(
            @NotNull LoggerLevel level,
            @Nullable T arg,
            @NotNull Logger.SinFactory<T> messageFactory
    ) {
        if (isEnabled(level)) {
            loggerFactory.write(level, name, arg, messageFactory);
        }
    }

    @Override
    public <F, S> void print(
            @NotNull LoggerLevel level,
            @Nullable F first,
            @Nullable S second,
            @NotNull Logger.BiFactory<F, S> messageFactory
    ) {
        if (isEnabled(level)) {
            loggerFactory.write(level, name, first, second, messageFactory);
        }
    }

    @Override
    public <F, S, T> void print(
            @NotNull LoggerLevel level,
            @Nullable F first,
            @Nullable S second,
            @Nullable T third,
            @NotNull Logger.TriFactory<F, S, T> messageFactory
    ) {
        if (isEnabled(level)) {
            loggerFactory.write(level, name, first, second, third, messageFactory);
        }
    }
}
