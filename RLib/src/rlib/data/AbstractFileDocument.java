package rlib.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Базовая модель для парсера данных с xml файла.
 *
 * @author Ronn
 */
public abstract class AbstractFileDocument<C> extends AbstractStreamDocument<C> {

    /**
     * Путь к файлу.
     */
    protected final String filePath;

    public AbstractFileDocument(final File file) {
        this.filePath = file.getPath();

        try {
            setStream(new FileInputStream(file));
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractFileDocument(final Path path) {
        this.filePath = path.toString();

        try {
            setStream(Files.newInputStream(path, StandardOpenOption.READ));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return путь к файлу.
     */
    protected String getFilePath() {
        return filePath;
    }
}
