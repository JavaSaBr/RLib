package rlib.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Базовая модель для парсера данных с xml файла.
 * 
 * @author Ronn
 */
public abstract class AbstractFileDocument<C> extends AbstractStreamDocument<C> {

	public AbstractFileDocument(final File file) {
		try {
			setStream(new FileInputStream(file));
		} catch(final FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
