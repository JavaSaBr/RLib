package javasabr.rlib.fx.impl;

import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

/**
 * The simple implementation of a draggable element.
 *
 * @author JavaSaBr
 */
public class SimpleDraggableElement extends VBox {

  public SimpleDraggableElement() {
    setOnDragDetected(this::processStartDragAndDrop);
    setOnDragDone(this::processFinishDragAndDrop);
  }

  /**
   * Get transfer modes.
   *
   * @return transfer modes.
   */
  protected TransferMode[] getTransferMode() {
    return TransferMode.COPY_OR_MOVE;
  }

  /**
   * Prepare data to the dragboard.
   *
   * @param dragboard the dragboard.
   */
  protected void prepareData(final Dragboard dragboard) {
  }

  /**
   * Handle finishing of dragging.
   *
   * @param event the mouse event.
   */
  protected void processFinishDragAndDrop(final DragEvent event) {
    event.consume();
  }

  /**
   * Handle starting of dragging.
   *
   * @param event the mouse event.
   */
  protected void processStartDragAndDrop(final MouseEvent event) {
    prepareData(startDragAndDrop(getTransferMode()));
    event.consume();
  }
}
