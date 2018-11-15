package com.ss.rlib.fx.window.popup.dialog;

import com.ss.rlib.fx.CssClasses;
import com.ss.rlib.fx.handler.WindowDragHandler;
import com.ss.rlib.fx.util.FxUtils;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
public class SimplePopupDialog extends AbstractPopupDialog<VBox> {

    @NotNull
    private final Label titleLabel;

    @NotNull
    private final Button closeButton;

    public SimplePopupDialog() {
        this.titleLabel = new Label("Title");
        this.closeButton = new Button("X");

        FxUtils.addClass(titleLabel, CssClasses.DIALOG_TITLE)
                .addClass(closeButton, CssClasses.BUTTON_CLOSE);
    }

    @Override
    protected @NotNull VBox createRoot() {
        var root = new VBox();
        FxUtils.addClass(root, CssClasses.SIMPLE_POPUP_DIALOG);
        return root;
    }

    @Override
    protected void configureSize(@NotNull VBox container, @NotNull Point2D size) {
        super.configureSize(container, size);
        FxUtils.setFixedSize(container, size);
    }

    @Override
    protected void createControls(@NotNull VBox root) {
        super.createControls(root);

        var header = new GridPane();
        var content = new GridPane();
        var actions = new GridPane();

        WindowDragHandler.install(header);

        fillHeader(header);
        fillContent(content);
        fillActions(actions);

        FxUtils.addClass(header, CssClasses.DIALOG_HEADER)
                .addClass(content, CssClasses.DIALOG_CONTENT)
                .addClass(actions, CssClasses.DIALOG_ACTIONS);

        FxUtils.addChild(root, header, content, actions);
    }

    public void setTitle(@NotNull String title) {
        this.titleLabel.setText(title);
    }

    protected void fillHeader(@NotNull GridPane container) {

        titleLabel.prefWidthProperty()
                .bind(container.widthProperty());

        var closeButton = new Button("X");
        closeButton.setOnAction(event -> hide());

        FxUtils.addClass(closeButton, CssClasses.BUTTON_CLOSE);

        container.add(titleLabel, 0, 0);
        container.add(closeButton, 1, 0);
    }

    protected void fillContent(@NotNull GridPane container) {

        var textArea = new TextArea();

        container.add(textArea, 0, 0);
    }

    protected void fillActions(@NotNull GridPane container) {

        var buttonYes = new Button("Yes");
        var buttonNo = new Button("No");

        container.add(buttonYes, 0,0);
        container.add(buttonNo, 1, 0);
    }
}
