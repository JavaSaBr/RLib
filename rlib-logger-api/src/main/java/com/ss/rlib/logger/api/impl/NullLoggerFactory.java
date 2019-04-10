package com.ss.rlib.logger.api.impl;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerFactory;
import com.ss.rlib.logger.api.LoggerListener;
import org.jetbrains.annotations.NotNull;

import java.io.Writer;

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
