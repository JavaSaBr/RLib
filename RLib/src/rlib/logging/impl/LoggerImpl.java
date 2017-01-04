package rlib.logging.impl;

import static rlib.logging.LoggerLevel.DEBUG;
import static rlib.logging.LoggerLevel.ERROR;
import static rlib.logging.LoggerLevel.INFO;
import static rlib.logging.LoggerLevel.WARNING;

import org.jetbrains.annotations.NotNull;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.StringUtils;

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

    public LoggerImpl() {
        this.name = "<empty name>";
    }

    @Override
    public void debug(@NotNull final Class<?> cs, @NotNull final String message) {
        LoggerManager.write(DEBUG, cs.getSimpleName(), message);
    }

    @Override
    public void debug(@NotNull final Object owner, @NotNull final String message) {
        LoggerManager.write(DEBUG, owner.getClass().getSimpleName(), message);
    }

    @Override
    public void debug(@NotNull final String message) {
        LoggerManager.write(DEBUG, getName(), message);
    }

    @Override
    public void debug(@NotNull final String name, @NotNull final String message) {
        LoggerManager.write(DEBUG, name, message);
    }

    @Override
    public void error(@NotNull final Class<?> cs, @NotNull final String message) {
        LoggerManager.write(ERROR, cs.getSimpleName(), message);
    }

    @Override
    public void error(@NotNull final Class<?> cs, @NotNull final Throwable exception) {
        LoggerManager.write(ERROR, cs.getSimpleName(), StringUtils.toString(exception));
    }

    @Override
    public void error(@NotNull final Object owner, @NotNull final String message) {
        LoggerManager.write(ERROR, owner.getClass().getSimpleName(), message);
    }

    @Override
    public void error(@NotNull final Object owner, @NotNull final Throwable exception) {
        LoggerManager.write(ERROR, owner.getClass().getSimpleName(), StringUtils.toString(exception));
    }

    @Override
    public void error(@NotNull final String message) {
        LoggerManager.write(ERROR, getName(), message);
    }

    @Override
    public void error(@NotNull final String name, @NotNull final String message) {
        LoggerManager.write(ERROR, name, message);
    }

    @Override
    public void error(@NotNull final String name, @NotNull final Throwable exception) {
        LoggerManager.write(ERROR, name, StringUtils.toString(exception));
    }

    @Override
    public void error(@NotNull final Throwable exception) {
        LoggerManager.write(ERROR, getName(), StringUtils.toString(exception));
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
        LoggerManager.write(INFO, cs.getSimpleName(), message);
    }

    @Override
    public void info(@NotNull final Object owner, @NotNull final String message) {
        LoggerManager.write(INFO, owner.getClass().getSimpleName(), message);
    }

    @Override
    public void info(@NotNull final String message) {
        LoggerManager.write(INFO, getName(), message);
    }

    @Override
    public void info(@NotNull final String name, @NotNull final String message) {
        LoggerManager.write(INFO, name, message);
    }

    @Override
    public boolean isEnabledDebug() {
        return DEBUG.isEnabled();
    }

    @Override
    public boolean isEnabledError() {
        return ERROR.isEnabled();
    }

    @Override
    public boolean isEnabledInfo() {
        return INFO.isEnabled();
    }

    @Override
    public boolean isEnabledWarning() {
        return WARNING.isEnabled();
    }

    @Override
    public void warning(@NotNull final Class<?> cs, @NotNull final String message) {
        LoggerManager.write(WARNING, cs.getSimpleName(), message);
    }

    @Override
    public void warning(@NotNull final Class<?> cs, @NotNull final Throwable exception) {
        LoggerManager.write(WARNING, cs.getSimpleName(), StringUtils.toString(exception));
    }

    @Override
    public void warning(@NotNull final Object owner, @NotNull final String message) {
        LoggerManager.write(WARNING, owner.getClass().getSimpleName(), message);
    }

    @Override
    public void warning(@NotNull final Object owner, @NotNull final Throwable exception) {
        LoggerManager.write(WARNING, owner.getClass().getSimpleName(), StringUtils.toString(exception));
    }

    @Override
    public void warning(@NotNull final String message) {
        LoggerManager.write(WARNING, getName(), message);
    }

    @Override
    public void warning(@NotNull final String name, @NotNull final String message) {
        LoggerManager.write(WARNING, name, message);
    }

    @Override
    public void warning(@NotNull final String name, @NotNull final Throwable exception) {
        LoggerManager.write(WARNING, name, StringUtils.toString(exception));
    }

    @Override
    public void warning(@NotNull final Throwable exception) {
        LoggerManager.write(WARNING, getName(), StringUtils.toString(exception));
    }
}
