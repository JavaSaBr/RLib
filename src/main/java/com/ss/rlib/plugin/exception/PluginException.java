package com.ss.rlib.plugin.exception;

import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of a plugin exception.
 *
 * @author JavaSaBr
 */
public class PluginException extends RuntimeException {

    public PluginException(@NotNull final String message) {
        super(message);
    }
}
