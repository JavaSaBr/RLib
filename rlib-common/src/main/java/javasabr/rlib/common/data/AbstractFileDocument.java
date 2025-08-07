package javasabr.rlib.common.data;

import static java.nio.file.Files.newInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.jspecify.annotations.NullMarked;

/**
 * The file implementation of the parser of xml documents.
 *
 * @param <C> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public abstract class AbstractFileDocument<C> extends AbstractStreamDocument<C> {

  /**
   * The file path.
   */
  protected final String filePath;

  public AbstractFileDocument(File file) {
    this.filePath = file.getAbsolutePath();
    try {
      setStream(new FileInputStream(file));
    } catch (FileNotFoundException e) {
      throw new UncheckedIOException(e);
    }
  }

  public AbstractFileDocument(Path path) {
    this.filePath = path
        .toAbsolutePath()
        .toString();
    try {
      setStream(newInputStream(path, StandardOpenOption.READ));
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Get the file path.
   *
   * @return the file path.
   */
  protected String getFilePath() {
    return filePath;
  }
}
