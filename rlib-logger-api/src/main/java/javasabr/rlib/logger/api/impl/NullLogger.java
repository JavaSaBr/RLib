package javasabr.rlib.logger.api.impl;

import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerLevel;
import org.jetbrains.annotations.NotNull;

public final class NullLogger implements Logger {

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull String message) {

    }

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull Throwable exception) {

    }
}
