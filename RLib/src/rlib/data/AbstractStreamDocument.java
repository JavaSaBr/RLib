package rlib.data;

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
 * Базовая модель для парсера данных с xml файла.
 *
 * @author Ronn
 */
public abstract class AbstractStreamDocument<C> implements DocumentXML<C> {

    protected static final Logger LOGGER = LoggerManager.getLogger(DocumentXML.class);

    protected static final ThreadLocal<DocumentBuilderFactory> LOCAL_FACTORY = ThreadLocal.withInitial(() -> {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringComments(true);
        return factory;
    });

    /**
     * Поток файла.
     */
    protected InputStream stream;

    /**
     * Отпарсенный xml фаил.
     */
    protected Document document;

    /**
     * Конечные данные.
     */
    protected C result;

    public AbstractStreamDocument() {
        super();
    }

    public AbstractStreamDocument(final InputStream stream) {
        this.stream = stream;
    }

    /**
     * Создание соответствующей коллекцией, в которой будет хранится результат.
     */
    protected abstract C create();

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
     * Процесс парсинга хмл файла.
     *
     * @param document DOM представление xml файла.
     */
    protected void parse(final Document document) {
        for (Node node = document.getFirstChild(); node != null; node = node.getNextSibling()) {

            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            parse(null, (Element) node);
        }
    }

    ;

    /**
     * Процесс парсинга хмл файла.
     *
     * @param parent  родительский элемент.
     * @param element текущий элемент.
     */
    protected void parse(final Element parent, final Element element) {
        handle(parent, element);

        for (Node node = element.getFirstChild(); node != null; node = node.getNextSibling()) {

            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            parse(element, (Element) node);
        }
    }

    /**
     * Процесс парсинга хмл файла.
     *
     * @param parent  родительский элемент.
     * @param element текущий элемент.
     */
    protected void handle(final Element parent, final Element element) {
    }

    /**
     * @param stream контент для парса.
     */
    protected void setStream(final InputStream stream) {
        this.stream = stream;
    }
}
