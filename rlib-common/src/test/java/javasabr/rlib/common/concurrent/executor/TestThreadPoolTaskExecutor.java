package javasabr.rlib.common.concurrent.executor;

import javasabr.rlib.common.concurrent.GroupThreadFactory;
import javasabr.rlib.common.concurrent.atomic.AtomicInteger;
import javasabr.rlib.common.concurrent.executor.impl.ThreadPoolTaskExecutor;
import javasabr.rlib.common.concurrent.util.ThreadUtils;
import org.junit.jupiter.api.Assertions;

/**
 * Реализация теста многопоточного исполнителя задач.
 *
 * @author JavaSaBr
 */
public class TestThreadPoolTaskExecutor {

    private static final int TASK_LIMIT = 100;

    public void test() {

        final String header = TestThreadPoolTaskExecutor.class.getSimpleName() + ": ";

        System.out.println(header + " start test executor...");

        final GroupThreadFactory factory = new GroupThreadFactory("test_executor", Thread::new, Thread.NORM_PRIORITY);
        final TaskExecutor<Object> executor = new ThreadPoolTaskExecutor<Object>(factory, 5, 5) {

            @Override
            protected Object getLocalObjects(final Thread thread) {
                return new Object();
            }
        };

        final AtomicInteger counter = new AtomicInteger();

        for (int i = 0; i < TASK_LIMIT; i++) {
            executor.execute((local, currentTime) -> {
                counter.incrementAndGet();
                ThreadUtils.sleep(1);
            });
        }

        ThreadUtils.sleep(30);

        Assertions.assertEquals(TASK_LIMIT, counter.get());

        System.out.println(header + " test executor finished.");
    }
}
