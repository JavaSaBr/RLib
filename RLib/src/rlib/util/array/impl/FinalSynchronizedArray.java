package rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

/**
 * The final implementation of the {@link SynchronizedArray}.
 *
 * @author JavaSaBr.
 */
public final class FinalSynchronizedArray<E> extends SynchronizedArray<E> {

    public FinalSynchronizedArray(@NotNull final Class<E> type) {
        super(type);
    }

    public FinalSynchronizedArray(@NotNull final Class<E> type, final int size) {
        super(type, size);
    }
}
