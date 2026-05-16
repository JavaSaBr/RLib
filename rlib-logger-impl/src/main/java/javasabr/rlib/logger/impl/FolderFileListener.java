package javasabr.rlib.logger.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javasabr.rlib.logger.api.LoggerListener;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
public class FolderFileListener implements LoggerListener {

  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyy-MM-dd_HH-mm-ss");

  final Path folder;
  
  @Nullable
  volatile Writer writer;

  public FolderFileListener(Path folder) {
    if (!Files.isDirectory(folder)) {
      throw new IllegalArgumentException("File:[%s] is not directory".formatted(folder));
    }
    if (!Files.exists(folder)) {
      try {
        Files.createDirectories(folder);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }
    this.folder = folder;
  }

  public Writer getOrCreateWriter() throws IOException {
    var local = writer;
    if (local == null) {
      synchronized (this) {
        local = writer;
        if (local == null) {
          var dateTime = LocalDateTime.now();
          var filename = TIME_FORMATTER.format(dateTime) + ".log";
          local = Files.newBufferedWriter(folder.resolve(filename), StandardCharsets.UTF_8);
          this.writer = local;
          return local;
        }
      }
    }
    return local;
  }

  @Override
  public void println(String text) {
    try {
      var writer = getOrCreateWriter();
      writer.append(text);
      writer.append('\n');
      writer.flush();
    } catch (IOException exception) {
      //noinspection CallToPrintStackTrace
      exception.printStackTrace();
    }
  }
}
