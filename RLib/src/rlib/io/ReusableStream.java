package rlib.io;

import java.io.IOException;

/**
 * Интерфейсд ля реализации переиспользуемых стримов.
 * 
 * @author Ronn
 */
public interface ReusableStream {

	/**
	 * Перезапустить стрим.
	 */
	public void reset() throws IOException;

	/**
	 * Инициализация потока под указанный буфер.
	 *
	 * @param buffer буффер данных.
	 * @param offset отступ.
	 * @param length длинна.
	 */
	public void initFor(byte[] buffer, int offset, int length);
}
