package com.ss.rlib.fx.control.dialog;

import static java.lang.Math.max;
import com.ss.rlib.fx.handler.ControlDragHandler;
import com.ss.rlib.fx.handler.ControlResizeHandler;
import com.ss.rlib.fx.util.FxUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementing a dialog like a control to be inside a main window.
 *
 * @author JavaSaBr
 */
public abstract class ControlDialog<H extends Parent, C extends Parent, A extends Parent> extends VBox {

    /**
     * The dialog's header.
     */
    @NotNull
    protected final H header;

    /**
     * The dialog's content container.
     */
    @NotNull
    protected final C container;

    /**
     * The dialog's actions container.
     */
    @NotNull
    protected final A actions;

    /**
     * The owner dialog.
     */
    @Nullable
    private ControlDialog<?, ?, ?> ownerDialog;

    /**
     * The flat about that this dialog was full constructed.
     */
    private volatile boolean ready;

    private final BooleanProperty showing;

    protected ControlDialog() {
        this.showing = new SimpleBooleanProperty(this, "showing", false);
        this.header = createHeader();
        this.container = createContainer();
        this.actions = createActions();
    }

    /**
     * Get the showing property.
     *
     * @return the showing property.
     */
    public @NotNull BooleanProperty showingProperty() {
        return showing;
    }

    /**
     * Create a header container.
     *
     * @return the header container.
     */
    protected abstract @NotNull H createHeader();

    /**
     * Create a content container.
     *
     * @return the content container.
     */
    protected abstract @NotNull C createContainer();

    /**
     * Create an actions container.
     *
     * @return the actions container.
     */
    protected abstract @NotNull A createActions();

    /**
     * Construct content of this dialog after constructor.
     */
    public void postConstruct() {

        if (ready) {
            return;
        }

        configure();
        fillHeader(header);
        fillContent(container);
        fillActions(actions);

        ready = true;
    }

    /**
     * Apply the size to this dialog.
     *
     * @param size the new size.
     */
    public void applySize(@NotNull Point2D size) {
        applySize(size.getX(), size.getY());
    }

    /**
     * Apply the size to this dialog.
     *
     * @param width  the new width.
     * @param height the new height.
     */
    public void applySize(double width, double height) {
        setPrefWidth(width);
        setPrefHeight(height);
    }

    /**
     * Configure this dialog.
     */
    protected void configure() {

        if (getPrefHeight() == USE_COMPUTED_SIZE) {
            setPrefHeight(100);
        }

        if (getPrefWidth() == USE_COMPUTED_SIZE) {
            setPrefWidth(100);
        }

        header.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> toFront());

        if (canDrag()) {
            ControlDragHandler.install(this, header);
        }

        if (canResize()) {
            ControlResizeHandler.install(this);
        }

        FxUtils.addChild(this,
                header, container, actions);
    }

    /**
     * Return true if this dialog can be resized.
     *
     * @return true if this dialog can be resized.
     */
    protected boolean canResize() {
        return true;
    }

    /**
     * Return true if this dialog can be dragged.
     *
     * @return true if this dialog can be dragged.
     */
    protected boolean canDrag() {
        return true;
    }

    /**
     * Fill header of this dialog.
     *
     * @param header the header container.
     */
    protected void fillHeader(@NotNull H header) {

    }

    /**
     * Fill content of this dialog.
     *
     * @param content the content's container.
     */
    protected void fillContent(@NotNull C content) {

    }

    /**
     * Fill actions of this dialog.
     *
     * @param actions the actions.
     */
    protected void fillActions(@NotNull A actions) {

    }

    /**
     * Show this dialog over the node.
     *
     * @param node the node.
     */
    public void show(@NotNull Node node) {

        var parent = node.getParent();
        var bounds = node.getBoundsInParent();
        var scene = node.getScene();
        var inScene = parent.localToScene(bounds.getMinX() + bounds.getWidth() / 2,
                bounds.getMinY() + bounds.getHeight() / 2);

        var prefHeight = max(max(getWidth(), getMinWidth()), getPrefHeight()) / 2;
        var prefWidth = max(max(getHeight(), getMinHeight()), getPrefWidth()) / 2;

        show(scene, inScene.getX() - prefWidth, inScene.getY() - prefHeight);
    }

    /**
     * Show this dialog on the scene.
     *
     * @param scene the scene.
     */
    public void show(@NotNull Scene scene) {

        var prefHeight = max(max(getWidth(), getMinWidth()), getPrefHeight()) / 2;
        var prefWidth = max(max(getHeight(), getMinHeight()), getPrefWidth()) / 2;

        show(scene, (scene.getWidth() / 2) - prefWidth, (scene.getHeight() / 2) - prefHeight);
    }

    /**
     * Show this dialog on the scene by the coords.
     *
     * @param scene the scene.
     * @param x the X coord.
     * @param y the Y coord.
     */
    public void show(@NotNull Scene scene, double x, double y) {

        if (showing.get()) {
            return;
        }

        postConstruct();

        ControlDialogSupport.getDialogsLayer(scene)
                .getChildren()
                .add(this);

        setTranslateX(x);
        setTranslateY(y);

        if (ownerDialog != null) {
            ownerDialog.setDisabled(true);
        }

        showing.setValue(true);
    }

    /**
     * Hide this dialog.
     */
    public void hide() {

        if (!showing.get()) {
            return;
        }

        ControlDialogSupport.getDialogsLayer(getScene())
                .getChildren()
                .remove(this);

        if (ownerDialog != null) {
            ownerDialog.setDisabled(false);
            ownerDialog = null;
        }

        showing.setValue(false);
    }
}
