package com.ss.rlib.common.plugin.extension;

import com.ss.rlib.common.util.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The class to present an extension point.
 *
 * @author JavaSaBr
 */
public class ExtensionPoint<T> {

    private static class State<T> {

        /**
         * The read only list of extensions.
         */
        @NotNull
        private final List<T> extensions;

        /**
         * The array for fast access.
         */
        @NotNull
        private final Object[] array;

        public State() {
            this.extensions = Collections.emptyList();
            this.array = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }

        public State(@NotNull List<T> extensions, @NotNull Object[] array) {
            this.extensions = extensions;
            this.array = array;
        }

        public @NotNull State<T> append(@NotNull T extension) {

            List<T> result = new ArrayList<>(extensions);
            result.add(extension);

            boolean canBeSorted = result.stream()
                    .allMatch(ext -> ext instanceof Comparable);

            if (canBeSorted) {
                result.sort((first, second) -> ((Comparable<T>) first).compareTo(second));
            }

            List<T> extensions = Collections.unmodifiableList(result);
            Object[] array = result.toArray();

            return new State<>(extensions, array);
        }
    }

    /**
     * The reference to the current state.
     */
    @NotNull
    private final AtomicReference<State<T>> state;

    public ExtensionPoint() {
        this.state = new AtomicReference<>(new State<>());
    }

    /**
     * Register a new extension.
     *
     * @param extension the new extension.
     * @return this point.
     */
    public ExtensionPoint<T> register(@NotNull T extension) {

        State<T> currentState = state.get();
        State<T> newState = currentState.append(extension);

        while (!state.compareAndSet(currentState, newState)) {
            currentState = state.get();
            newState = currentState.append(extension);
        }

        return this;
    }

    /**
     * Get all registered extensions.
     *
     * @return the all registered extensions.
     */
    public @NotNull List<T> getExtensions() {
        return state.get().extensions;
    }

    /**
     * Handle each extension.
     *
     * @param consumer the consumer.
     * @return this point.
     */
    public @NotNull ExtensionPoint<T> forEach(@NotNull Consumer<T> consumer) {

        Object[] array = state.get().array;

        for (Object obj : array) {
            consumer.accept((T) obj);
        }

        return this;
    }

    /**
     * Handle each extension.
     *
     * @param first    the first argument.
     * @param consumer the consumer.
     * @param <F>      the argument's type.
     * @return this point.
     */
    public <F> @NotNull ExtensionPoint<T> forEach(@NotNull F first, @NotNull BiConsumer<T, F> consumer) {

        Object[] array = state.get().array;

        for (Object obj : array) {
            consumer.accept((T) obj, first);
        }

        return this;
    }
}
