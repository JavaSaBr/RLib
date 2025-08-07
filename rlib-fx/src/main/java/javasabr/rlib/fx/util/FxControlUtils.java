package javasabr.rlib.fx.util;

import javasabr.rlib.fx.control.input.TypedTextField;
import javasabr.rlib.fx.util.ObservableUtils.ChangeEventAppender;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The utility class.
 *
 * @author JavaSaBr
 */
public class FxControlUtils {

    /**
     * Add handler of text changes.
     *
     * @param control the text control.
     * @param handler the handler.
     * @return the change event appender.
     */
    public static ChangeEventAppender<String> onTextChange(
            @NotNull TextInputControl control,
            @NotNull Runnable handler
    ) {
        return ObservableUtils.onChange(control.textProperty(), handler);
    }

    /**
     * Add handler of text changes.
     *
     * @param control the text control.
     * @param handler the handler.
     * @return the change event appender.
     */
    public static ChangeEventAppender<String> onTextChange(
            @NotNull TextInputControl control,
            @NotNull Consumer<String> handler
    ) {
        return ObservableUtils.onChange(control.textProperty(), handler);
    }

    /**
     * Add handler of selected changes.
     *
     * @param control the check box control.
     * @param handler the handler.
     * @return the change event appender.
     */
    public static ChangeEventAppender<Boolean> onSelectedChange(
            @NotNull CheckBox control,
            @NotNull Runnable handler
    ) {
        return ObservableUtils.onChange(control.selectedProperty(), handler);
    }

    /**
     * Add handler of selected changes.
     *
     * @param control the check box control.
     * @param handler the handler.
     * @return the change event appender.
     */
    public static ChangeEventAppender<Boolean> onSelectedChange(
            @NotNull CheckBox control,
            @NotNull Consumer<Boolean> handler
    ) {
        return ObservableUtils.onChange(control.selectedProperty(), handler);
    }

    /**
     * Add handler of selected changes.
     *
     * @param control the check box control.
     * @param handler the handler.
     * @return the change event appender.
     */
    public static ChangeEventAppender<Boolean> onSelectedChange(
            @NotNull ToggleButton control,
            @NotNull Runnable handler
    ) {
        return ObservableUtils.onChange(control.selectedProperty(), handler);
    }

    /**
     * Add handler of selected changes.
     *
     * @param control the check box control.
     * @param handler the handler.
     * @return the change event appender.
     */
    public static ChangeEventAppender<Boolean> onSelectedChange(
            @NotNull ToggleButton control,
            @NotNull Consumer<Boolean> handler
    ) {
        return ObservableUtils.onChange(control.selectedProperty(), handler);
    }

    /**
     * Add handler of color changes.
     *
     * @param control the color picker control.
     * @param handler the handler.
     * @return the change event appender.
     */
    public static ChangeEventAppender<Color> onColorChange(
            @NotNull ColorPicker control,
            @NotNull Consumer<Color> handler
    ) {
        return ObservableUtils.onChange(control.valueProperty(), handler);
    }

    /**
     * Add handler of color changes.
     *
     * @param control the color picker control.
     * @param handler the handler.
     * @return the change event appender.
     */
    public static ChangeEventAppender<Color> onColorChange(
            @NotNull ColorPicker control,
            @NotNull Runnable handler
    ) {
        return ObservableUtils.onChange(control.valueProperty(), handler);
    }

    /**
     * Add handler of value changes.
     *
     * @param control the typed text field control.
     * @param handler the handler.
     * @param <T>     the value's type.
     * @return the change event appender.
     */
    public static <T> ChangeEventAppender<T> onValueChange(
            @NotNull TypedTextField<T> control,
            @NotNull Consumer<T> handler
    ) {
        return ObservableUtils.onChange(control.valueProperty(), handler);
    }

    /**
     * Add handler of value changes.
     *
     * @param control the typed text field control.
     * @param handler the handler.
     * @param <T>     the value's type.
     * @return the change event appender.
     */
    public static <T> ChangeEventAppender<T> onValueChange(
            @NotNull TypedTextField<T> control,
            @NotNull Runnable handler
    ) {
        return ObservableUtils.onChange(control.valueProperty(), handler);
    }

