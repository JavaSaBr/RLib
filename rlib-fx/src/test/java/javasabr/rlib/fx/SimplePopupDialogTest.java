package javasabr.rlib.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javasabr.rlib.fx.dialog.SimplePopupDialog;
import javasabr.rlib.fx.util.FxUtils;

public class SimplePopupDialogTest extends Application {

  @Override
  public void start(Stage stage) throws Exception {

    var root = new StackPane();
    var scene = new Scene(root);
    scene
        .getStylesheets()
        .add(CssClasses.CSS_FILE);

    var button = new Button("Create a dialog");
    button.setOnAction(event -> openDialog(stage));

    FxUtils.addChild(root, button);

    stage.setScene(scene);
    stage.setWidth(1024);
    stage.setHeight(768);
    stage.show();
  }

  private void openDialog(Stage window) {
    SimplePopupDialog dialog = new SimplePopupDialog();
    dialog.show(window);
  }
}
