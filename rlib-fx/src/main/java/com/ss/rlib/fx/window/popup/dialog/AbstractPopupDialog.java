package com.ss.rlib.fx.window.popup.dialog;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.stage.Popup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of a dialog which is based on a popup.
 *
 * @author JavaSaBr
 */
public abstract class AbstractPopupDialog<C extends Node> extends Popup {

    protected static final Point2D DEFAULT_DIALOG_SIZE =
            new Point2D(500, 500);

    /**
     * The dialog's root container.
     */
    @NotNull
    private final C container;

    /**
     * The owner dialog.
     */
    @Nullable
    private AbstractPopupDialog<?> ownerDialog;

    /**
     * The flat about that this dialog was full constructed.
     */
    private volatile boolean ready;

    public AbstractPopupDialog() {
        container = createRoot();
        getContent().add(container);
    }

    /**
     * Create a root container of this dialog.
     *
     * @return the root container of this dialog.
     */
    protected abstract @NotNull C createRoot();

    /**
     * Construct content of this dialog after constructor.
     */
    public void postConstruct() {

        if (ready) {
            return;
        }

        createControls(container);
        configureSize(container, getSize());

        ready = true;
    }

    /**
     * Configure a size of this dialog.
     *
     * @param container the root container.
     * @param size      the size.
     */
    protected void configureSize(@NotNull C container, @NotNull Point2D size) {
    }

    /**
     * Create content of this dialog.
     *
     * @param root the root.
     */
    protected void createControls(@NotNull C root) {
    }

    /**
     * Get the content container.
     *
     * @return the content container.
     */
    protected @NotNull C getContainer() {
        return container;
    }

    /**
     * Get the owner dialog.
     *
     * @return the owner dialog.
     */
    protected @Nullable AbstractPopupDialog<?> getOwnerDialog() {
        return ownerDialog;
    }

    /**
     * Set the owner dialog.
     *
     * @param ownerDialog the owner dialog.
     */
    protected void setOwnerDialog(@Nullable AbstractPopupDialog<?> ownerDialog) {
        this.ownerDialog = ownerDialog;
    }

    /**
     * Get the dialog size.
     *
     * @return the dialog size.
     */
    protected @NotNull Point2D getSize() {
        return DEFAULT_DIALOG_SIZE;
    }

    @Override
    protected void show() {
        postConstruct();
        super.show();
    }

    @Override
    public void hide() {
        super.hide();

        var dialog = getOwnerDialog();

        if (dialog != null) {
            dialog.getContainer().setDisable(false);
        }

        setOwnerDialog(null);
    }
}
