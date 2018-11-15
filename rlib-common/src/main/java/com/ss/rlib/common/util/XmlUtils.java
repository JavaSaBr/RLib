package com.ss.rlib.common.util;

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
     * Convert attributes of the xml node to {@link VarTable}.
     *
     * @param node the xml node.
     * @return the new vars table.
     */
    @Deprecated(forRemoval = true)
    public static @NotNull VarTable toVarsTable(@Nullable Node node) {
        return toVarsTable(new VarTable(), node);
    }

    /**
     * New instance var table.
     *
     * @param node      the node
     * @param childName the child name
     * @param nameType  the name type
     * @param nameValue the name value
     * @return the var table
     */
    @Deprecated(forRemoval = true)
    public static @NotNull VarTable toVarsTable(
            @Nullable Node node,
            @NotNull String childName,
            @NotNull String nameType,
            @NotNull String nameValue
    ) {
        return toVarsTable(new VarTable(), node, childName, nameType, nameValue);
    }

    public static @NotNull VarTable toVarsTable(@NotNull VarTable vars, @Nullable Node node) {

        if (node == null) {
            return vars;
        }

        var attributes = node.getAttributes();

        if (attributes == null) {
            return vars;
        }

        for (int i = 0, length = attributes.getLength(); i < length; i++) {
            var item = attributes.item(i);
            vars.put(item.getNodeName(), item.getNodeValue());
        }

        return vars;
    }

    /**
     * Put variables to the vars table from XML node's children.
     *
     * <pre>
     * 	&#60;element&#62;
     * 		&#60;child name="name" value="value" /&#62;
     * 		&#60;child name="name" value="value" /&#62;
     * 		&#60;child name="name" value="value" /&#62;
     * 		&#60;child name="name" value="value" /&#62;
     * 	&#60;/element&#62;
     *
     * XmlUtils.toVarsTable(vars, node, "child", "name", "value")
     * </pre>
     *
     * @param node      the xml node.
     * @param childName the name of a child element.
     * @param nameName  the name of name attribute.
     * @param nameValue the name of value attribute.
     * @return the vars table.
     */
    private static @NotNull VarTable toVarsTable(
            @NotNull VarTable vars,
            @Nullable Node node,
            @NotNull String childName,
            @NotNull String nameName,
            @NotNull String nameValue
    ) {

        if (node == null) {
            return vars;
        }

        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {

            if (child.getNodeType() != Node.ELEMENT_NODE || !childName.equals(child.getNodeName())) {
                continue;
            }

            var attributes = child.getAttributes();
            var nameNode = attributes.getNamedItem(nameName);
            var valueNode = attributes.getNamedItem(nameValue);

            if (nameNode == null || valueNode == null) {
                continue;
            }

            vars.put(nameNode.getNodeValue(), valueNode.getNodeValue());
        }

        return vars;
    }

    /**
     * Find the first element for the name.
     *
     * @param node the node.
     * @param name the name searched element.
     * @return the element or null.
     */
    public static @Nullable Element findFirstElement(@Nullable Node node, @NotNull String name) {

        if (node == null) {
            return null;
        }

        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {

            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if (name.equals(child.getNodeName())) {
                return (Element) child;
            }

            var element = findFirstElement(child, name);

            if (element != null) {
                return element;
            }
        }

        return null;
    }
}
