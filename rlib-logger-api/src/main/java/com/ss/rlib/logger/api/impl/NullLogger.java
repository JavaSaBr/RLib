package com.ss.rlib.logger.api.impl;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class NullLogger implements Logger {

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull String message) {

    }

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull Throwable exception) {

    }
}
