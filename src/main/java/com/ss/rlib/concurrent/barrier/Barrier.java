package com.ss.rlib.concurrent.barrier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The interface to implement a simple barrier to transfer visibility of changes between different threads.
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
       // loadChanges();
        try {
            consumer.accept(first);
        } finally {
       //     commitChanges();
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

    /**
     * Load changes from other threads.
     */
    void loadChanges();

    /**
     * Commit all changes.
     */
    void commitChanges();
}
