package javasabr.rlib.fx.control.dialog;

import javasabr.rlib.fx.CssClasses;
import javasabr.rlib.fx.util.FxUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

/**
 * The default implementation of {@link ControlDialog}
 *
 * @author JavaSaBr
 */
public class DefaultControlDialog extends ControlDialog<GridPane, GridPane, GridPane> {

    @NotNull
    protected final Label titleLabel;

    @NotNull
    protected final Button closeButton;

    public DefaultControlDialog() {
        this.titleLabel = new Label("No title");
        this.closeButton = createCloseButton();
    }

    protected @NotNull Button createCloseButton() {
        return new Button("X");
    }

    @Override
    protected @NotNull GridPane createHeader() {
        return new GridPane();
    }

    @Override
    protected @NotNull GridPane createContainer() {
        return new GridPane();
    }

    @Override
    protected @NotNull GridPane createActions() {
        return new GridPane();
    }

    @Override
    protected void configure() {
        super.configure();

        container.prefHeightProperty()
                .bind(heightProperty());

        closeButton.setOnAction(event -> hide());

        FxUtils.addClass(this, CssClasses.CONTROL_DIALOG)
                .addClass(header, CssClasses.DIALOG_HEADER)
                .addClass(container, CssClasses.DIALOG_CONTENT)
                .addClass(actions, CssClasses.DIALOG_ACTIONS);
    }

    @Override
    protected void fillHeader(@NotNull GridPane header) {
        super.fillHeader(header);

        titleLabel.prefWidthProperty()
                .bind(header.widthProperty());

        FxUtils.addClass(titleLabel, CssClasses.DIALOG_TITLE)
                .addClass(closeButton, CssClasses.BUTTON_CLOSE);

        header.add(titleLabel, 0, 0);
        header.add(closeButton, 1, 0);
    }

    @Override
    protected void fillContent(@NotNull GridPane content) {

        super.fillContent(content);
    }

    @Override
    protected void fillActions(@NotNull GridPane actions) {

        super.fillActions(actions);
    }
}
