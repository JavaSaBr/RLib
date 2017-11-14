package com.ss.rlib.logging.impl;

import com.ss.rlib.function.TripleFunction;
import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerLevel;
import com.ss.rlib.logging.LoggerManager;
import com.ss.rlib.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The base implementation of the logger.
 *
 * @author JavaSaBr
 */
public final class LoggerImpl implements Logger {

    /**
     * The logger name.
     */
    @NotNull
    private String name;

    public LoggerImpl() {
        this.name = "<empty name>";
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
        LoggerManager.write(LoggerLevel.INFO, owner.getClass().getSimpleName(), message);
    }

    @Override
    public void info(@NotNull final String message) {
        LoggerManager.write(LoggerLevel.INFO, getName(), message);
    }

    @Override
    public boolean isEnabledDebug() {
        return LoggerLevel.DEBUG.isEnabled();
    }

    @Override
    public boolean isEnabledError() {
        return LoggerLevel.ERROR.isEnabled();
    }

    @Override
    public boolean isEnabledInfo() {
        return LoggerLevel.INFO.isEnabled();
    }

    @Override
    public boolean isEnabledWarning() {
        return LoggerLevel.WARNING.isEnabled();
    }

    @Override
    public void print(@NotNull final LoggerLevel level, @NotNull final String message) {
        LoggerManager.write(level, getName(), message);
    }

    @Override
    public void print(@NotNull final LoggerLevel level, @NotNull final Object owner, @NotNull final String message) {
        if (!level.isEnabled()) return;
        LoggerManager.write(level, owner.getClass().getSimpleName(), message);
    }

    @Override
    public void print(@NotNull final LoggerLevel level, @NotNull final Object owner,
                      @NotNull final Throwable exception) {
        if (!level.isEnabled()) return;
        LoggerManager.write(level, owner.getClass().getSimpleName(), StringUtils.toString(exception));
    }

    @Override
    public void print(@NotNull final LoggerLevel level, @NotNull final Throwable exception) {
        if (!level.isEnabled()) return;
        LoggerManager.write(level, getName(), StringUtils.toString(exception));
    }

    @Override
    public <T> void print(@NotNull final LoggerLevel level, @NotNull final Object owner, @NotNull final T arg,
                          @NotNull final Function<@NotNull T, String> messageFactory) {
        if (!level.isEnabled()) return;
        LoggerManager.write(level, owner.getClass().getSimpleName(), arg, messageFactory);
    }

    @Override
    public <F, S> void print(@NotNull final LoggerLevel level, @NotNull final Object owner, @NotNull final F first,
                             @NotNull final S second,
                             @NotNull final BiFunction<@NotNull F, @NotNull S, String> messageFactory) {
        if (!level.isEnabled()) return;
        LoggerManager.write(level, owner.getClass().getSimpleName(), first, second, messageFactory);

    }

    @Override
    public <F, S, T> void print(@NotNull final LoggerLevel level, @NotNull final Object owner, @NotNull final F first,
                                @NotNull final S second, @NotNull final T third,
                                @NotNull final TripleFunction<@NotNull F, @NotNull S, @NotNull T, String> messageFactory) {
        if (!level.isEnabled()) return;
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
