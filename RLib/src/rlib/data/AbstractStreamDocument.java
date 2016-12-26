package rlib.data;

import static java.lang.ThreadLocal.withInitial;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.IOUtils;

/**
 * The base implementation of the parser of xml documents.
 *
 * @author JavaSaBr
 */
public abstract class AbstractStreamDocument<C> implements DocumentXML<C> {

    protected static final Logger LOGGER = LoggerManager.getLogger(DocumentXML.class);

    protected static final ThreadLocal<DocumentBuilderFactory> LOCAL_FACTORY = withInitial(() -> {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringComments(true);
        return factory;
    });

    /**
     * The stream of xml document.
     */
    protected InputStream stream;

    /**
     * the DOM model of this document.
     */
    protected Document document;

    /**
     * The result of parsing.
     */
    protected C result;

    public AbstractStreamDocument() {
        super();
    }

    public AbstractStreamDocument(@NotNull final InputStream stream) {
        this.stream = stream;
    }

    /**
     * Creates the container of the result.
     */
    @NotNull
    protected abstract C create();

    @NotNull
    @Override
    public C parse() {

        final DocumentBuilderFactory factory = LOCAL_FACTORY.get();
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(stream);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            LOGGER.warning(this, e);
            throw new RuntimeException(e);
        } finally {
            IOUtils.close(stream);
        }

        result = create();

        try {
            parse(document);
        } catch (final Exception e) {
            LOGGER.warning(this, e);
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * The process of parsing this DOM model.
     *
     * @param document the DOM model of this document.
     */
    protected void parse(@NotNull final Document document) {
        for (Node node = document.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;
            parse(null, (Element) node);
        }
    }

    /**
     * The process of parsing this document.
     *
     * @param parent  the parent element.
     * @param element the current element.
     */
    protected void parse(@Nullable final Element parent, @NotNull final Element element) {
        handle(parent, element);

        for (Node node = element.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;
            parse(element, (Element) node);
        }
    }

    /**
     * The process of parsing this element.
     *
     * @param parent  the parent element.
     * @param element the current element.
     */
    protected void handle(@Nullable final Element parent, @NotNull final Element element) {
    }

    /**
     * @param stream the stream to parsing.
     */
    protected void setStream(@NotNull final InputStream stream) {
        this.stream = stream;
    }
}
