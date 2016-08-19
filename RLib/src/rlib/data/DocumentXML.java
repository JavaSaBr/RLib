package rlib.data;

/**
 * Интерфейс для реализации парсера хмл.
 *
 * @author JavaSaBr
 */
public interface DocumentXML<C> {

    /**
     * Парс файла с получением результата.
     */
    public C parse();
}
