package rlib.concurrent;

import java.util.concurrent.locks.Lock;

import rlib.concurrent.impl.LThreadImpl;
import rlib.concurrent.impl.LThreadPoolExecutorImpl;
import rlib.concurrent.impl.LWorkerFactoryImpl;
import rlib.concurrent.interfaces.LCallable;
import rlib.concurrent.interfaces.LExecutorService;
import rlib.concurrent.interfaces.LRunnable;
import rlib.concurrent.interfaces.LThread;
import rlib.concurrent.interfaces.LThreadExceptionHandler;
import rlib.concurrent.interfaces.LThreadFactory;
import rlib.concurrent.interfaces.LWorkerFactory;
import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.util.Copyable;
import rlib.util.linkedlist.LinkedList;
import rlib.util.linkedlist.LinkedLists;

/**
 * @author Ronn
 */
public final class LExecutors
{
	private static final Logger log = Loggers.getLogger(LExecutors.class);
	
	protected static final LThreadExceptionHandler THREAD_EXCEPTION_HANDLER = new LThreadExceptionHandler()
	{
		@Override
		public void handle(LThread<?> thread, Exception exception)
		{
			log.warning(getClass(), exception);
		}
	};

	private LExecutors()
	{
		throw new RuntimeException();
	}
	
	public static <L> LThread<L> createThread(ThreadGroup group, LRunnable<L> task, L localObjects, String name)
	{
		return new LThreadImpl<L>(group, task, localObjects, name);
	}
	
	public static <L extends Copyable<L>> LThreadFactory<L> ctreateThreadFactory(L localObjects, String name, int priority)
	{
		return new LGroupThreadFactory<L>(localObjects, name, priority);
	}
	
	public static <L> LExecutorService<L> createExecutorService(LThreadFactory<L> threadFactory, LWorkerFactory<L> workerFactory, LThreadExceptionHandler handler,
			LinkedList<LRunnable<L>> waitTasks, Lock sync, int timeout, int poolSize)
	{
		return new LThreadPoolExecutorImpl<>(threadFactory, workerFactory, handler, waitTasks, sync, timeout, poolSize);
	}
	
	public static <L> LExecutorService<L> createExecutorService(LThreadFactory<L> threadFactory, LWorkerFactory<L> workerFactory, int timeout, int poolSize)
	{
		LinkedList<LRunnable<L>> waitTasks = LinkedLists.newLinkedList(LRunnable.class);
		
		return new LThreadPoolExecutorImpl<L>(threadFactory, workerFactory, THREAD_EXCEPTION_HANDLER, waitTasks, Locks.newLock(), timeout, poolSize);
	}
	
	public static <L> LWorkerFactory<L> createWorkerFactory()
	{
		return new LWorkerFactoryImpl<L>();
	}
	
	public static <L, V> LCallable<L, V> runnableToCallable(final LRunnable<L> runnable, final V result)
	{
		return new LCallable<L, V>()
		{
			@Override
			public V call(L localObjects)
			{
				runnable.run(localObjects);
				return result;
			}
		};
		
	}
	
	public static void main(String[] args)
	{
		Cop c = new Cop();
		
		LThreadFactory<Cop> threadFactory = ctreateThreadFactory(c, "test", Thread.NORM_PRIORITY);
		
		LWorkerFactory<Cop> workerFactory = createWorkerFactory();
		
		LExecutorService<Cop> executor = createExecutorService(threadFactory, workerFactory, 1, 3);
		
		LRunnable<Cop> task = new LRunnable<Cop>(){

			@Override
			public void run(Cop localObjects)
			{
				//System.out.println("execute - " + this);
			}
		};
		
		while(true)
		{
			executor.execute(task);
			try
			{
				Thread.sleep(1);
			}
			catch(InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private static class Cop implements Copyable<Cop>
	{

		@Override
		public Cop copy()
		{
			return this;
		}
		
	}
}
