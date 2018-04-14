package com.ss.rlib.common.plugin.exception;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * The exception about problems with pre-loading plugins.
 *
 * @author JavaSaBr
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
    public @NotNull Path getPath() {
        return path;
    }
}
