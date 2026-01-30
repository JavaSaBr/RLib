package javasabr.rlib.concurrent.executor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;
import javasabr.rlib.common.util.GroupThreadFactory;
import javasabr.rlib.common.util.ThreadUtils;
import javasabr.rlib.concurrent.executor.impl.ThreadPoolTaskExecutor;

/**
 * @author JavaSaBr
 */
public class TestThreadPoolTaskExecutor {

  private static final int TASK_LIMIT = 100;

  public void test() {

    final String header = TestThreadPoolTaskExecutor.class.getSimpleName() + ": ";

    System.out.println(header + " start test executor...");

    final GroupThreadFactory factory = new GroupThreadFactory("test_executor", Thread::new, Thread.NORM_PRIORITY);
    final TaskExecutor<Object> executor = new ThreadPoolTaskExecutor<>(factory, thread -> new Object(), 5, 5);

    final AtomicInteger counter = new AtomicInteger();

    for (int i = 0; i < TASK_LIMIT; i++) {
      executor.execute((local, currentTime) -> {
        counter.incrementAndGet();
        ThreadUtils.sleep(1);
      });
    }

    ThreadUtils.sleep(30);

    assertThat(counter.get()).isEqualTo(TASK_LIMIT);

    System.out.println(header + " test executor finished.");
  }
}
