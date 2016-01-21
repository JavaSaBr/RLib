package rlib.data;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.IOUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Базовая модель для парсера данных с xml файла.
 *
 * @author Ronn
 */
public abstract class AbstractStreamDocument<C> implements DocumentXML<C> {

    protected static final Logger LOGGER = LoggerManager.getLogger(DocumentXML.class);

    protected static final ThreadLocal<DocumentBuilderFactory> LOCAL_FACTORY = new ThreadLocal<DocumentBuilderFactory>() {

        @Override
        protected DocumentBuilderFactory initialValue() {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringComments(true);
            return factory;
        }
    };

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
    protected abstract void parse(Document document);

    /**
     * @param stream контент для парса.
     */
    protected void setStream(final InputStream stream) {
        this.stream = stream;
    }
}
