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
	public void debug(Class<?> cs, String message) {
		LoggerManager.write(LoggerLevel.DEBUG, cs.getSimpleName(), message);
	}

	@Override
	public void debug(Object owner, String message) {
		LoggerManager.write(LoggerLevel.DEBUG, owner.getClass().getSimpleName(), message);
	}

	@Override
	public void debug(String message) {
		LoggerManager.write(LoggerLevel.DEBUG, getName(), message);
	}

	@Override
	public void debug(String name, String message) {
		LoggerManager.write(LoggerLevel.DEBUG, name, message);
	}

	@Override
	public void error(Class<?> cs, String message) {
		LoggerManager.write(LoggerLevel.ERROR, cs.getSimpleName(), message);
	}

	@Override
	public void error(Class<?> cs, Throwable exception) {
		LoggerManager.write(LoggerLevel.ERROR, cs.getSimpleName(), Util.toString(exception));
	}

	@Override
	public void error(Object owner, String message) {
		LoggerManager.write(LoggerLevel.ERROR, owner.getClass().getSimpleName(), message);
	}

	@Override
	public void error(Object owner, Throwable exception) {
		LoggerManager.write(LoggerLevel.ERROR, owner.getClass().getSimpleName(), Util.toString(exception));
	}

	@Override
	public void error(String message) {
		LoggerManager.write(LoggerLevel.ERROR, getName(), message);
	}

	@Override
	public void error(String name, String message) {
		LoggerManager.write(LoggerLevel.ERROR, name, message);
	}

	@Override
	public void error(String name, Throwable exception) {
		LoggerManager.write(LoggerLevel.ERROR, name, Util.toString(exception));
	}

	@Override
	public void error(Throwable exception) {
		LoggerManager.write(LoggerLevel.ERROR, getName(), Util.toString(exception));
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void info(Class<?> cs, String message) {
		LoggerManager.write(LoggerLevel.INFO, cs.getSimpleName(), message);
	}

	@Override
	public void info(Object owner, String message) {
		LoggerManager.write(LoggerLevel.INFO, owner.getClass().getSimpleName(), message);
	}

	@Override
	public void info(String message) {
		LoggerManager.write(LoggerLevel.INFO, getName(), message);
	}

	@Override
	public void info(String name, String message) {
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
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void warning(Class<?> cs, String message) {
		LoggerManager.write(LoggerLevel.WARNING, cs.getSimpleName(), message);
	}

	@Override
	public void warning(Class<?> cs, Throwable exception) {
		LoggerManager.write(LoggerLevel.WARNING, cs.getSimpleName(), Util.toString(exception));
	}

	@Override
	public void warning(Object owner, String message) {
		LoggerManager.write(LoggerLevel.WARNING, owner.getClass().getSimpleName(), message);
	}

	@Override
	public void warning(Object owner, Throwable exception) {
		LoggerManager.write(LoggerLevel.WARNING, owner.getClass().getSimpleName(), Util.toString(exception));
	}

	@Override
	public void warning(String message) {
		LoggerManager.write(LoggerLevel.WARNING, getName(), message);
	}

	@Override
	public void warning(String name, String message) {
		LoggerManager.write(LoggerLevel.WARNING, name, message);
	}

	@Override
	public void warning(String name, Throwable exception) {
		LoggerManager.write(LoggerLevel.WARNING, name, Util.toString(exception));
	}

	@Override
	public void warning(Throwable exception) {
		LoggerManager.write(LoggerLevel.WARNING, getName(), Util.toString(exception));
	}
}
