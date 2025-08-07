package javasabr.rlib.fx.control.dialog;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

/**
 * The list of methods to support control dialogs on a scene.
 *
 * @author JavaSaBr
 */
public class ControlDialogSupport {

    private static final String DIALOGS_LAYER = "ControlDialogSupport.dialogsLayer";

    /**
     * Add supporting {@link ControlDialog} in the scene.
     *
     * @param scene the scene.
     */
    public static void addSupport(@NotNull Scene scene) {

        var dialogsLayer = new Pane();
        dialogsLayer.setPickOnBounds(false);
        dialogsLayer.prefWidthProperty()
                .bind(scene.widthProperty());
        dialogsLayer.prefHeightProperty()
                .bind(scene.heightProperty());

        var wrapper = new StackPane(scene.getRoot(), dialogsLayer);

        scene.getProperties()
                .put(DIALOGS_LAYER, dialogsLayer);

        scene.setRoot(wrapper);
    }

    /**
     * Get dialogs layer on the scene.
     *
     * @param scene the scene.
     * @return the dialogs layer.
     */
    static @NotNull Pane getDialogsLayer(@NotNull Scene scene) {

        var layer = (Pane) scene.getProperties()
                .get(DIALOGS_LAYER);

        if (layer == null) {
            throw new IllegalStateException("The scene " + scene + " doesn't support control dialogs, " +
                    "please apply #addSupport(scene) method to your scene to add supporting control dialogs.");
        }

        return layer;
    }
}
