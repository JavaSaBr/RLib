package com.ss.rlib.common.concurrent;

import com.ss.rlib.common.concurrent.atomic.AtomicInteger;
import com.ss.rlib.common.util.ClassUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.concurrent.ThreadFactory;

/**
 * The implementation of the {@link ThreadFactory} to create threads in the same thread
 * group.
 *
 * @author JavaSaBr
 */
public class GroupThreadFactory implements ThreadFactory {

    public interface ThreadConstructor {

        @NotNull Thread create(@NotNull ThreadGroup group, @NotNull Runnable runnable, @NotNull String name);
    }

    /**
     * The order of the next thread.
     */
    private final AtomicInteger ordinal;

    /**
     * The name of this group.
     */
    private final String name;

    /**
     * The group of threads.
     */
    private final ThreadGroup group;

    /**
     * The thread's constructor.
     */
    private final ThreadConstructor constructor;

    /**
     * The priority of these threads.
     */
    private final int priority;

    public GroupThreadFactory(@NotNull String name) {
        this(name, Thread::new, Thread.NORM_PRIORITY);
    }

    public GroupThreadFactory(@NotNull String name, int priority) {
        this(name, Thread::new, priority);
    }

    public GroupThreadFactory(@NotNull String name, @NotNull ThreadConstructor constructor, int priority) {
        this.constructor = constructor;
        this.priority = priority;
        this.name = name;
        this.group = new ThreadGroup(name);
        this.ordinal = new AtomicInteger();
    }

    @Deprecated
    public GroupThreadFactory(@NotNull String name, @NotNull Class<? extends Thread> cs, int priority) {
        this.priority = priority;
        this.name = name;
        this.group = new ThreadGroup(name);
        this.ordinal = new AtomicInteger();
        this.constructor = new ThreadConstructor() {

            Constructor<? extends Thread> constructor =
                ClassUtils.getConstructor(cs, ThreadGroup.class, Runnable.class, String.class);

            @Override
            public @NotNull Thread create(
                @NotNull ThreadGroup group,
                @NotNull Runnable runnable,
                @NotNull String name
            ) {
                return ClassUtils.newInstance(constructor, group, runnable, name);
            }
        };
    }

    @Override
    public @NotNull Thread newThread(@NotNull Runnable runnable) {
        var thread = constructor.create(group, runnable, name + "-" + ordinal.incrementAndGet());
        thread.setPriority(priority);
        return thread;
    }
}
