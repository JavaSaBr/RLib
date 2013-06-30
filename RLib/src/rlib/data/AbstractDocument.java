package rlib.data;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import rlib.logging.Logger;
import rlib.logging.Loggers;


/**
 * Базовая модель для парсера данных с xml файла.
 * 
 * @author Ronn
 */
public abstract class AbstractDocument<C> implements DocumentXML<C>
{
	protected static final Logger log = Loggers.getLogger("DocumentXML");
	
	/** фаил с которого парсим */
	protected File file;
	
	/** отпарсенный xml фаил */
	protected Document doc;
	
	/** конечные данные */
	protected C result;
	
	/**
	 * @param file фаил, который будет парсить парсер.
	 */
	public AbstractDocument(File file)
	{
		this.file = file;
	}
	
	/**
	 * Создание соответствующей коллекцией, в которой будет хранится результат.
	 */
	protected abstract C create();

	@Override
	public C parse()
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			doc = factory.newDocumentBuilder().parse(file);
		}
		catch(SAXException | IOException | ParserConfigurationException e)
		{
			log.warning(this, "incorrect file " + file);
			log.warning(this, e);
		}
	
		result = create();
		
		try
		{
			parse(doc);
		}
		catch(Exception e)
		{
			log.warning(this, "incorrect file " + file);
			log.warning(this, e);
		}
		
		return result;
	}
	
	/**
	 * Процесс парсинга хмл файла.
	 * 
	 * @param doc отпарсенный xml фаил.
	 */
	protected abstract void parse(Document doc);
}
