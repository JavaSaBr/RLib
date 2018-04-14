package com.ss.rlib.common.concurrent;

import com.ss.rlib.common.concurrent.atomic.AtomicInteger;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.concurrent.atomic.AtomicInteger;
import com.ss.rlib.common.util.ClassUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.concurrent.ThreadFactory;

/**
 * The implementation of the {@link ThreadFactory} for creating some threads in the same thread
 * group.
 *
 * @author JavaSaBr
 */
public class GroupThreadFactory implements ThreadFactory {

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
     * The constructor for making threads.
     */
    private final Constructor<? extends Thread> constructor;

    /**
     * The priority of these threads.
     */
    private final int priority;

    /**
     * Instantiates a new Group thread factory.
     *
     * @param name     the name
     * @param cs       the cs
     * @param priority the priority
     */
    public GroupThreadFactory(@NotNull final String name, @NotNull final Class<? extends Thread> cs, final int priority) {
        this.constructor = ClassUtils.getConstructor(cs, ThreadGroup.class, Runnable.class, String.class);
        this.priority = priority;
        this.name = name;
        this.group = new ThreadGroup(name);
        this.ordinal = new AtomicInteger();
    }

    @Override
    public Thread newThread(@NotNull final Runnable runnable) {
        final Thread thread = ClassUtils.newInstance(constructor, group, runnable, name + "-" + ordinal.incrementAndGet());
        thread.setPriority(priority);
        return thread;
    }
}
