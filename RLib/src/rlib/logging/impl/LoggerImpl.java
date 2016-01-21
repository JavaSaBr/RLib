package rlib.logging.impl;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.Util;

import static rlib.logging.LoggerLevel.*;

/**
 * Реализация логгера консольных сообщений. Работает через LoggerManager.
 *
 * @author Ronn
 */
public final class LoggerImpl implements Logger {

    /**
     * От чьего имени будет выводится сообщение.
     */
    private String name;

    public LoggerImpl() {
        this.name = "<empty name>";
    }

    @Override
    public void debug(final Class<?> cs, final String message) {
        LoggerManager.write(DEBUG, cs.getSimpleName(), message);
    }

    @Override
    public void debug(final Object owner, final String message) {
        LoggerManager.write(DEBUG, owner.getClass().getSimpleName(), message);
    }

    @Override
    public void debug(final String message) {
        LoggerManager.write(DEBUG, getName(), message);
    }

    @Override
    public void debug(final String name, final String message) {
        LoggerManager.write(DEBUG, name, message);
    }

    @Override
    public void error(final Class<?> cs, final String message) {
        LoggerManager.write(ERROR, cs.getSimpleName(), message);
    }

    @Override
    public void error(final Class<?> cs, final Throwable exception) {
        LoggerManager.write(ERROR, cs.getSimpleName(), Util.toString(exception));
    }

    @Override
    public void error(final Object owner, final String message) {
        LoggerManager.write(ERROR, owner.getClass().getSimpleName(), message);
    }

    @Override
    public void error(final Object owner, final Throwable exception) {
        LoggerManager.write(ERROR, owner.getClass().getSimpleName(), Util.toString(exception));
    }

    @Override
    public void error(final String message) {
        LoggerManager.write(ERROR, getName(), message);
    }

    @Override
    public void error(final String name, final String message) {
        LoggerManager.write(ERROR, name, message);
    }

    @Override
    public void error(final String name, final Throwable exception) {
        LoggerManager.write(ERROR, name, Util.toString(exception));
    }

    @Override
    public void error(final Throwable exception) {
        LoggerManager.write(ERROR, getName(), Util.toString(exception));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public void info(final Class<?> cs, final String message) {
        LoggerManager.write(INFO, cs.getSimpleName(), message);
    }

    @Override
    public void info(final Object owner, final String message) {
        LoggerManager.write(INFO, owner.getClass().getSimpleName(), message);
    }

    @Override
    public void info(final String message) {
        LoggerManager.write(INFO, getName(), message);
    }

    @Override
    public void info(final String name, final String message) {
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
    public void warning(final Class<?> cs, final String message) {
        LoggerManager.write(WARNING, cs.getSimpleName(), message);
    }

    @Override
    public void warning(final Class<?> cs, final Throwable exception) {
        LoggerManager.write(WARNING, cs.getSimpleName(), Util.toString(exception));
    }

    @Override
    public void warning(final Object owner, final String message) {
        LoggerManager.write(WARNING, owner.getClass().getSimpleName(), message);
    }

    @Override
    public void warning(final Object owner, final Throwable exception) {
        LoggerManager.write(WARNING, owner.getClass().getSimpleName(), Util.toString(exception));
    }

    @Override
    public void warning(final String message) {
        LoggerManager.write(WARNING, getName(), message);
    }

    @Override
    public void warning(final String name, final String message) {
        LoggerManager.write(WARNING, name, message);
    }

    @Override
    public void warning(final String name, final Throwable exception) {
        LoggerManager.write(WARNING, name, Util.toString(exception));
    }

    @Override
    public void warning(final Throwable exception) {
        LoggerManager.write(WARNING, getName(), Util.toString(exception));
    }
}
