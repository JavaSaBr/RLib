package javasabr.rlib.fx.util;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javasabr.rlib.fx.control.input.TypedTextField;
import javasabr.rlib.fx.util.ObservableUtils.ChangeEventAppender;

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
  public static ChangeEventAppender<String> onTextChange(TextInputControl control, Runnable handler) {
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
      TextInputControl control,
      Consumer<String> handler) {
    return ObservableUtils.onChange(control.textProperty(), handler);
  }

  /**
   * Add handler of selected changes.
   *
   * @param control the check box control.
   * @param handler the handler.
   * @return the change event appender.
   */
  public static ChangeEventAppender<Boolean> onSelectedChange(CheckBox control, Runnable handler) {
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
      CheckBox control,
      Consumer<Boolean> handler) {
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
      ToggleButton control,
      Runnable handler) {
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
      ToggleButton control,
      Consumer<Boolean> handler) {
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
      ColorPicker control,
      Consumer<Color> handler) {
    return ObservableUtils.onChange(control.valueProperty(), handler);
  }

  /**
   * Add handler of color changes.
   *
   * @param control the color picker control.
   * @param handler the handler.
   * @return the change event appender.
   */
  public static ChangeEventAppender<Color> onColorChange(ColorPicker control, Runnable handler) {
    return ObservableUtils.onChange(control.valueProperty(), handler);
  }

  /**
   * Add handler of value changes.
   *
   * @param control the typed text field control.
   * @param handler the handler.
   * @param <T> the value's type.
   * @return the change event appender.
   */
  public static <T> ChangeEventAppender<T> onValueChange(
      TypedTextField<T> control,
      Consumer<T> handler) {
    return ObservableUtils.onChange(control.valueProperty(), handler);
  }

  /**
   * Add handler of value changes.
   *
   * @param control the typed text field control.
   * @param handler the handler.
   * @param <T> the value's type.
   * @return the change event appender.
   */
  public static <T> ChangeEventAppender<T> onValueChange(
      TypedTextField<T> control,
      Runnable handler) {
    return ObservableUtils.onChange(control.valueProperty(), handler);
  }

  /**
   * Add handler of focus state changes.
   *
   * @param control the any focusable control.
   * @param handler the handler.
   * @return the change event appender.
   */
  public static ChangeEventAppender<Boolean> onFocusChange(Node control, Consumer<Boolean> handler) {
    return ObservableUtils.onChange(control.focusedProperty(), handler);
  }

  /**
   * Add handler of selected item changes.
   *
   * @param control the combo box control.
   * @param handler the handler.
   * @param <T> the value's type.
   * @return the change event appender.
   */
  public static <T> ChangeEventAppender<T> onSelectedItemChange(
      ComboBox<T> control,
      Runnable handler) {
    return ObservableUtils.onChange(control.valueProperty(), handler);
  }

  /**
   * Add handler of selected item changes.
   *
   * @param control the combo box control.
   * @param handler the handler.
   * @param <T> the value's type.
   * @return the change event appender.
   */
  public static <T> ChangeEventAppender<T> onSelectedItemChange(
      ComboBox<T> control,
      Consumer<T> handler) {
    return ObservableUtils.onChange(control.valueProperty(), handler);
  }

  /**
   * Add handler of selected item changes.
   *
   * @param control the list view control.
   * @param handler the handler.
   * @param <T> the value's type.
   * @return the change event appender.
   */
  public static <T> ChangeEventAppender<T> onSelectedItemChange(
      ListView<T> control,
      Consumer<T> handler) {
    var selectionModel = control.getSelectionModel();
    return ObservableUtils.onChange(selectionModel.selectedItemProperty(), handler);
  }

  /**
   * Add handler of selected item changes.
   *
   * @param control the tree view control.
   * @param handler the handler.
   * @param <T> the value's type.
   * @return the change event appender.
   */
  public static <T> ChangeEventAppender<TreeItem<T>> onSelectedItemChange(
      TreeView<T> control,
      Runnable handler) {
    var selectionModel = control.getSelectionModel();
    return ObservableUtils.onChange(selectionModel.selectedItemProperty(), handler);
  }

  /**
   * Add handler of selected item changes.
   *
   * @param control the tree view control.
   * @param handler the handler.
   * @param <T> the value's type.
   * @return the change event appender.
   */
  public static <T> ChangeEventAppender<TreeItem<T>> onSelectedItemChange(
      TreeView<T> control,
      Consumer<TreeItem<T>> handler) {
    var selectionModel = control.getSelectionModel();
    return ObservableUtils.onChange(selectionModel.selectedItemProperty(), handler);
  }

  /**
   * Add handler to handle action events.
   *
   * @param control the button control.
   * @param handler the handler.
   */
  public static void onAction(ButtonBase control, Runnable handler) {
    control.addEventHandler(ActionEvent.ACTION, event -> handler.run());
  }

  /**
   * Add handler of selected tab changes.
   *
   * @param control the tab pane control.
   * @param handler the handler.
   * @return the change event appender.
   */
  public static ChangeEventAppender<Tab> onSelectedTabChange(TabPane control, Consumer<Tab> handler) {
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
      TabPane control,
      BiConsumer<Tab, Tab> handler) {
    var selectionModel = control.getSelectionModel();
    return ObservableUtils.onChange(selectionModel.selectedItemProperty(), handler);
  }
}
