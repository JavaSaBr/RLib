package com.ss.rlib.fx.control.input;

import static com.ss.rlib.common.util.ClassUtils.unsafeCast;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of a typed text field control.
 *
 * @author JavaSaBr
 */
public class TypedTextField<T> extends TextField {

    public TypedTextField() {
        setTextFormatter(new TextFormatter<>(createValueConverter()));
    }

    public TypedTextField(@NotNull String text) {
        super(text);
        setTextFormatter(new TextFormatter<>(createValueConverter()));
    }

    /**
     * Create a new value converter.
     *
     * @return the new value converter.
     */
    protected @NotNull StringConverter<T> createValueConverter() {
        throw new UnsupportedOperationException();
    }

    /**
     * Add a new change listener.
     *
     * @param listener the change listener.
     */
    public void addChangeListener(@NotNull ChangeListener<T> listener) {
        getTypedTextFormatter()
                .valueProperty()
                .addListener(listener);
    }

    /**
     * Get the typed text formatter.
     *
     * @return the typed text formatter.
     */
    protected @NotNull TextFormatter<T> getTypedTextFormatter() {
        return unsafeCast(getTextFormatter());
    }

    /**
     * Get the value property.
     *
     * @return the value property.
     */
    public @NotNull ReadOnlyObjectProperty<T> valueProperty() {
        return getTypedTextFormatter().valueProperty();
    }

    /**
     * Get a current value.
     *
     * @return the current value.
     */
    public @Nullable T getValue() {
        return getTypedTextFormatter().getValue();
    }

    /**
     * Set a new value.
     *
     * @param value the new value.
     */
    public void setValue(@Nullable T value) {
        getTypedTextFormatter().setValue(value);
    }
}
