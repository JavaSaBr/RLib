package javasabr.rlib.fx.util;

import java.awt.Point;
import java.util.Random;
import java.util.function.Consumer;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import org.jetbrains.annotations.NotNull;

/**
 * The utility class.
 *
 * @author JavaSaBr
 */
@Deprecated
public class FXUtils {

    /**
     * Add a css class to nodes.
     *
     * @param first     the first styleable.
     * @param second    the second styleable.
     * @param className the css class.
     */
    public static void addClassTo(@NotNull final Styleable first, @NotNull final Styleable second,
                                  @NotNull final String className) {
        first.getStyleClass().add(className);
        second.getStyleClass().add(className);
    }

    /**
     * Add a css classes to nodes.
     *
     * @param first      the first styleable.
     * @param second     the second styleable.
     * @param classNames the css classes.
     */
    public static void addClassesTo(@NotNull final Styleable first, @NotNull final Styleable second,
                                    @NotNull final String... classNames) {
        first.getStyleClass().addAll(classNames);
        second.getStyleClass().addAll(classNames);
    }


    /**
     * Add a css class to nodes.
     *
     * @param first     the first styleable.
     * @param second    the second styleable.
     * @param third     the third styleable.
     * @param className the css class.
     */
    public static void addClassTo(@NotNull final Styleable first, @NotNull final Styleable second,
                                  @NotNull final Styleable third, @NotNull final String className) {
        first.getStyleClass().add(className);
        second.getStyleClass().add(className);
        third.getStyleClass().add(className);
    }

    /**
     * Add a css classes to nodes.
     *
     * @param first      the first styleable.
     * @param second     the second styleable.
     * @param third      the third styleable.
     * @param classNames the css classes.
     */
    public static void addClassesTo(@NotNull final Styleable first, @NotNull final Styleable second,
                                    @NotNull final Styleable third, @NotNull final String... classNames) {
        first.getStyleClass().addAll(classNames);
        second.getStyleClass().addAll(classNames);
        third.getStyleClass().addAll(classNames);
    }

    /**
     * Add a css class to nodes.
     *
     * @param first     the first styleable.
     * @param second    the second styleable.
     * @param third     the third styleable.
     * @param fourth    the fourth styleable.
     * @param className the css class.
     */
    public static void addClassTo(@NotNull final Styleable first, @NotNull final Styleable second,
                                  @NotNull final Styleable third, @NotNull final Styleable fourth,
                                  @NotNull final String className) {
        first.getStyleClass().add(className);
        second.getStyleClass().add(className);
        third.getStyleClass().add(className);
        fourth.getStyleClass().add(className);
    }

    /**
     * Add a css classes to nodes.
     *
     * @param first      the first styleable.
     * @param second     the second styleable.
     * @param third      the third styleable.
     * @param fourth     the fourth styleable.
     * @param classNames the css classes.
     */
    public static void addClassesTo(@NotNull final Styleable first, @NotNull final Styleable second,
                                    @NotNull final Styleable third, @NotNull final Styleable fourth,
                                    @NotNull final String... classNames) {
        first.getStyleClass().addAll(classNames);
        second.getStyleClass().addAll(classNames);
        third.getStyleClass().addAll(classNames);
        fourth.getStyleClass().addAll(classNames);
    }

    /**
     * Add a css class to nodes.
     *
     * @param first     the first styleable.
     * @param second    the second styleable.
     * @param third     the third styleable.
     * @param fourth    the fourth styleable.
     * @param fifth     the fifth styleable.
     * @param className the css class.
     */
    public static void addClassTo(@NotNull final Styleable first, @NotNull final Styleable second,
                                  @NotNull final Styleable third, @NotNull final Styleable fourth,
                                  @NotNull final Styleable fifth, @NotNull final String className) {
        first.getStyleClass().add(className);
        second.getStyleClass().add(className);
        third.getStyleClass().add(className);
        fourth.getStyleClass().add(className);
        fifth.getStyleClass().add(className);
    }

    /**
     * Add a css classes to nodes.
     *
     * @param first      the first styleable.
     * @param second     the second styleable.
     * @param third      the third styleable.
     * @param fourth     the fourth styleable.
     * @param fifth      the fifth styleable.
     * @param classNames the css classes.
     */
    public static void addClassesTo(@NotNull final Styleable first, @NotNull final Styleable second,
                                    @NotNull final Styleable third, @NotNull final Styleable fourth,
                                    @NotNull final Styleable fifth, @NotNull final String... classNames) {
        first.getStyleClass().addAll(classNames);
        second.getStyleClass().addAll(classNames);
        third.getStyleClass().addAll(classNames);
        fourth.getStyleClass().addAll(classNames);
        fifth.getStyleClass().addAll(classNames);
    }

