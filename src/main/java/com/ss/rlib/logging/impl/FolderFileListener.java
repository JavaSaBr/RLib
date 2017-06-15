package com.ss.rlib.logging.impl;

import com.ss.rlib.logging.LoggerListener;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    /**
     * Instantiates a new Folder file listener.
     *
     * @param folder the folder
     */
    public FolderFileListener(@NotNull final Path folder) {

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
     * Get or create a writer.
     *
     * @return the writer.
     * @throws IOException the io exception
     */
    @NotNull
    public Writer getWriter() throws IOException {

        if (writer == null) {

            final LocalDateTime dateTime = LocalDateTime.now();
            final String filename = TIME_FORMATTER.format(dateTime) + ".log";

            writer = Files.newBufferedWriter(folder.resolve(filename), Charset.forName("UTF-8"));
        }

        return writer;
    }

    @Override
    public void println(@NotNull final String text) {
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
