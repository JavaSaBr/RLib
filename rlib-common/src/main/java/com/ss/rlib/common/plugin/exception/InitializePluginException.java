package com.ss.rlib.common.plugin.exception;

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

    public InitializePluginException(@NotNull String message, @NotNull Path path) {
        super(message);
        this.path = path;
    }

    public InitializePluginException(
            @NotNull String message,
            @NotNull Path path,
            @NotNull Throwable e
    ) {
        super(message, e);
        this.path = path;
    }

    /**
     * Get the path of the plugin.
     *
     * @return the path of the plugin.
     */
    public @NotNull Path getPath() {
        return path;
    }
}
