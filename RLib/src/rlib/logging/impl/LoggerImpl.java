package rlib.logging.impl;

import rlib.logging.Logger;
import rlib.logging.LoggerLevel;
import rlib.logging.LoggerManager;
import rlib.util.Util;

/**
 * Модель логгера консольных сообщений. Работает через LoggerManager.
 * 
 * @author Ronn
 */
public final class LoggerImpl implements Logger {

	/** от чьего имени будет выводится сообщение */
	private String name;

	public LoggerImpl() {
		this.name = "<empty name>";
	}

	@Override
	public void debug(final Class<?> cs, final String message) {
		LoggerManager.write(LoggerLevel.DEBUG, cs.getSimpleName(), message);
	}

	@Override
	public void debug(final Object owner, final String message) {
		LoggerManager.write(LoggerLevel.DEBUG, owner.getClass().getSimpleName(), message);
	}

	@Override
	public void debug(final String message) {
		LoggerManager.write(LoggerLevel.DEBUG, getName(), message);
	}

	@Override
	public void debug(final String name, final String message) {
		LoggerManager.write(LoggerLevel.DEBUG, name, message);
	}

	@Override
	public void error(final Class<?> cs, final String message) {
		LoggerManager.write(LoggerLevel.ERROR, cs.getSimpleName(), message);
	}

	@Override
	public void error(final Class<?> cs, final Throwable exception) {
		LoggerManager.write(LoggerLevel.ERROR, cs.getSimpleName(), Util.toString(exception));
	}

	@Override
	public void error(final Object owner, final String message) {
		LoggerManager.write(LoggerLevel.ERROR, owner.getClass().getSimpleName(), message);
	}

	@Override
	public void error(final Object owner, final Throwable exception) {
		LoggerManager.write(LoggerLevel.ERROR, owner.getClass().getSimpleName(), Util.toString(exception));
	}

	@Override
	public void error(final String message) {
		LoggerManager.write(LoggerLevel.ERROR, getName(), message);
	}

	@Override
	public void error(final String name, final String message) {
		LoggerManager.write(LoggerLevel.ERROR, name, message);
	}

	@Override
	public void error(final String name, final Throwable exception) {
		LoggerManager.write(LoggerLevel.ERROR, name, Util.toString(exception));
	}

	@Override
	public void error(final Throwable exception) {
		LoggerManager.write(LoggerLevel.ERROR, getName(), Util.toString(exception));
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void info(final Class<?> cs, final String message) {
		LoggerManager.write(LoggerLevel.INFO, cs.getSimpleName(), message);
	}

	@Override
	public void info(final Object owner, final String message) {
		LoggerManager.write(LoggerLevel.INFO, owner.getClass().getSimpleName(), message);
	}

	@Override
	public void info(final String message) {
		LoggerManager.write(LoggerLevel.INFO, getName(), message);
	}

	@Override
	public void info(final String name, final String message) {
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
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public void warning(final Class<?> cs, final String message) {
		LoggerManager.write(LoggerLevel.WARNING, cs.getSimpleName(), message);
	}

	@Override
	public void warning(final Class<?> cs, final Throwable exception) {
		LoggerManager.write(LoggerLevel.WARNING, cs.getSimpleName(), Util.toString(exception));
	}

	@Override
	public void warning(final Object owner, final String message) {
		LoggerManager.write(LoggerLevel.WARNING, owner.getClass().getSimpleName(), message);
	}

	@Override
	public void warning(final Object owner, final Throwable exception) {
		LoggerManager.write(LoggerLevel.WARNING, owner.getClass().getSimpleName(), Util.toString(exception));
	}

	@Override
	public void warning(final String message) {
		LoggerManager.write(LoggerLevel.WARNING, getName(), message);
	}

	@Override
	public void warning(final String name, final String message) {
		LoggerManager.write(LoggerLevel.WARNING, name, message);
	}

	@Override
	public void warning(final String name, final Throwable exception) {
		LoggerManager.write(LoggerLevel.WARNING, name, Util.toString(exception));
	}

	@Override
	public void warning(final Throwable exception) {
		LoggerManager.write(LoggerLevel.WARNING, getName(), Util.toString(exception));
	}
}
