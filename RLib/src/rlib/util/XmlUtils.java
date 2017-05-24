package rlib.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The utility class.
 *
 * @author JavaSaBR
 */
public class XmlUtils {

    /**
     * Find the first element for the name.
     *
     * @param node the node.
     * @param name the name searched element.
     * @return the element or null.
     */
    @Nullable
    public static Element findFirstElement(@Nullable final Node node, @NotNull final String name) {
        if (node == null) return null;

        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() != Node.ELEMENT_NODE) continue;

            if (name.equals(child.getNodeName())) {
                return (Element) child;
            }

            final Element element = findFirstElement(child, name);
            if (element != null) return element;
        }

        return null;
    }
}
