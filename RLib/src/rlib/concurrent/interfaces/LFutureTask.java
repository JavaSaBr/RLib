package rlib.concurrent.interfaces;

/**
 * @author Ronn
 */
public interface LFutureTask<L, V> extends LRunnableFuture<L, V>
{
	public void done();

	public void setException(Throwable ex);
	
	public void setResult(V result);
}
