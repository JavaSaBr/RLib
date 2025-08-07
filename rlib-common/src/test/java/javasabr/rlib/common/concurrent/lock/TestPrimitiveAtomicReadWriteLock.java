package javasabr.rlib.common.concurrent.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javasabr.rlib.common.concurrent.lock.impl.AtomicReadWriteLock;
import javasabr.rlib.common.concurrent.lock.impl.ReentrantARSWLock;
import javasabr.rlib.common.concurrent.util.ThreadUtils;

/**
 * Тест функционала блокировщика.
 *
 * @author JavaSaBr
 */
public class TestPrimitiveAtomicReadWriteLock {

  public void test() throws InterruptedException, ExecutionException {

    final String header = TestPrimitiveAtomicReadWriteLock.class.getSimpleName() + ": ";

    System.out.println(header + "start test...");

    final AsyncReadSyncWriteLock atomicLock = new AtomicReadWriteLock();
    final AsyncReadSyncWriteLock reentrantLock = new ReentrantARSWLock();

    final ExecutorService service = Executors.newFixedThreadPool(10);

    long atomicTime = testImpl(service, atomicLock);
    System.out.println(header + "atomic lock execute " + atomicTime);
    atomicTime = testImpl(service, atomicLock);
    System.out.println(header + "atomic lock execute " + atomicTime);
    long reentrantTime = testImpl(service, reentrantLock);
    System.out.println(header + "reentrant lock execute " + reentrantTime);
    reentrantTime = testImpl(service, reentrantLock);
    System.out.println(header + "reentrant lock execute " + reentrantTime);

    atomicTime = testImpl(service, atomicLock);
    System.out.println(header + "atomic lock execute " + atomicTime);
    reentrantTime = testImpl(service, reentrantLock);
    System.out.println(header + "reentrant lock execute " + reentrantTime);
  }

  @SuppressWarnings("unused")
  private long testImpl(final ExecutorService service, final AsyncReadSyncWriteLock lock)
      throws InterruptedException, ExecutionException {

    System.gc();

    final List<Integer> list = new ArrayList<>();

    final Runnable writeTask = () -> {

      final Random random = new Random();

      for (int i = 0, length = 15; i < length; i++) {

        lock.syncLock();
        try {

          for (int g = 0; g < 5; g++) {
            list.add(random.nextInt(120));
          }

        } finally {
          lock.syncUnlock();
        }

        ThreadUtils.sleep(10);

        lock.syncLock();
        try {

          for (int g = 0; g < 5; g++) {
            list.add(random.nextInt(120));
          }

        } finally {
          lock.syncUnlock();
        }

        ThreadUtils.sleep(10);
      }
    };

    final Runnable readTask = () -> {

      for (int i = 0, length = 500; i < length; i++) {

        lock.asyncLock();
        try {

          int sum = 0;

          for (final int val1 : list) {
            sum += val1;
          }

        } finally {
          lock.asyncUnlock();
        }

        ThreadUtils.sleep(1);

        lock.asyncLock();
        try {

          int sum = 0;

          for (final int val1 : list) {
            sum += val1;
          }

        } finally {
          lock.asyncUnlock();
        }

        ThreadUtils.sleep(1);
      }
    };

    final long time = System.currentTimeMillis();

    final List<Future<?>> futures = new ArrayList<>();

    for (int i = 0, length = 3; i < length; i++) {
      futures.add(service.submit(writeTask));
    }

    for (int i = 0, length = 7; i < length; i++) {
      futures.add(service.submit(readTask));
    }

    for (final Future<?> future : futures) {
      future.get();
    }

    return System.currentTimeMillis() - time;
  }
}
