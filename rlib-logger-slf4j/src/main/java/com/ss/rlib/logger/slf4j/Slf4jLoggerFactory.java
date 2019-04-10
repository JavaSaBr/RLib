package com.ss.rlib.logger.slf4j;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerFactory;
import com.ss.rlib.logger.api.LoggerListener;
import org.jetbrains.annotations.NotNull;

import java.io.Writer;

public class Slf4jLoggerFactory implements LoggerFactory {

    private final Logger logger;

    public Slf4jLoggerFactory() {
        this.logger = new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(""));
    }

    @Override
    public @NotNull Logger make(@NotNull String name) {
        return new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(name));
    }

    @Override
    public @NotNull Logger make(@NotNull Class<?> type) {
        return new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(type));
    }

    @Override
    public @NotNull Logger getDefault() {
        return logger;
    }

    @Override
    public void addListener(@NotNull LoggerListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addWriter(@NotNull Writer writer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeListener(@NotNull LoggerListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeWriter(@NotNull Writer writer) {
        throw new UnsupportedOperationException();
    }
}
