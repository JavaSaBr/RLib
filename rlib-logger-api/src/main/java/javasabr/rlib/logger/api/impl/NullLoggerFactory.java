package javasabr.rlib.logger.api.impl;

import java.io.Writer;
import javasabr.rlib.logger.api.Logger;
import javasabr.rlib.logger.api.LoggerFactory;
import javasabr.rlib.logger.api.LoggerListener;
import org.jetbrains.annotations.NotNull;

public class NullLoggerFactory implements LoggerFactory {

    private static final NullLogger NULL_LOGGER = new NullLogger();

    @Override
    public @NotNull Logger make(@NotNull String name) {
        return NULL_LOGGER;
    }

    @Override
    public @NotNull Logger make(@NotNull Class<?> type) {
        return NULL_LOGGER;
    }

    @Override
    public @NotNull Logger getDefault() {
        return NULL_LOGGER;
    }

    @Override
    public void addListener(@NotNull LoggerListener listener) {
    }

    @Override
    public void addWriter(@NotNull Writer writer) {
    }

    @Override
    public void removeListener(@NotNull LoggerListener listener) {
    }

    @Override
    public void removeWriter(@NotNull Writer writer) {
    }
}
