package rlib.logging;

/**
 * Слушатель логгера.
 * 
 * @author Ronn
 */
public interface LoggerListener {

	/**
	 * @param text текст.
	 */
	public void println(String text);
}
