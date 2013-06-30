package rlib.data;

/**
 * Интерфейс для реализации парсера хмл.
 * 
 * @author Ronn
 */
public interface DocumentXML<C>
{
	/**
	 * Парс файла с получением результата.
	 */
	public C parse();
}
