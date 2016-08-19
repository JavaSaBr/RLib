package rlib.logging;

/**
 * Слушатель логгера.
 *
 * @author JavaSaBr
 */
public interface LoggerListener {

    /**
     * @param text текст.
     */
    public void println(String text);
}
