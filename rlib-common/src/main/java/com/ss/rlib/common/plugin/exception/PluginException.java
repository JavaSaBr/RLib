package com.ss.rlib.common.plugin.exception;

import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of a plugin exception.
 *
 * @author JavaSaBr
 */
public class PluginException extends RuntimeException {

    public PluginException(@NotNull String message) {
        super(message);
    }

    public PluginException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}
