package rlib.logging.impl;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import rlib.logging.LoggerListener;

/**
 * Реализация слушателя логирования с записью в создаваемый файл в указанной папке.
 *
 * @author JavaSaBr
 */
public class FolderFileListener implements LoggerListener {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyy-MM-dd_HH-mm-ss");

    /**
     * Ссылка на папку, где нужно создать фаил для лога.
     */
    private final Path folder;

    /**
     * Записчик лога в фаил.
     */
    private Writer writer;

    public FolderFileListener(final Path folder) {

        if (!Files.isDirectory(folder)) {
            throw new IllegalArgumentException("file is not directory.");
        }

        if (!Files.exists(folder)) {
            try {
                Files.createDirectories(folder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.folder = folder;

    }

    /**
     * @return записчик в фаил.
     */
    public Writer getWriter() throws IOException {

        if (writer == null) {

            final LocalDateTime dateTime = LocalDateTime.now();
            final String filename = TIME_FORMATTER.format(dateTime) + ".log";

            writer = Files.newBufferedWriter(folder.resolve(filename), Charset.forName("UTF-8"));
        }

        return writer;
    }

    @Override
    public void println(final String text) {
        try {
            final Writer writer = getWriter();
            writer.append(text);
            writer.append('\n');
            writer.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
