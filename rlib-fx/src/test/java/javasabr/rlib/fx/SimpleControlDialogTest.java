package javasabr.rlib.fx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javasabr.rlib.fx.control.dialog.ControlDialogSupport;
import javasabr.rlib.fx.control.dialog.DefaultControlDialog;
import javasabr.rlib.fx.util.FxUtils;
import org.jetbrains.annotations.NotNull;

public class SimpleControlDialogTest extends Application {

  private static class TestDialog extends DefaultControlDialog {

    @Override
    protected void fillContent(@NotNull GridPane content) {
      super.fillContent(content);

      var textArea = new TextArea("This is a text area.");
      textArea
          .prefWidthProperty()
          .bind(content.widthProperty());
      textArea
          .prefHeightProperty()
          .bind(content.heightProperty());

      content.add(textArea, 0, 0);
    }

    @Override
    protected void fillActions(@NotNull GridPane actions) {
      super.fillActions(actions);

      var okButton = new Button("Ok");
      var cancelButton = new Button("Cancel");

      actions.add(okButton, 0, 0);
      actions.add(cancelButton, 1, 0);

      FxUtils
          .addClass(okButton, CssClasses.BUTTON_ACTION)
          .addClass(cancelButton, CssClasses.BUTTON_ACTION);
    }
  }

  @Override
  public void start(@NotNull Stage stage) {

    var root = new VBox();
    root.setAlignment(Pos.CENTER);

    var scene = new Scene(root);
    scene
        .getStylesheets()
        .add(CssClasses.CSS_FILE);

    var button = new Button("Create a dialog in scene's center");
    button.setOnAction(event -> openInCenterDialog(scene));

    var button2 = new Button("Create a dialog over this button");
    button2.setOnAction(event -> openOverNode(button2));

    var emptyPane = new Pane();
    emptyPane.setMinHeight(300);

    FxUtils.addChild(root, button, emptyPane, button2);

    ControlDialogSupport.addSupport(scene);

    stage.setScene(scene);
    stage.setWidth(1024);
    stage.setHeight(768);
    stage.show();
  }

  private void openInCenterDialog(@NotNull Scene scene) {
    var dialog = new TestDialog();
    dialog.applySize(200, 200);
    dialog.show(scene);
  }

  private void openOverNode(@NotNull Node node) {
    var dialog = new TestDialog();
    dialog.applySize(200, 200);
    dialog.show(node);
  }
}
