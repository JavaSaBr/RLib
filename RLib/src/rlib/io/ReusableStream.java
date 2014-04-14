package rlib.io;

/**
 * Интерфейсд ля реализации переиспользуемых стримов.
 * 
 * @author Ronn
 */
public interface ReusableStream {

	/**
	 * Перезапустить стрим.
	 */
	public void reset();
}
