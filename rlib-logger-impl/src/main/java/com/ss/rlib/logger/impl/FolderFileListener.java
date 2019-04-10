package com.ss.rlib.logger.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.ss.rlib.logger.api.LoggerListener;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of a logger listener to save log to files in a directory.
 *
 * @author JavaSaBr
 */
public class FolderFileListener implements LoggerListener {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyy-MM-dd_HH-mm-ss");

    /**
     * The folder with log files.
     */
    @NotNull
    private final Path folder;

    /**
     * The current writer.
     */
    private Writer writer;

    public FolderFileListener(@NotNull Path folder) {

        if (!Files.isDirectory(folder)) {
            throw new IllegalArgumentException("file is not directory.");
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

    /**
     * Get or create a writer.
     *
     * @return the writer.
     * @throws IOException the io exception
     */
    public @NotNull Writer getWriter() throws IOException {

        if (writer == null) {

            var dateTime = LocalDateTime.now();
            var filename = TIME_FORMATTER.format(dateTime) + ".log";

            writer = Files.newBufferedWriter(folder.resolve(filename), Charset.forName("UTF-8"));
        }

        return writer;
    }

    @Override
    public void println(@NotNull String text) {
        try {
            var writer = getWriter();
            writer.append(text);
            writer.append('\n');
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
