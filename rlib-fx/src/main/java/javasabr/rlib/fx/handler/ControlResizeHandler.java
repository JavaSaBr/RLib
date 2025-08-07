package javasabr.rlib.fx.handler;

import static javafx.scene.input.MouseEvent.MOUSE_DRAGGED;
import static javafx.scene.input.MouseEvent.MOUSE_EXITED;
import static javafx.scene.input.MouseEvent.MOUSE_EXITED_TARGET;
import static javafx.scene.input.MouseEvent.MOUSE_MOVED;
import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of a control resize handler.
 *
 * @author JavaSaBr
 */
public class ControlResizeHandler implements EventHandler<MouseEvent> {

    /**
     * Install resize handler to the region.
     *
     * @param node the region.
     */
    public static void install(@NotNull Region node) {
        install(node, 4);
    }

    /**
     * Install resize handler to the region.
     *
     * @param node the region.
     * @param borderWidth the border width.
     */
    public static void install(@NotNull Region node, int borderWidth) {

        var handler = new ControlResizeHandler(node, borderWidth);

        node.addEventHandler(MOUSE_MOVED, handler);
        node.addEventHandler(MOUSE_PRESSED, handler);
        node.addEventHandler(MOUSE_DRAGGED, handler);
        node.addEventHandler(MOUSE_EXITED, handler);
        node.addEventHandler(MOUSE_EXITED_TARGET, handler);
    }

    @NotNull
    private final Region node;

    @NotNull
    private Cursor cursor;

    private double startX;
    private double startY;

    private int border;

    public ControlResizeHandler(@NotNull Region node, int borderWidth) {
        this.node = node;
        this.border = borderWidth;
        this.startX = 0;
        this.startY = 0;
        this.cursor = Cursor.DEFAULT;
    }

    @Override
    public void handle(@NotNull MouseEvent mouseEvent) {

        var eventType = mouseEvent.getEventType();
        var eventX = mouseEvent.getX();
        var eventY = mouseEvent.getY();

        var nodeWidth = node.getWidth();
        var nodeHeight = node.getHeight();

        if (MOUSE_MOVED.equals(eventType)) {
            handleMoving(eventX, eventY, nodeWidth, nodeHeight);
        } else if (MOUSE_EXITED.equals(eventType) || MOUSE_EXITED_TARGET.equals(eventType)) {
            node.setCursor(Cursor.DEFAULT);
        } else if (MOUSE_PRESSED.equals(eventType)) {
            startX = nodeWidth - eventX;
            startY = nodeHeight - eventY;
        } else if (MOUSE_DRAGGED.equals(eventType)) {

            if (Cursor.DEFAULT.equals(cursor)) {
                return;
            } else if (!Cursor.W_RESIZE.equals(cursor) && !Cursor.E_RESIZE.equals(cursor)) {
                resizeHeight(eventY);
            }

            if (!Cursor.N_RESIZE.equals(cursor) && !Cursor.S_RESIZE.equals(cursor)) {
                resizeWidth(eventX);
            }
        }
    }

    private void resizeWidth(double eventX) {

        var minWidth = node.getMinWidth() > (border * 2) ? node.getMinWidth() : (border * 2);
        var newWidth = eventX + startX;

        if (node.getWidth() > minWidth || newWidth - node.getWidth() > 0) {
            node.setPrefWidth(newWidth);
        }
    }

    private void resizeHeight(double eventY) {

        var minHeight = node.getMinHeight() > (border * 2) ? node.getMinHeight() : (border * 2);
        var newHeight = eventY + startY;

        if (node.getHeight() > minHeight || newHeight - node.getHeight() > 0) {
            node.setPrefHeight(newHeight);
        }
    }

    private void handleMoving(double eventX, double eventY, double nodeWidth, double nodeHeight) {

        if (eventX > nodeWidth - border && eventY > nodeHeight - border) {
            cursor = Cursor.SE_RESIZE;
        } else if (eventX > nodeWidth - border) {
            cursor = Cursor.E_RESIZE;
        } else if (eventY > nodeHeight - border) {
            cursor = Cursor.S_RESIZE;
        } else {
            cursor = Cursor.DEFAULT;
        }

        node.setCursor(cursor);
    }
}
