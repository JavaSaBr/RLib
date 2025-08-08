package javasabr.rlib.fx.control.input;

import javafx.scene.input.ScrollEvent;

/**
 * The implementation of a typed text field control.
 *
 * @author JavaSaBr
 */
public class ScrollableTypedTextField<T> extends TypedTextField<T> {

  /**
   * The scroll power.
   */
  private float scrollPower;

  public ScrollableTypedTextField() {
    setOnScroll(this::scrollValue);
  }

  public ScrollableTypedTextField(String text) {
    super(text);
    setOnScroll(this::scrollValue);
  }

  /**
   * Set the scroll power.
   *
   * @param scrollPower the scroll power.
   */
  public void setScrollPower(float scrollPower) {
    this.scrollPower = scrollPower;
  }

  /**
   * Get the scroll power.
   *
   * @return the scroll power.
   */
  public float getScrollPower() {
    return scrollPower;
  }

  /**
   * Scroll the current value.
   *
   * @param event the scroll event.
   */
  protected void scrollValue(ScrollEvent event) {
    if (event.isControlDown()) {
      event.consume();
      scrollValueImpl(event);
    }
  }

  /**
   * Scroll the current value.
   *
   * @param event the scroll event.
   */
  protected void scrollValueImpl(ScrollEvent event) {
  }
}
