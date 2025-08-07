package javasabr.rlib.fx;

import javasabr.rlib.fx.util.FxUtils;
import javasabr.rlib.fx.dialog.SimplePopupDialog;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class SimplePopupDialogTest extends Application {

    @Override
    public void start(@NotNull Stage stage) throws Exception {

        var root = new StackPane();
        var scene = new Scene(root);
        scene.getStylesheets()
                .add(CssClasses.CSS_FILE);

        var button = new Button("Create a dialog");
        button.setOnAction(event -> openDialog(stage));

        FxUtils.addChild(root, button);

        stage.setScene(scene);
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.show();
    }

    private void openDialog(@NotNull Stage window) {
        SimplePopupDialog dialog = new SimplePopupDialog();
        dialog.show(window);
    }
}
