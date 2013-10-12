package rlib.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

import rlib.concurrent.impl.ExtThreadImpl;
import rlib.concurrent.impl.ExtThreadPoolExecutorImpl;
import rlib.concurrent.impl.ExtWorkerFactoryImpl;
import rlib.concurrent.interfaces.CallableTask;
import rlib.concurrent.interfaces.ExtExecutorService;
import rlib.concurrent.interfaces.ExtThread;
import rlib.concurrent.interfaces.ExtThreadExceptionHandler;
import rlib.concurrent.interfaces.ExtThreadFactory;
import rlib.concurrent.interfaces.Task;
import rlib.concurrent.interfaces.WorkerFactory;
import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.util.Copyable;
import rlib.util.linkedlist.LinkedList;
import rlib.util.linkedlist.LinkedLists;

/**
 * @author Ronn
 */
public final class ExtExecutors {

	private static class Cop implements Copyable<Cop> {

		@Override
		public Cop copy() {
			return this;
		}

	}

	private static final Logger log = Loggers.getLogger(ExtExecutors.class);

	protected static final ExtThreadExceptionHandler THREAD_EXCEPTION_HANDLER = new ExtThreadExceptionHandler() {

		@Override
		public void handle(final ExtThread<?> thread, final Exception exception) {
			log.warning(getClass(), exception);
		}
	};

	public static <L> ExtExecutorService<L> createExecutorService(final ExtThreadFactory<L> threadFactory, final WorkerFactory<L> workerFactory, final int timeout, final int poolSize) {
		final LinkedList<Task<L>> waitTasks = LinkedLists.newLinkedList(Task.class);
		return new ExtThreadPoolExecutorImpl<L>(threadFactory, workerFactory, THREAD_EXCEPTION_HANDLER, waitTasks, Locks.newLock(), poolSize);
	}

	public static <L> ExtExecutorService<L> createExecutorService(final ExtThreadFactory<L> threadFactory, final WorkerFactory<L> workerFactory, final ExtThreadExceptionHandler handler,
			final LinkedList<Task<L>> waitTasks, final Lock sync, final int timeout, final int poolSize) {
		return new ExtThreadPoolExecutorImpl<>(threadFactory, workerFactory, handler, waitTasks, sync, poolSize);
	}

	public static <L> ExtThread<L> createThread(final ThreadGroup group, final Task<L> task, final L localObjects, final String name) {
		return new ExtThreadImpl<L>(group, task, localObjects, name);
	}

	public static <L> WorkerFactory<L> createWorkerFactory() {
		return new ExtWorkerFactoryImpl<L>();
	}

	public static <L extends Copyable<L>> ExtThreadFactory<L> ctreateThreadFactory(final L localObjects, final String name, final int priority) {
		return new ExtGroupThreadFactory<L>(localObjects, name, priority);
	}

	public static void main(final String[] args) {
		final Cop c = new Cop();

		final ExtThreadFactory<Cop> threadFactory = ctreateThreadFactory(c, "test", Thread.NORM_PRIORITY);
		final WorkerFactory<Cop> workerFactory = createWorkerFactory();
		final ExtExecutorService<Cop> executor = createExecutorService(threadFactory, workerFactory, 1, 3);

		CallableTask<Cop, String> callable = new CallableTask<ExtExecutors.Cop, String>() {

			@Override
			public String call(Cop localObjects) {
				try {
					Thread.sleep(5000);
				} catch(InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("call - " + this);
				return "12345";
			}
		};

		while(true) {
			System.out.println("add task");
			Future<String> future = executor.submit(callable);
			try {
				System.out.println("result " + future.get());
			} catch(InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static <L, V> CallableTask<L, V> runnableToCallable(final Task<L> runnable, final V result) {
		return new CallableTask<L, V>() {

			@Override
			public V call(final L localObjects) {
				runnable.run(localObjects);
				return result;
			}
		};

	}

	private ExtExecutors() {
		throw new RuntimeException();
	}
}
