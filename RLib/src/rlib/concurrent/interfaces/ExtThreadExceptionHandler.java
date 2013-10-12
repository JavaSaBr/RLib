package rlib.concurrent.interfaces;

/**
 * @author Ronn
 */
public interface ExtThreadExceptionHandler {

	/**
	 * Обработка эксепшена в потоке.
	 * 
	 * @param thread поток, в котором произошел эксепшен.
	 * @param exception сам эксепшен.
	 */
	public void handle(ExtThread<?> thread, Exception exception);
}
