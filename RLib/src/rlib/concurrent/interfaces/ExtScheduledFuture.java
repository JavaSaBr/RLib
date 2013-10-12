package rlib.concurrent.interfaces;

import java.util.concurrent.ScheduledFuture;

/**
 * Интерфейс для реализации управления отложненными задачами.
 * 
 * @author Ronn
 */
public interface ExtScheduledFuture<L, V> extends ExtFutureTask<L, V>, ScheduledFuture<V> {
}
