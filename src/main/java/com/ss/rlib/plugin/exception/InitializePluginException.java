package com.ss.rlib.plugin.exception;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * The exception about problems with initializing plugins.
 *
 * @author JavaSaBr
 */
public class InitializePluginException extends PluginException {

    @NotNull
    private final Path path;

    public InitializePluginException(@NotNull final String message, @NotNull final Path path) {
        super(message);
        this.path = path;
    }

    public InitializePluginException(@NotNull final String message, @NotNull final Path path,
                                     @NotNull final Exception e) {
        super(message, e);
        this.path = path;
    }

    /**
     * @return the path of the plugin.
     */
    public @NotNull Path getPath() {
        return path;
    }
}
