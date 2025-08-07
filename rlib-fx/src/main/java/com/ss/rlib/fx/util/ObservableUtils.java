package com.ss.rlib.fx.util;

import static javasabr.rlib.common.util.ClassUtils.unsafeCast;
import javafx.beans.value.ObservableValue;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * The utility class.
 *
 * @author JavaSaBr
 */
public class ObservableUtils {

    public static class ChangeEventAppender<T> implements AutoCloseable {

        private ObservableValue<T> value;

        public void complete() {
            value = null;
        }

        /**
         * Add on change event handler.
         *
         * @param action the action on changes.
         * @return the change event appender.
         */
        public ChangeEventAppender<T> onChange(@NotNull Runnable action) {
            value.addListener((observable, oldValue, newValue) -> action.run());
            return this;
        }

        /**
         * Add on change event handler.
         *
         * @param predicate the condition to use the consumer.
         * @param action    the action on changes.
         * @return the change event appender.
         */
        public ChangeEventAppender<T> onChangeIf(@NotNull Predicate<T> predicate, @NotNull Runnable action) {

            value.addListener((observable, oldValue, newValue) -> {
                if (predicate.test(newValue)) {
                    action.run();
                }
            });

            return this;
        }

        /**
         * Add on change event handler.
         *
         * @param consumer the consumer to handle new values.
         * @return the change event appender.
         */
        public ChangeEventAppender<T> onChange(@NotNull Consumer<T> consumer) {
            value.addListener((observable, oldValue, newValue) -> consumer.accept(newValue));
            return this;
        }

        /**
         * Add on change event handler.
         *
         * @param predicate the condition to use the consumer.
         * @param consumer  the consumer to handle new values.
         * @return the change event appender.
         */
        public ChangeEventAppender<T> onChangeIf(@NotNull Predicate<T> predicate, @NotNull Consumer<T> consumer) {

            value.addListener((observable, oldValue, newValue) -> {
                if (predicate.test(newValue)) {
                    consumer.accept(newValue);
                }
            });

            return this;
        }

        /**
         * Add on change event handler.
         *
         * @param consumer the consumer to handle new values.
         * @return the change event appender.
         */
        public ChangeEventAppender<T> onChange(@NotNull BiConsumer<T, T> consumer) {
            value.addListener((observable, oldValue, newValue) -> consumer.accept(oldValue, newValue));
            return this;
        }

        /**
         * Add on change event handler.
         *
         * @param predicate the condition to use the consumer.
         * @param consumer  the consumer to handle new values.
         * @return the change event appender.
         */
        public ChangeEventAppender<T> onChangeIf(
                @NotNull BiPredicate<T, T> predicate,
                @NotNull BiConsumer<T, T> consumer
        ) {

            value.addListener((observable, oldValue, newValue) -> {
                if (predicate.test(oldValue, newValue)) {
                    consumer.accept(oldValue, newValue);
                }
            });

            return this;
        }

        @Override
        public void close() throws Exception {
            value = null;
        }
    }

    private static final ThreadLocal<ChangeEventAppender> CHANGE_EVENT_APPENDER =
        ThreadLocal.withInitial(ChangeEventAppender::new);

    private static <T> @NotNull ChangeEventAppender getAppender(@NotNull ObservableValue<T> value) {

        ChangeEventAppender<T> appender = unsafeCast(CHANGE_EVENT_APPENDER.get());
        appender.value = value;

        return appender;
    }

    /**
     * Add on change event handler.
     *
     * @param value  the observable value.
     * @param action the action on changes.
     * @param <T>    the value type.
     * @return the change event appender.
     */
    public static <T> @NotNull ChangeEventAppender<T> onChange(
            @NotNull ObservableValue<T> value,
            @NotNull Runnable action
    ) {
        value.addListener((observable, oldValue, newValue) -> action.run());
        return getAppender(value);
    }

    /**
     * Add on change event handler.
     *
     * @param value     the observable value.
     * @param predicate the condition to use the consumer.
     * @param action    the action on changes.
     * @param <T>       the value type.
     * @return the change event appender.
     */
    public static <T> ChangeEventAppender<T> onChangeIf(
            @NotNull ObservableValue<T> value,
            @NotNull Predicate<T> predicate,
            @NotNull Runnable action
    ) {

        value.addListener((observable, oldValue, newValue) -> {
            if (predicate.test(newValue)) {
                action.run();
            }
        });

        return getAppender(value);
    }

    /**
     * Add on change event handler.
     *
     * @param value    the observable value.
     * @param consumer the consumer to handle new values.
     * @param <T>      the value type.
     * @return the change event appender.
     */
    public static <T> ChangeEventAppender<T> onChange(
            @NotNull ObservableValue<T> value,
            @NotNull Consumer<T> consumer
    ) {
        value.addListener((observable, oldValue, newValue) -> consumer.accept(newValue));
        return getAppender(value);
    }

    /**
     * Add on change event handler.
     *
     * @param value     the observable value.
     * @param predicate the condition to use the consumer.
     * @param consumer  the consumer to handle new values.
     * @param <T>       the value type.
     * @return the change event appender.
     */
    public static <T> ChangeEventAppender<T> onChangeIf(
            @NotNull ObservableValue<T> value,
            @NotNull Predicate<T> predicate,
            @NotNull Consumer<T> consumer
    ) {

        value.addListener((observable, oldValue, newValue) -> {
            if (predicate.test(newValue)) {
                consumer.accept(newValue);
            }
        });

        return getAppender(value);
    }

    /**
     * Start building change handlers.
     *
     * @param value    the observable value.
     * @param <T>      the value type.
     * @return the change event appender.
     */
    public static <T> ChangeEventAppender<T> onChanges(@NotNull ObservableValue<T> value) {
        return getAppender(value);
    }

    /**
     * Add on change event handler.
     *
     * @param value    the observable value.
     * @param consumer the consumer to handle new values.
     * @param <T>      the value type.
     * @return the change event appender.
     */
    public static <T> ChangeEventAppender<T> onChange(
            @NotNull ObservableValue<T> value,
            @NotNull BiConsumer<T, T> consumer
    ) {
        value.addListener((observable, oldValue, newValue) -> consumer.accept(oldValue, newValue));
        return getAppender(value);
    }

    /**
     * Add on change event handler.
     *
     * @param value     the observable value.
     * @param predicate the condition to use the consumer.
     * @param consumer  the consumer to handle new values.
     * @param <T>       the value type.
     * @return the change event appender.
     */
    public static <T> ChangeEventAppender<T> onChangeIf(
            @NotNull ObservableValue<T> value,
            @NotNull BiPredicate<T, T> predicate,
            @NotNull BiConsumer<T, T> consumer
    ) {

        value.addListener((observable, oldValue, newValue) -> {
            if (predicate.test(oldValue, newValue)) {
                consumer.accept(oldValue, newValue);
            }
        });

        return getAppender(value);
    }
}
