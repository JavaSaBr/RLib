package rlib.data;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.util.IOUtils;

/**
 * Базовая модель для парсера данных с xml файла.
 * 
 * @author Ronn
 */
public abstract class AbstractStreamDocument<C> implements DocumentXML<C> {

	protected static final Logger LOGGER = Loggers.getLogger(DocumentXML.class);

	protected static final ThreadLocal<DocumentBuilderFactory> LOCAL_FACTORY = new ThreadLocal<DocumentBuilderFactory>() {

		@Override
		protected DocumentBuilderFactory initialValue() {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			return factory;
		};
	};

	/** поток файла */
	protected InputStream stream;
	/** отпарсенный xml фаил */
	protected Document document;
	/** конечные данные */
	protected C result;

	public AbstractStreamDocument() {
	}

	public AbstractStreamDocument(InputStream stream) {
		this.stream = stream;
	}

	/**
	 * @param stream контент для парса.
	 */
	protected void setStream(InputStream stream) {
		this.stream = stream;
	}

	/**
	 * Создание соответствующей коллекцией, в которой будет хранится результат.
	 */
	protected abstract C create();

	@Override
	public C parse() {

		DocumentBuilderFactory factory = LOCAL_FACTORY.get();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(stream);
		} catch(SAXException | IOException | ParserConfigurationException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.close(stream);
		}

		result = create();

		try {
			parse(document);
		} catch(Exception e) {
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
}
