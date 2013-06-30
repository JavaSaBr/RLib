package rlib.concurrent.interfaces;

/**
 * @author Ronn
 */
public interface LThreadExceptionHandler
{
	/**
	 * Обработка эксепшена в потоке.
	 * 
	 * @param thread поток, в котором произошел эксепшен.
	 * @param exception сам эксепшен.
	 */
	public void handle(LThread<?> thread, Exception exception);
}
