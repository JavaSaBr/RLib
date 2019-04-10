package com.ss.rlib.fx.handler;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerLevel;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.fx.LoggerClass;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of a handler to process windows dragging.
 *
 * @author JavaSaBr
 */
public class WindowDragHandler {

    private static final Logger LOGGER = LoggerManager.getLogger(LoggerClass.class);

    /**
     * Install the handler to the node.
     *
     * @param node the node.
     */
    public static void install(@NotNull Node node) {
        new WindowDragHandler(node);
    }

    /**
     * The node.
     */
    @NotNull
    private final Node node;

    /**
     * The drag offset.
     */
    @Nullable
    private Point2D dragOffset;

    /**
     * The started X coord.
     */
    private double startX;

    /**
     * The started Y coord.
     */
    private double startY;

    public WindowDragHandler(@NotNull Node node) {
        this.node = node;
        this.node.setOnMousePressed(this::processStartDrag);
        this.node.setOnMouseDragged(this::processMove);
        this.node.setOnMouseReleased(this::processStopDrag);
    }

    /**
     * Get the drag offset.
     *
     * @return the drag offset.
     */
    protected @Nullable Point2D getDragOffset() {
        return dragOffset;
    }

    /**
     * Set the drag offset.
     *
     * @param dragOffset the drag offset.
     */
    public void setDragOffset(@Nullable Point2D dragOffset) {
        this.dragOffset = dragOffset;
    }

    /**
     * Get the start X coord.
     *
     * @return the start X coord.
     */
    protected double getStartX() {
        return startX;
    }

    /**
     * Set the start X coord.
     *
     * @param startX the start X coord.
     */
    public void setStartX(double startX) {
        this.startX = startX;
    }

    /**
     * Get start Y coord.
     *
     * @return the start Y coord.
     */
    protected double getStartY() {
        return startY;
    }

    /**
     * Set the start Y coord.
     *
     * @param startY the start Y coord.
     */
    public void setStartY(double startY) {
        this.startY = startY;
    }

    /**
     * Get the handled node.
     *
     * @return the handled node.
     */
    public @NotNull Node getNode() {
        return node;
    }

    /**
     * Handle moving.
     *
     * @param event the event
     */
    protected void processMove(@NotNull MouseEvent event) {

        LOGGER.debug(this, event, ev -> "processMove -> " + ev);

        var node = getNode();
        var scene = node.getScene();
        var window = scene.getWindow();

        var dragOffset = getDragOffset();
        if (dragOffset == null) {
            LOGGER.debug(this, "processMove -> dragOffset -> null");
            return;
        }

        LOGGER.debug(this, dragOffset, offset -> "processMove -> dragOffset -> " + offset);

        var dragX = event.getScreenX() - dragOffset.getX();
        var dragY = event.getScreenY() - dragOffset.getY();

        LOGGER.debug(this, dragOffset, event, (offset, ev) ->
                "processMove -> dragXY -> " + (ev.getScreenX() - offset.getX()) +
                        "-" + (ev.getScreenY() - offset.getY()));

        var newXPos = startX + dragX;
        var newYPos = startY + dragY;

        if (LOGGER.isEnabled(LoggerLevel.DEBUG)) {
            LOGGER.debug(this, "processMove -> newXY -> " + newXPos + ", " + newYPos);
        }

        window.setX(newXPos);
        window.setY(newYPos);
    }

    /**
     * Handle starting moving.
     *
     * @param event the event.
     */
    protected void processStartDrag(@NotNull MouseEvent event) {

        LOGGER.debug(this, event, ev -> "processStartDrag -> " + ev);

        var node = getNode();
        var scene = node.getScene();
        var window = scene.getWindow();

        setStartX(window.getX());
        setStartY(window.getY());

        LOGGER.debug(this, this, handler ->
                "processStartDrag -> initXY -> " + handler.getStartX() + ", " + handler.getStartY());

        var dragOffset = new Point2D(event.getScreenX(), event.getScreenY());

        LOGGER.debug(this, dragOffset,
                offset -> "processStartDrag -> dragOffset -> " + offset);

        setDragOffset(dragOffset);
    }

    /**
     * Handle finishing moving.
     *
     * @param event the event
     */
    protected void processStopDrag(@NotNull MouseEvent event) {
        LOGGER.debug(this, event, ev -> "processStopDrag -> " + ev);
        setStartX(0);
        setStartY(0);
        setDragOffset(null);
    }
}
