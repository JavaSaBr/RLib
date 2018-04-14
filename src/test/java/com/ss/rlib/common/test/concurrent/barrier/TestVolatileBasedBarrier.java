package com.ss.rlib.common.test.concurrent.barrier;

import com.ss.rlib.common.concurrent.atomic.AtomicReference;
import com.ss.rlib.common.concurrent.util.ConcurrentUtils;
import com.ss.rlib.common.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author JavaSaBr
 */
public class TestVolatileBasedBarrier {

    private static class Executor extends Thread {

        private final List<Runnable> tasks;

        private Executor() {
            this.tasks = new ArrayList<>();
        }

        @Override
        public void run() {

            List<Runnable> toExecute = new ArrayList<>();

            for(;;) {

                toExecute.clear();

                synchronized (tasks) {
                    if (tasks.isEmpty()) {
                        ConcurrentUtils.waitInSynchronize(tasks);
                    }
                    if (!tasks.isEmpty()) {
                        toExecute.addAll(tasks);
                        tasks.clear();
                    }
                }

                if (toExecute.isEmpty()) {
                    continue;
                }

                toExecute.forEach(Runnable::run);
            }
        }

        public void add(@NotNull Runnable task) {
            synchronized (tasks) {
                boolean empty = tasks.isEmpty();
                tasks.add(task);
                if (empty) {
                    ConcurrentUtils.notifyAll(tasks);
                }
            }
        }
    }

    private static final Executor FIRST_EXECUTOR = new Executor();
    private static final Executor SECOND_EXECUTOR = new Executor();

    static {
        FIRST_EXECUTOR.start();
        SECOND_EXECUTOR.start();
    }

    private static class SharedClass {
        private int field1;
        private long field2;
        private byte field3;
        private int[] array = new int[1000];
    }

    private static class SharedClass2 {
        private SharedClass field = new SharedClass();
    }

    private static class SharedClass3 {
        private SharedClass2 field = new SharedClass2();
    }

    @Test
    public void test() throws Throwable {

        AtomicReference<Throwable> excRef = new AtomicReference<>();
        CountDownLatch countDownLatch = new CountDownLatch(100_000);

        for (int i = 0; i < 100_000; i++) {

            testWithoutBarrier(countDownLatch, excRef);

            if (excRef.get() != null) {
                throw excRef.get();
            }
        }

        Utils.run(countDownLatch, CountDownLatch::await);

        if (excRef.get() != null) {
            throw excRef.get();
        }
    }

    private void testWithoutBarrier(
            @NotNull CountDownLatch countDownLatch,
            @NotNull AtomicReference<Throwable> excRef
    ) {

        SharedClass3 sharedClass = new SharedClass3();

        FIRST_EXECUTOR.add(() -> {
            sharedClass.field.field.field1 = 5;
            sharedClass.field.field.field2 = 11L;
            sharedClass.field.field.field3 = -5;
            sharedClass.field.field.array[900] = 6;
            SECOND_EXECUTOR.add(() -> {
                try {
                    Assertions.assertEquals(5, sharedClass.field.field.field1);
                    Assertions.assertEquals(11L, sharedClass.field.field.field2);
                    Assertions.assertEquals(-5, sharedClass.field.field.field3);
                    Assertions.assertEquals(6, sharedClass.field.field.array[900]);
                } catch (Throwable e) {
                    excRef.set(e);
                }
                sharedClass.field.field.field1 = 3;
                sharedClass.field.field.field2 = 18L;
                sharedClass.field.field.field3 = -3;
                FIRST_EXECUTOR.add(() -> {
                    try {
                        Assertions.assertEquals(3, sharedClass.field.field.field1);
                        Assertions.assertEquals(18L, sharedClass.field.field.field2);
                        Assertions.assertEquals(-3, sharedClass.field.field.field3);
                    } catch (Throwable e) {
                        excRef.set(e);
                    }
                    sharedClass.field.field.field1 = 11;
                    sharedClass.field.field.field2 = 2L;
                    sharedClass.field.field.field3 = -9;
                    SECOND_EXECUTOR.add(() -> {
                        try {
                            Assertions.assertEquals(11, sharedClass.field.field.field1);
                            Assertions.assertEquals(2L, sharedClass.field.field.field2);
                            Assertions.assertEquals(-9, sharedClass.field.field.field3);
                        } catch (Throwable e) {
                            excRef.set(e);
                        }
                        countDownLatch.countDown();
                    });
                });
            });
        });
    }
}
