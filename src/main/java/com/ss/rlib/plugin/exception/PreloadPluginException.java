package com.ss.rlib.plugin.exception;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * The exception about problems with preloading plugin.
 */
public class PreloadPluginException extends PluginException {

    @NotNull
    private final Path path;

    public PreloadPluginException(@NotNull final String message, @NotNull final Path path) {
        super(message);
        this.path = path;
    }

    /**
     * @return the path of the plugin.
     */
    @NotNull
    public Path getPath() {
        return path;
    }
}
