package com.ss.rlib.common.concurrent.barrier;

import com.ss.rlib.common.function.TripleConsumer;
import com.ss.rlib.common.function.TripleConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The interface to implement a memory barrier to transfer visibility of changes between different threads.
 *
 * @author JavaSaBr
 */
public interface Barrier {

    default void run(@NotNull Runnable runnable) {
        loadChanges();
        try {
            runnable.run();
        } finally {
            commitChanges();
        }
    }

    default <F> void run(@Nullable F first, @NotNull Consumer<F> consumer) {
        loadChanges();
        try {
            consumer.accept(first);
        } finally {
            commitChanges();
        }
    }

    default <F, S> void run(@Nullable F first, @Nullable S second, @NotNull BiConsumer<F, S> consumer) {
        loadChanges();
        try {
            consumer.accept(first, second);
        } finally {
            commitChanges();
        }
    }

    default <F, S, T> void run(
            @Nullable F first,
            @Nullable S second,
            @Nullable T third,
            @NotNull TripleConsumer<F, S, T> consumer
    ) {
        loadChanges();
        try {
            consumer.accept(first, second, third);
        } finally {
            commitChanges();
        }
    }

    /**
     * Load changes from other threads.
     */
    void loadChanges();

    /**
     * Commit all changes.
     */
    void commitChanges();
}
