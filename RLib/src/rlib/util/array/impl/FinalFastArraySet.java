package rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

/**
 * The final version of the {@link FastArraySet}.
 *
 * @author JavaSaBr
 */
public final class FinalFastArraySet<E> extends FastArraySet<E> {

    public FinalFastArraySet(@NotNull final Class<E> type) {
        super(type);
    }

    public FinalFastArraySet(@NotNull final Class<E> type, final int size) {
        super(type, size);
    }
}