    /**
     * Add a css class to nodes.
     *
     * @param first     the first styleable.
     * @param second    the second styleable.
     * @param third     the third styleable.
     * @param fourth    the fourth styleable.
     * @param fifth     the fifth styleable.
     * @param sixth     the sixth styleable.
     * @param className the css class.
     */
    public static void addClassTo(@NotNull final Styleable first, @NotNull final Styleable second,
                                  @NotNull final Styleable third, @NotNull final Styleable fourth,
                                  @NotNull final Styleable fifth, @NotNull final Styleable sixth,
                                  @NotNull final String className) {
        first.getStyleClass().add(className);
        second.getStyleClass().add(className);
        third.getStyleClass().add(className);
        fourth.getStyleClass().add(className);
        fifth.getStyleClass().add(className);
        sixth.getStyleClass().add(className);
    }

    /**
     * Add a css classes to nodes.
     *
     * @param first      the first styleable.
     * @param second     the second styleable.
     * @param third      the third styleable.
     * @param fourth     the fourth styleable.
     * @param fifth      the fifth styleable.
     * @param sixth      the sixth styleable.
     * @param classNames the css classes.
     */
    public static void addClassesTo(@NotNull final Styleable first, @NotNull final Styleable second,
                                    @NotNull final Styleable third, @NotNull final Styleable fourth,
                                    @NotNull final Styleable fifth, @NotNull final Styleable sixth,
                                    @NotNull final String... classNames) {
        first.getStyleClass().addAll(classNames);
        second.getStyleClass().addAll(classNames);
        third.getStyleClass().addAll(classNames);
        fourth.getStyleClass().addAll(classNames);
        fifth.getStyleClass().addAll(classNames);
        sixth.getStyleClass().addAll(classNames);
    }

    /**
     * Add a css class to a styleable.
     *
     * @param styleable the styleable.
     * @param className the css class.
     */
    public static void addClassTo(@NotNull final Styleable styleable, @NotNull final String className) {
        styleable.getStyleClass().add(className);
    }

    /**
     * Add a css classes to a styleable.
     *
     * @param styleable  the styleable.
     * @param classNames the css classes.
     */
    public static void addClassesTo(@NotNull final Styleable styleable, @NotNull final String... classNames) {
        styleable.getStyleClass().addAll(classNames);
    }

    /**
     * Add css classes to a styleables.
     *
     * @param args the list of styleables and class names.
     */
    public static void addClassesTo(@NotNull final Object... args) {
        for (final Object object : args) {

            if (!(object instanceof Styleable)) {
                continue;
            }

            final ObservableList<String> classes = ((Styleable) object).getStyleClass();

            for (final Object arg : args) {
                if (arg instanceof String) {
                    classes.add((String) arg);
                }
            }
        }
    }

    /**
     * Add a random color to background.
     *
     * @param node the node.
     */
    public static void addDebugBackgroundColor(@NotNull final Node node) {

        final Random random = new Random();
        final String color =
                "rgb(" + random.nextInt(255) + "," + random.nextInt(255) + ", " + random.nextInt(255) + ")";

        node.setStyle("-fx-background-color: " + color + ";");
    }

    /**
     * Add a debug border to a node..
     *
     * @param node the node.
     */
    public static void addDebugBorderTo(@NotNull final Node node) {
        node.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
    }

    /**
     * Add a node to a parent.
     *
     * @param node   the node.
     * @param parent the parent.
     */
    public static void addToPane(@NotNull final Node node, @NotNull final Pane parent) {
        final ObservableList<Node> children = parent.getChildren();
        children.add(node);
    }

    /**
     * Add nodes to a parent.
     *
     * @param first  the first node.
     * @param second the second node.
     * @param parent the parent.
     */
    public static void addToPane(@NotNull final Node first, @NotNull final Node second, @NotNull final Pane parent) {
        final ObservableList<Node> children = parent.getChildren();
        children.add(first);
        children.add(second);
    }

    /**
     * Add nodes to a parent.
     *
     * @param first  the first node.
     * @param second the second node.
     * @param third  the third node.
     * @param parent the parent.
     */
    public static void addToPane(@NotNull final Node first, @NotNull final Node second, @NotNull final Node third,
                                 @NotNull final Pane parent) {
        final ObservableList<Node> children = parent.getChildren();
        children.add(first);
        children.add(second);
        children.add(third);
    }

