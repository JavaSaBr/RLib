package com.ss.rlib.logging.impl;

import com.ss.rlib.logging.LoggerLevel;
import org.jetbrains.annotations.NotNull;

import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerManager;
import com.ss.rlib.util.StringUtils;

/**
 * The base implementation of a logger.
 *
 * @author JavaSaBr
 */
public final class LoggerImpl implements Logger {

    /**
     * The owner name.
     */
    @NotNull
    private String name;

    /**
     * Instantiates a new Logger.
     */
    public LoggerImpl() {
        this.name = "<empty name>";
    }

    @Override
    public void debug(@NotNull final Class<?> cs, @NotNull final String message) {
        LoggerManager.write(LoggerLevel.DEBUG, cs.getSimpleName(), message);
    }

    @Override
    public void debug(@NotNull final Object owner, @NotNull final String message) {
        LoggerManager.write(LoggerLevel.DEBUG, owner.getClass().getSimpleName(), message);
    }

    @Override
    public void debug(@NotNull final String message) {
        LoggerManager.write(LoggerLevel.DEBUG, getName(), message);
    }

    @Override
    public void debug(@NotNull final String name, @NotNull final String message) {
        LoggerManager.write(LoggerLevel.DEBUG, name, message);
    }

    @Override
    public void error(@NotNull final Class<?> cs, @NotNull final String message) {
        LoggerManager.write(LoggerLevel.ERROR, cs.getSimpleName(), message);
    }

    @Override
    public void error(@NotNull final Class<?> cs, @NotNull final Throwable exception) {
        LoggerManager.write(LoggerLevel.ERROR, cs.getSimpleName(), StringUtils.toString(exception));
    }

    @Override
    public void error(@NotNull final Object owner, @NotNull final String message) {
        LoggerManager.write(LoggerLevel.ERROR, owner.getClass().getSimpleName(), message);
    }

    @Override
    public void error(@NotNull final Object owner, @NotNull final Throwable exception) {
        LoggerManager.write(LoggerLevel.ERROR, owner.getClass().getSimpleName(), StringUtils.toString(exception));
    }

    @Override
    public void error(@NotNull final String message) {
        LoggerManager.write(LoggerLevel.ERROR, getName(), message);
    }

    @Override
    public void error(@NotNull final String name, @NotNull final String message) {
        LoggerManager.write(LoggerLevel.ERROR, name, message);
    }

    @Override
    public void error(@NotNull final String name, @NotNull final Throwable exception) {
        LoggerManager.write(LoggerLevel.ERROR, name, StringUtils.toString(exception));
    }

    @Override
    public void error(@NotNull final Throwable exception) {
        LoggerManager.write(LoggerLevel.ERROR, getName(), StringUtils.toString(exception));
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull final String name) {
        this.name = name;
    }

    @Override
    public void info(@NotNull final Class<?> cs, @NotNull final String message) {
        LoggerManager.write(LoggerLevel.INFO, cs.getSimpleName(), message);
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
    public void info(@NotNull final String name, @NotNull final String message) {
        LoggerManager.write(LoggerLevel.INFO, name, message);
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
    public void warning(@NotNull final Class<?> cs, @NotNull final String message) {
        LoggerManager.write(LoggerLevel.WARNING, cs.getSimpleName(), message);
    }

    @Override
    public void warning(@NotNull final Class<?> cs, @NotNull final Throwable exception) {
        LoggerManager.write(LoggerLevel.WARNING, cs.getSimpleName(), StringUtils.toString(exception));
    }

    @Override
    public void warning(@NotNull final Object owner, @NotNull final String message) {
        LoggerManager.write(LoggerLevel.WARNING, owner.getClass().getSimpleName(), message);
    }

    @Override
    public void warning(@NotNull final Object owner, @NotNull final Throwable exception) {
        LoggerManager.write(LoggerLevel.WARNING, owner.getClass().getSimpleName(), StringUtils.toString(exception));
    }

    @Override
    public void warning(@NotNull final String message) {
        LoggerManager.write(LoggerLevel.WARNING, getName(), message);
    }

    @Override
    public void warning(@NotNull final String name, @NotNull final String message) {
        LoggerManager.write(LoggerLevel.WARNING, name, message);
    }

    @Override
    public void warning(@NotNull final String name, @NotNull final Throwable exception) {
        LoggerManager.write(LoggerLevel.WARNING, name, StringUtils.toString(exception));
    }

    @Override
    public void warning(@NotNull final Throwable exception) {
        LoggerManager.write(LoggerLevel.WARNING, getName(), StringUtils.toString(exception));
    }
}
