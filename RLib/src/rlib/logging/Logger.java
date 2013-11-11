package rlib.logging;

import rlib.util.Util;

/**
 * Модель логгера консольных сообщений. Работает через LoggerManager.
 * 
 * @author Ronn
 */
public final class Logger {

	/** от чьего имени будет выводится сообщение */
	private String name;

	public Logger() {
		super();
	}

	/**
	 * @param name от чьего имени будет выводится сообщение.
	 */
	public Logger(String name) {
		super();
		this.name = name;
	}

	/**
	 * Вывод информативного сообщение.
	 * 
	 * @param class класс объекта, посылающего сообщение.
	 * @param message содержание сообщения.
	 */
	public void info(Class<?> cs, String message) {
		Loggers.println("INFO " + Util.formatTime(System.currentTimeMillis()) + " " + cs.getSimpleName() + ": " + message);
	}

	/**
	 * Вывод информативного сообщение.
	 * 
	 * @param owner объект, посылающий сообщение .
	 * @param message содержание сообщения.
	 */
	public void info(Object owner, String message) {
		Loggers.println("INFO " + Util.formatTime(System.currentTimeMillis()) + " " + owner.getClass().getSimpleName() + ": " + message);
	}

	/**
	 * Вывод информативного сообщение.
	 * 
	 * @param message содержание сообщения.
	 */
	public void info(String message) {
		Loggers.println("INFO " + Util.formatTime(System.currentTimeMillis()) + " " + name + ": " + message);
	}

	/**
	 * Вывод информативного сообщение.
	 * 
	 * @param name имя объекта, посылающего сообщение.
	 * @param message содержание сообщения.
	 */
	public void info(String name, String message) {
		Loggers.println("INFO " + Util.formatTime(System.currentTimeMillis()) + " " + name + ": " + message);
	}

	/**
	 * Вывод важного эксепшена.
	 * 
	 * @param class класс объекта, посылающего сообщение.
	 * @param exception сам эксепшен.
	 */
	public void warning(Class<?> cs, Throwable exception) {

		StringBuilder builder = new StringBuilder(exception.getClass().getSimpleName() + " : " + exception.getMessage());

		builder.append(" : stack trace:\n");

		for(StackTraceElement stack : exception.getStackTrace()) {
			builder.append(stack).append("\n");
		}

		Loggers.println("WARNING " + Util.formatTime(System.currentTimeMillis()) + " " + cs.getSimpleName() + ": " + builder);
	}

	/**
	 * Вывод важного сообщение.
	 * 
	 * @param class класс объекта, посылающего сообщение.
	 * @param message содержание сообщения.
	 */
	public void warning(Class<?> cs, String message) {
		Loggers.println(" INFO " + Util.formatTime(System.currentTimeMillis()) + " " + cs.getSimpleName() + ": " + message);
	}

	/**
	 * Вывод важного эксепшена.
	 * 
	 * @param exception сам эксепшен.
	 */
	public void warning(Throwable exception) {

		StringBuilder builder = new StringBuilder(exception.getClass().getSimpleName() + " : " + exception.getMessage());

		builder.append(" : stack trace:\n");

		for(StackTraceElement stack : exception.getStackTrace()) {
			builder.append(stack).append("\n");
		}

		Loggers.println("WARNING " + Util.formatTime(System.currentTimeMillis()) + " " + name + ": " + builder);
	}

	/**
	 * Вывод важного эксепшена.
	 * 
	 * @param owner объект, посылающий эксепшен.
	 * @param exception сам эксепшен.
	 */
	public void warning(Object owner, Throwable exception) {

		StringBuilder builder = new StringBuilder(exception.getClass().getSimpleName() + " : " + exception.getMessage());

		builder.append(" : stack trace:\n");

		for(StackTraceElement stack : exception.getStackTrace()) {
			builder.append(stack).append("\n");
		}

		Loggers.println("WARNING " + Util.formatTime(System.currentTimeMillis()) + " " + owner.getClass().getSimpleName() + ": " + builder);
	}

	/**
	 * Вывод важного сообщение.
	 * 
	 * @param owner объект, посылающий сообщение.
	 * @param message содержание сообщения.
	 */
	public void warning(Object owner, String message) {
		Loggers.println("WARNING " + Util.formatTime(System.currentTimeMillis()) + " " + owner.getClass().getSimpleName() + ": " + message);
	}

	/**
	 * Вывод важного сообщение.
	 * 
	 * @param message содержание сообщения.
	 */
	public void warning(String message) {
		Loggers.println("WARNING " + Util.formatTime(System.currentTimeMillis()) + " " + name + ": " + message);
	}

	/**
	 * Вывод важного эксепшена.
	 * 
	 * @param name имя объекта, посылающего сообщение.
	 * @param exception сам эксепшен.
	 */
	public void warning(String name, Throwable exception) {

		StringBuilder builder = new StringBuilder(exception.getClass().getSimpleName() + " : " + exception.getMessage());

		builder.append(" : stack trace:\n");

		for(StackTraceElement stack : exception.getStackTrace()) {
			builder.append(stack).append("\n");
		}

		Loggers.println("WARNING " + Util.formatTime(System.currentTimeMillis()) + " " + name + ": " + builder);
	}

	/**
	 * Вывод важного сообщение.
	 * 
	 * @param name имя объекта, посылающего сообщение.
	 * @param message содержание сообщения.
	 */
	public void warning(String name, String message) {
		Loggers.println("WARNING " + Util.formatTime(System.currentTimeMillis()) + " " + name + ": " + message);
	}
}
