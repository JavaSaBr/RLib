package rlib.concurrent.interfaces;

import java.util.concurrent.Future;

/**
 * Интерфейс для реализации задачи с возможностью ожидания результата.
 * 
 * @author Ronn
 */
public interface ExtFutureTask<L, V> extends Task<L>, Future<V> {

	/**
	 * уведомление о завершении работы.
	 */
	public void done();
}