    /**
     * Add nodes to a parent.
     *
     * @param first  the first node.
     * @param second the second node.
     * @param third  the third node.
     * @param fourth the fourth node.
     * @param parent the parent.
     */
    public static void addToPane(@NotNull final Node first, @NotNull final Node second, @NotNull final Node third,
                                 @NotNull final Node fourth, @NotNull final Pane parent) {
        final ObservableList<Node> children = parent.getChildren();
        children.add(first);
        children.add(second);
        children.add(third);
        children.add(fourth);
    }

    /**
     * Bind fixed height of region to height property.
     *
     * @param region         the region.
     * @param heightProperty the height property.
     */
    public static void bindFixedHeight(@NotNull final Region region,
                                       @NotNull final ReadOnlyProperty<Double> heightProperty) {
        region.minHeightProperty().bind(heightProperty);
        region.maxHeightProperty().bind(heightProperty);
    }


    /**
     * Bind fixed height of region to height value.
     *
     * @param region the region.
     * @param height the height value.
     */
    public static void bindFixedHeight(@NotNull final Region region,
                                       @NotNull final ObservableValue<? extends Number> height) {
        region.minHeightProperty().bind(height);
        region.maxHeightProperty().bind(height);
    }

    /**
     * Bind fixed size of region to a height and width properties.
     *
     * @param region         the region.
     * @param widthProperty  the width property.
     * @param heightProperty the height property.
     */
    public static void bindFixedSize(@NotNull final Region region,
                                     @NotNull final ReadOnlyProperty<Double> widthProperty,
                                     @NotNull final ReadOnlyProperty<Double> heightProperty) {
        region.minWidthProperty().bind(widthProperty);
        region.maxWidthProperty().bind(widthProperty);
        region.minHeightProperty().bind(heightProperty);
        region.maxHeightProperty().bind(heightProperty);
    }

    /**
     * Bind fixed size of region to a height and width properties.
     *
     * @param region the region.
     * @param width  the width value.
     * @param height the height value.
     */
    public static void bindFixedSize(@NotNull final Region region,
                                     @NotNull final ObservableValue<? extends Number> width,
                                     @NotNull final ObservableValue<? extends Number> height) {
        region.minWidthProperty().bind(width);
        region.maxWidthProperty().bind(width);
        region.minHeightProperty().bind(height);
        region.maxHeightProperty().bind(height);
    }

    /**
     * Bind fixed height of region to width property.
     *
     * @param region        the region.
     * @param widthProperty the height property.
     */
    public static void bindFixedWidth(@NotNull final Region region,
                                      @NotNull final ReadOnlyProperty<Double> widthProperty) {
        region.minWidthProperty().bind(widthProperty);
        region.maxWidthProperty().bind(widthProperty);
    }

    /**
     * Bind fixed height of region to width value.
     *
     * @param region the region.
     * @param width  the height value.
     */
    public static void bindFixedWidth(@NotNull final Region region,
                                      @NotNull final ObservableValue<? extends Number> width) {
        region.minWidthProperty().bind(width);
        region.maxWidthProperty().bind(width);
    }

    /**
     * Remove a node from a parent.
     *
     * @param node   the node.
     * @param parent the parent.
     */
    public static void removeFromParent(@NotNull final Node node, @NotNull final Pane parent) {
        final ObservableList<Node> children = parent.getChildren();
        children.remove(node);
    }

    /**
     * Set fixed height to a region.
     *
     * @param region the region.
     * @param height the height.
     */
    public static void setFixedHeight(@NotNull final Region region, final double height) {
        region.setMaxHeight(height);
        region.setMinHeight(height);
    }

    /**
     * Set fixed size to a region.
     *
     * @param region the region.
     * @param width  the width.
     * @param height the height.
     */
    public static void setFixedSize(@NotNull final Region region, final double width, final double height) {
        region.setMaxHeight(height);
        region.setMinHeight(height);
        region.setMaxWidth(width);
        region.setMinWidth(width);
    }

    /**
     * Set fixed size to a region.
     *
     * @param region the region.
     * @param size   the size.
     */
    public static void setFixedSize(@NotNull final Region region, @NotNull final Point size) {
        region.setMaxHeight(size.getY());
        region.setMinHeight(size.getY());
        region.setMaxWidth(size.getX());
        region.setMinWidth(size.getX());
    }

    /**
     * Set fixed width to a region.
     *
     * @param region the region.
     * @param width  the width.
     */
    public static void setFixedWidth(@NotNull final Region region, final double width) {
        region.setMaxWidth(width);
        region.setMinWidth(width);
    }

    /**
     * Apply the handler to each child of the node.
     *
     * @param node    the node.
     * @param handler the handler.
     */
    public static void applyToChildren(@NotNull final Parent node, @NotNull final Consumer<Node> handler) {
        node.getChildrenUnmodifiable().forEach(handler);
    }
}
