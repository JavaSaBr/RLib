package rlib.logging;

/**
 * Интерфейс для реализации логгера.
 *
 * @author Ronn
 */
public interface Logger {

    /**
     * Вывод отладочного сообщение.
     *
     * @param cs      класс объекта, посылающего сообщение.
     * @param message содержание сообщения.
     */
    public void debug(Class<?> cs, String message);

    /**
     * Вывод отладочного сообщение.
     *
     * @param owner   объект, посылающий сообщение .
     * @param message содержание сообщения.
     */
    public void debug(Object owner, String message);

    /**
     * Вывод отладочного сообщение.
     *
     * @param message содержание сообщения.
     */
    public void debug(String message);

    /**
     * Вывод отладочного сообщение.
     *
     * @param name    имя объекта, посылающего сообщение.
     * @param message содержание сообщения.
     */
    public void debug(String name, String message);

    /**
     * Вывод критического сообщение.
     *
     * @param cs      класс объекта, посылающего сообщение.
     * @param message содержание сообщения.
     */
    public void error(Class<?> cs, String message);

    /**
     * Вывод критического эксепшена.
     *
     * @param cs        класс объекта, посылающего сообщение.
     * @param exception сам эксепшен.
     */
    public void error(Class<?> cs, Throwable exception);

    /**
     * Вывод критического сообщение.
     *
     * @param owner   объект, посылающий сообщение.
     * @param message содержание сообщения.
     */
    public void error(Object owner, String message);

    /**
     * Вывод критического эксепшена.
     *
     * @param owner     объект, посылающий эксепшен.
     * @param exception сам эксепшен.
     */
    public void error(Object owner, Throwable exception);

    /**
     * Вывод критического сообщение.
     *
     * @param message содержание сообщения.
     */
    public void error(String message);

    /**
     * Вывод критического сообщение.
     *
     * @param name    имя объекта, посылающего сообщение.
     * @param message содержание сообщения.
     */
    public void error(String name, String message);

    /**
     * Вывод критического эксепшена.
     *
     * @param name      имя объекта, посылающего сообщение.
     * @param exception сам эксепшен.
     */
    public void error(String name, Throwable exception);

    /**
     * Вывод критического эксепшена.
     *
     * @param exception сам эксепшен.
     */
    public void error(Throwable exception);

    /**
     * @return имя логера.
     */
    public String getName();

    /**
     * @param name имя логера.
     */
    public void setName(String name);

    /**
     * Вывод информативного сообщение.
     *
     * @param cs      класс объекта, посылающего сообщение.
     * @param message содержание сообщения.
     */
    public void info(Class<?> cs, String message);

    /**
     * Вывод информативного сообщение.
     *
     * @param owner   объект, посылающий сообщение .
     * @param message содержание сообщения.
     */
    public void info(Object owner, String message);

    /**
     * Вывод информативного сообщение.
     *
     * @param message содержание сообщения.
     */
    public void info(String message);

    /**
     * Вывод информативного сообщение.
     *
     * @param name    имя объекта, посылающего сообщение.
     * @param message содержание сообщения.
     */
    public void info(String name, String message);

    /**
     * @return активно ли отображения отладочных сообщений.
     */
    public boolean isEnabledDebug();

    /**
     * @return активно ли отображения критических сообщений.
     */
    public boolean isEnabledError();

    /**
     * @return активно ли отображения информационных сообщений.
     */
    public boolean isEnabledInfo();

    /**
     * @return активно ли отображения важных сообщений.
     */
    public boolean isEnabledWarning();

    /**
     * Вывод важного сообщение.
     *
     * @param cs      класс объекта, посылающего сообщение.
     * @param message содержание сообщения.
     */
    public void warning(Class<?> cs, String message);

    /**
     * Вывод важного эксепшена.
     *
     * @param cs        класс объекта, посылающего сообщение.
     * @param exception сам эксепшен.
     */
    public void warning(Class<?> cs, Throwable exception);

    /**
     * Вывод важного сообщение.
     *
     * @param owner   объект, посылающий сообщение.
     * @param message содержание сообщения.
     */
    public void warning(Object owner, String message);

    /**
     * Вывод важного эксепшена.
     *
     * @param owner     объект, посылающий эксепшен.
     * @param exception сам эксепшен.
     */
    public void warning(Object owner, Throwable exception);

    /**
     * Вывод важного сообщение.
     *
     * @param message содержание сообщения.
     */
    public void warning(String message);

    /**
     * Вывод важного сообщение.
     *
     * @param name    имя объекта, посылающего сообщение.
     * @param message содержание сообщения.
     */
    public void warning(String name, String message);

    /**
     * Вывод важного эксепшена.
     *
     * @param name      имя объекта, посылающего сообщение.
     * @param exception сам эксепшен.
     */
    public void warning(String name, Throwable exception);

    /**
     * Вывод важного эксепшена.
     *
     * @param exception сам эксепшен.
     */
    public void warning(Throwable exception);
}
