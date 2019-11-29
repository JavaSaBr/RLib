package com.ss.rlib.logger.impl;

import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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
        var value = override[level.ordinal()];
        return Objects.requireNonNullElse(value, level.isEnabled());
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
    public void print(@NotNull LoggerLevel level, @NotNull Throwable exception) {
        if (isEnabled(level)) {
            loggerFactory.write(level, name, StringUtils.toString(exception));
        }
    }
}
