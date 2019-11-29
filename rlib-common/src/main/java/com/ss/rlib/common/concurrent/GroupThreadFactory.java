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

    private final AtomicInteger ordinal;
    private final String name;
    private final ThreadGroup group;
    private final ThreadConstructor constructor;

    private final int priority;
    private final boolean daemon;

    public GroupThreadFactory(@NotNull String name) {
        this(name, Thread::new, Thread.NORM_PRIORITY);
    }

    public GroupThreadFactory(@NotNull String name, int priority) {
        this(name, Thread::new, priority);
    }

    public GroupThreadFactory(@NotNull String name, @NotNull ThreadConstructor constructor, int priority) {
        this(name, constructor, priority, false);
    }

    public GroupThreadFactory(
        @NotNull String name,
        @NotNull ThreadConstructor constructor,
        int priority,
        boolean daemon
    ) {
        this.constructor = constructor;
        this.priority = priority;
        this.name = name;
        this.group = new ThreadGroup(name);
        this.ordinal = new AtomicInteger();
        this.daemon = daemon;
    }

    @Override
    public @NotNull Thread newThread(@NotNull Runnable runnable) {
        var thread = constructor.create(group, runnable, name + "-" + ordinal.incrementAndGet());
        thread.setPriority(priority);
        thread.setDaemon(daemon);
        return thread;
    }
}