    /**
     * Add handler of focus state changes.
     *
     * @param control the any focusable control.
     * @param handler the handler.
     * @return the change event appender.
     */
    public static ChangeEventAppender<Boolean> onFocusChange(
            @NotNull Node control,
            @NotNull Consumer<Boolean> handler
    ) {
        return ObservableUtils.onChange(control.focusedProperty(), handler);
    }

    /**
     * Add handler of selected item changes.
     *
     * @param control the combo box control.
     * @param handler the handler.
     * @param <T>     the value's type.
     * @return the change event appender.
     */
    public static <T> ChangeEventAppender<T> onSelectedItemChange(
            @NotNull ComboBox<T> control,
            @NotNull Runnable handler
    ) {
        return ObservableUtils.onChange(control.valueProperty(), handler);
    }

    /**
     * Add handler of selected item changes.
     *
     * @param control the combo box control.
     * @param handler the handler.
     * @param <T>     the value's type.
     * @return the change event appender.
     */
    public static <T> ChangeEventAppender<T> onSelectedItemChange(
            @NotNull ComboBox<T> control,
            @NotNull Consumer<T> handler
    ) {
        return ObservableUtils.onChange(control.valueProperty(), handler);
    }

    /**
     * Add handler of selected item changes.
     *
     * @param control the list view control.
     * @param handler the handler.
     * @param <T>     the value's type.
     * @return the change event appender.
     */
    public static <T> ChangeEventAppender<T> onSelectedItemChange(
            @NotNull ListView<T> control,
            @NotNull Consumer<T> handler
    ) {
        var selectionModel = control.getSelectionModel();
        return ObservableUtils.onChange(selectionModel.selectedItemProperty(), handler);
    }

    /**
     * Add handler of selected item changes.
     *
     * @param control the tree view control.
     * @param handler the handler.
     * @param <T>     the value's type.
     * @return the change event appender.
     */
    public static <T> ChangeEventAppender<TreeItem<T>> onSelectedItemChange(
            @NotNull TreeView<T> control,
            @NotNull Runnable handler
    ) {
        var selectionModel = control.getSelectionModel();
        return ObservableUtils.onChange(selectionModel.selectedItemProperty(), handler);
    }

    /**
     * Add handler of selected item changes.
     *
     * @param control the tree view control.
     * @param handler the handler.
     * @param <T>     the value's type.
     * @return the change event appender.
     */
    public static <T> ChangeEventAppender<TreeItem<T>> onSelectedItemChange(
            @NotNull TreeView<T> control,
            @NotNull Consumer<TreeItem<T>> handler
    ) {
        var selectionModel = control.getSelectionModel();
        return ObservableUtils.onChange(selectionModel.selectedItemProperty(), handler);
    }

    /**
     * Add handler to handle action events.
     *
     * @param control the button control.
     * @param handler the handler.
     */
    public static void onAction(@NotNull ButtonBase control, @NotNull Runnable handler) {
        control.addEventHandler(ActionEvent.ACTION, event -> handler.run());
    }

    /**
     * Add handler of selected tab changes.
     *
     * @param control the tab pane control.
     * @param handler the handler.
     * @return the change event appender.
     */
    public static ChangeEventAppender<Tab> onSelectedTabChange(
            @NotNull TabPane control,
            @NotNull Consumer<Tab> handler
    ) {
        var selectionModel = control.getSelectionModel();
        return ObservableUtils.onChange(selectionModel.selectedItemProperty(), handler);
    }

    /**
     * Add handler of selected tab changes.
     *
     * @param control the tab pane control.
     * @param handler the handler.
     * @return the change event appender.
     */
    public static ChangeEventAppender<Tab> onSelectedTabChange(
            @NotNull TabPane control,
            @NotNull BiConsumer<Tab, Tab> handler
    ) {
        var selectionModel = control.getSelectionModel();
        return ObservableUtils.onChange(selectionModel.selectedItemProperty(), handler);
    }
}
