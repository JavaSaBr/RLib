package com.ss.rlib.test.concurrent.executor;

import com.ss.rlib.concurrent.executor.impl.ThreadPoolTaskExecutor;
import org.junit.Assert;
import org.junit.Test;

import com.ss.rlib.concurrent.GroupThreadFactory;
import com.ss.rlib.concurrent.atomic.AtomicInteger;
import com.ss.rlib.concurrent.executor.TaskExecutor;
import com.ss.rlib.concurrent.util.ThreadUtils;

/**
 * Реализация теста многопоточного исполнителя задач.
 *
 * @author JavaSaBr
 */
public class TestThreadPoolTaskExecutor extends Assert {

    private static final int TASK_LIMIT = 100;

    @Test
    public void test() {

        final String header = TestThreadPoolTaskExecutor.class.getSimpleName() + ": ";

        System.out.println(header + " start test executor...");

        final GroupThreadFactory factory = new GroupThreadFactory("test_executor", Thread.class, Thread.NORM_PRIORITY);

        final TaskExecutor<Void> executor = new ThreadPoolTaskExecutor<>(factory, 5, 5);

        final AtomicInteger counter = new AtomicInteger();

        for (int i = 0; i < TASK_LIMIT; i++) {
            executor.execute((local, currentTime) -> {
                counter.incrementAndGet();
                ThreadUtils.sleep(1);
            });
        }

        ThreadUtils.sleep(30);

        assertTrue(counter.get() == TASK_LIMIT);

        System.out.println(header + " test executor finished.");
    }
}
