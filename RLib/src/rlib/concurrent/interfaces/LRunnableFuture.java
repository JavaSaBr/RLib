package rlib.concurrent.interfaces;

import java.util.concurrent.Future;

import rlib.util.pools.Foldable;

/**
 * @author Ronn
 */
public interface LRunnableFuture<L, V> extends LRunnable<L>, Future<V>, Foldable
{
}
