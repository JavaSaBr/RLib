package rlib.concurrent.interfaces;

import java.util.concurrent.Future;

/**
 * @author Ronn
 */
public interface LRunnableFuture<L, V> extends LRunnable<L>, Future<V>
{
}
