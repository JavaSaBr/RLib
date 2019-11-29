package com.ss.rlib.common.data;

import static java.lang.ThreadLocal.withInitial;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.common.util.IOUtils;
import lombok.AccessLevel;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * The base implementation of the parser of xml documents.
 *
 * @author JavaSaBr
 */
public abstract class AbstractStreamDocument<C> implements DocumentXML<C> {

    protected static final Logger LOGGER = LoggerManager.getLogger(DocumentXML.class);

    protected static final ThreadLocal<DocumentBuilderFactory> LOCAL_FACTORY = withInitial(() -> {
        var factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringComments(true);
        return factory;
    });

    /**
     * The stream of xml document.
     */
    @Setter(AccessLevel.PROTECTED)
    protected @Nullable InputStream stream;

    /**
     * the DOM model of this document.
     */
    protected @Nullable Document document;

    /**
     * The result of parsing.
     */
    protected @Nullable C result;

    public AbstractStreamDocument() {
        super();
    }

    public AbstractStreamDocument(@NotNull InputStream stream) {
        this.stream = stream;
    }

    /**
     * Create a container of the result.
     *
     * @return the container of the result.
     */
    protected abstract @NotNull C create();

    @Override
    public @NotNull C parse() {

        var factory = LOCAL_FACTORY.get();
        try {
            var builder = factory.newDocumentBuilder();
            document = builder.parse(stream);
        } catch (SAXException | IOException | ParserConfigurationException exc) {
            LOGGER.warning(exc);
            throw new RuntimeException(exc);
        } finally {
            IOUtils.close(stream);
        }

        result = create();
        try {
            parse(document);
        } catch (Exception exc) {
            LOGGER.warning(exc);
            throw new RuntimeException(exc);
        }

        return result;
    }

    /**
     * The process of parsing this DOM model.
     *
     * @param document the DOM model of this document.
     */
    protected void parse(@NotNull Document document) {
        for (var node = document.getFirstChild(); node != null; node = node.getNextSibling()) {

            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            parse(null, (Element) node);
        }
    }

    /**
     * The process of parsing this document.
     *
     * @param parent  the parent element.
     * @param element the current element.
     */
    protected void parse(@Nullable Element parent, @NotNull Element element) {
        handle(parent, element);

        for (var node = element.getFirstChild(); node != null; node = node.getNextSibling()) {

            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            parse(element, (Element) node);
        }
    }

    /**
     * The process of parsing this element.
     *
     * @param parent  the parent element.
     * @param element the current element.
     */
    protected void handle(@Nullable Element parent, @NotNull Element element) {
    }
}
