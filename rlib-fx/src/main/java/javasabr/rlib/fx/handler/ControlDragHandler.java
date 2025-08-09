package javasabr.rlib.fx.handler;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javasabr.rlib.fx.LoggerClass;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import javasabr.rlib.logger.api.LoggerManager;

/**
 * The implementation of a handler to process windows dragging.
 *
 * @author JavaSaBr
 */
public class ControlDragHandler {

  private static final Logger LOGGER = LoggerManager.getLogger(LoggerClass.class);

  /**
   * Install the handler to the node.
   *
   * @param draggableNode the node which should be moved.
   * @param draggableControl the node which should be like a handle.
   */
  public static void install(Node draggableNode, Node draggableControl) {
    new ControlDragHandler(draggableNode, draggableControl);
  }

  /**
   * The draggable node.
   */
  private final Node draggableNode;

  /**
   * The draggable control.
   */
  private final Node draggableControl;

  /**
   * The offset X coord.
   */
  private double offsetX;

  /**
   * The offset Y coord.
   */
  private double offsetY;

  /**
   * The started X coord.
   */
  private double startX;

  /**
   * The started Y coord.
   */
  private double startY;

  public ControlDragHandler(Node draggableNode, Node draggableControl) {
    this.draggableNode = draggableNode;
    this.draggableControl = draggableControl;
    this.draggableControl.setOnMousePressed(this::processStartDrag);
    this.draggableControl.setOnMouseDragged(this::processMove);
    this.draggableControl.setOnMouseReleased(this::processStopDrag);
  }

  /**
   * Handle moving.
   *
   * @param event the mouse event
   */
  protected void processMove(MouseEvent event) {

    LOGGER.debug(event, ev -> "processMove -> " + ev);

    if (LOGGER.isEnabled(LoggerLevel.DEBUG)) {
      LOGGER.debug("processMove -> dragOffset -> " + offsetX + "," + offsetY);
    }

    var dragPosition = draggableNode
        .getParent()
        .sceneToLocal(draggableControl.localToScene(event.getX(), event.getY()));

    var dragX = dragPosition.getX() - offsetX;
    var dragY = dragPosition.getY() - offsetY;

    if (LOGGER.isEnabled(LoggerLevel.DEBUG)) {
      LOGGER.debug("processMove -> dragXY -> " + (dragX - offsetX) + "-" + (dragY - offsetY));
    }

    var newXPos = startX + dragX;
    var newYPos = startY + dragY;

    if (LOGGER.isEnabled(LoggerLevel.DEBUG)) {
      LOGGER.debug("processMove -> newXY -> " + newXPos + ", " + newYPos);
    }

    draggableNode.setTranslateX(newXPos);
    draggableNode.setTranslateY(newYPos);
  }

  /**
   * Handle starting moving.
   *
   * @param event the mouse event.
   */
  protected void processStartDrag(MouseEvent event) {

    LOGGER.debug(event, ev -> "processStartDrag -> " + ev);

    startX = draggableNode.getTranslateX();
    startY = draggableNode.getTranslateY();

    LOGGER.debug(this, handler -> "processStartDrag -> initXY -> " + handler.startX + ", " + handler.startY);

    var offset = draggableNode
        .getParent()
        .sceneToLocal(draggableControl.localToScene(event.getX(), event.getY()));

    LOGGER.debug(offset, point -> "processStartDrag -> dragOffset -> " + point);

    offsetX = offset.getX();
    offsetY = offset.getY();
  }

  /**
   * Handle finishing moving.
   *
   * @param event the event
   */
  protected void processStopDrag(MouseEvent event) {

    startX = 0;
    startY = 0;
    offsetX = 0;
    offsetY = 0;

    LOGGER.debug(event, ev -> "processStopDrag -> " + ev);
  }
}
