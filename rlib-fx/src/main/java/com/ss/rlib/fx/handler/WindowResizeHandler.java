package com.ss.rlib.fx.handler;

import static javafx.scene.input.MouseEvent.*;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of a windows resize handler.
 *
 * @author JavaSaBr
 */
public class WindowResizeHandler implements EventHandler<MouseEvent> {

    /**
     * Install the resize handler.
     *
     * @param stage the stage.
     * @param nodes the nodes.
     */
    public static void install(@NotNull Stage stage, @NotNull Node... nodes) {

        var handler = new WindowResizeHandler(stage);
        var scene = stage.getScene();
        scene.addEventHandler(MOUSE_MOVED, handler);
        scene.addEventHandler(MOUSE_PRESSED, handler);
        scene.addEventHandler(MOUSE_DRAGGED, handler);
        scene.addEventHandler(MOUSE_EXITED, handler);
        scene.addEventHandler(MOUSE_EXITED_TARGET, handler);

        addHandler(scene.getRoot(), handler);

        for (var node : nodes) {
            addHandler(node, handler);
        }
    }

    private static void addHandler(@NotNull Node node, @NotNull EventHandler<MouseEvent> listener) {
        node.addEventHandler(MOUSE_MOVED, listener);
        node.addEventHandler(MOUSE_PRESSED, listener);
        node.addEventHandler(MOUSE_DRAGGED, listener);
        node.addEventHandler(MOUSE_EXITED, listener);
        node.addEventHandler(MOUSE_EXITED_TARGET, listener);
    }

    @NotNull
    private final Stage stage;

    @NotNull
    private Cursor cursorEvent;

    private double startX;
    private double startY;

    private int border;

    public WindowResizeHandler(@NotNull Stage stage) {
        this.stage = stage;
        this.border = 4;
        this.startX = 0;
        this.startY = 0;
        this.cursorEvent = Cursor.DEFAULT;
    }

    @Override
    public void handle(@NotNull MouseEvent mouseEvent) {

        var mouseEventType = mouseEvent.getEventType();
        var scene = stage.getScene();

        var mouseEventX = mouseEvent.getSceneX();
        var mouseEventY = mouseEvent.getSceneY();
        var sceneWidth = scene.getWidth();
        var sceneHeight = scene.getHeight();

        if (MOUSE_MOVED.equals(mouseEventType)) {

            if (mouseEventX < border && mouseEventY < border) {
                cursorEvent = Cursor.NW_RESIZE;
            } else if (mouseEventX < border && mouseEventY > sceneHeight - border) {
                cursorEvent = Cursor.SW_RESIZE;
            } else if (mouseEventX > sceneWidth - border && mouseEventY < border) {
                cursorEvent = Cursor.NE_RESIZE;
            } else if (mouseEventX > sceneWidth - border && mouseEventY > sceneHeight - border) {
                cursorEvent = Cursor.SE_RESIZE;
            } else if (mouseEventX < border) {
                cursorEvent = Cursor.W_RESIZE;
            } else if (mouseEventX > sceneWidth - border) {
                cursorEvent = Cursor.E_RESIZE;
            } else if (mouseEventY < border) {
                cursorEvent = Cursor.N_RESIZE;
            } else if (mouseEventY > sceneHeight - border) {
                cursorEvent = Cursor.S_RESIZE;
            } else {
                cursorEvent = Cursor.DEFAULT;
            }

            scene.setCursor(cursorEvent);

        } else if (MOUSE_EXITED.equals(mouseEventType) || MOUSE_EXITED_TARGET.equals(mouseEventType)) {
            scene.setCursor(Cursor.DEFAULT);
        } else if (MOUSE_PRESSED.equals(mouseEventType)) {
            startX = stage.getWidth() - mouseEventX;
            startY = stage.getHeight() - mouseEventY;
        } else if (MOUSE_DRAGGED.equals(mouseEventType)) {

            if (Cursor.DEFAULT.equals(cursorEvent)) {
                return;
            }

            if (!Cursor.W_RESIZE.equals(cursorEvent) && !Cursor.E_RESIZE.equals(cursorEvent)) {
                double minHeight = stage.getMinHeight() > (border * 2) ? stage.getMinHeight() : (border * 2);
                if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.N_RESIZE.equals(cursorEvent) ||
                        Cursor.NE_RESIZE.equals(cursorEvent)) {
                    if (stage.getHeight() > minHeight || mouseEventY < 0) {
                        stage.setHeight(stage.getY() - mouseEvent.getScreenY() + stage.getHeight());
                        stage.setY(mouseEvent.getScreenY());
                    }
                } else {
                    if (stage.getHeight() > minHeight || mouseEventY + startY - stage.getHeight() > 0) {
                        stage.setHeight(mouseEventY + startY);
                    }
                }
            }

            if (!Cursor.N_RESIZE.equals(cursorEvent) && !Cursor.S_RESIZE.equals(cursorEvent)) {
                double minWidth = stage.getMinWidth() > (border * 2) ? stage.getMinWidth() : (border * 2);
                if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.W_RESIZE.equals(cursorEvent) ||
                        Cursor.SW_RESIZE.equals(cursorEvent)) {
                    if (stage.getWidth() > minWidth || mouseEventX < 0) {
                        stage.setWidth(stage.getX() - mouseEvent.getScreenX() + stage.getWidth());
                        stage.setX(mouseEvent.getScreenX());
                    }
                } else {
                    if (stage.getWidth() > minWidth || mouseEventX + startX - stage.getWidth() > 0) {
                        stage.setWidth(mouseEventX + startX);
                    }
                }
            }
        }
    }
}
