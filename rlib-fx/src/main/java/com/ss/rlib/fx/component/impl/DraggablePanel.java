package com.ss.rlib.fx.component.impl;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of a draggable panel.
 *
 * @author JavaSaBr
 */
public class DraggablePanel extends VBox {

    /**
     * The drag offset.
     */
    @Nullable
    private Point2D dragOffset;

    /**
     * The start X coord.
     */
    private double startX;

    /**
     * The start Y coord.
     */
    private double startY;

    public DraggablePanel() {
        setOnMousePressed(this::processStartDrag);
        setOnMouseDragged(this::processMove);
        setOnMouseReleased(this::processStopDrag);
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
    public void setDragOffset(@Nullable final Point2D dragOffset) {
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
    public void setStartX(final double startX) {
        this.startX = startX;
    }

    /**
     * Get the start Y coord.
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
    public void setStartY(final double startY) {
        this.startY = startY;
    }

    /**
     * Handle moving.
     *
     * @param event the mouse event.
     */
    protected void processMove(@NotNull final MouseEvent event) {

        final Point2D dragOffset = getDragOffset();
        if (dragOffset == null) {
            return;
        }

        final double dragX = event.getSceneX() - dragOffset.getX();
        final double dragY = event.getSceneY() - getDragOffset().getY();

        final double newXPosition = startX + dragX;
        final double newYPosition = startY + dragY;

        setTranslateX(newXPosition);
        setTranslateY(newYPosition);
    }

    /**
     * Handle starting of dragging.
     *
     * @param event the mouse event.
     */
    protected void processStartDrag(@NotNull final MouseEvent event) {
        setStartX(getTranslateX());
        setStartY(getTranslateY());
        setDragOffset(new Point2D(event.getSceneX(), event.getSceneY()));
        toFront();
    }

    /**
     * Handle finishing of dragging.
     *
     * @param event the mouse event.
     */
    protected void processStopDrag(@NotNull final MouseEvent event) {
        setStartX(0);
        setStartY(0);
        setDragOffset(null);
    }
}
