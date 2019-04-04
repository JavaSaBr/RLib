package com.ss.rlib.logger.api.impl;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class NullLogger implements Logger {

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull Object owner, @NotNull String message) {
    }

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull String message) {
    }

    @Override
    public void print(
        @NotNull LoggerLevel level,
        @NotNull Object owner,
        @NotNull Throwable exception
    ) {
    }

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull Throwable exception) {
    }

    @Override
    public <T> void print(
        @NotNull LoggerLevel level,
        @NotNull Object owner,
        @Nullable T arg,
        @NotNull Logger.SinFactory<T> messageFactory
    ) {
    }

    @Override
    public <F, S> void print(
        @NotNull LoggerLevel level,
        @NotNull Object owner,
        @Nullable F first,
        @Nullable S second,
        @NotNull Logger.BiFactory<F, S> messageFactory
    ) {

    }

    @Override
    public <F, S, T> void print(
        @NotNull LoggerLevel level,
        @NotNull Object owner,
        @Nullable F first,
        @Nullable S second,
        @Nullable T third,
        @NotNull Logger.TriFactory<F, S, T> messageFactory
    ) {
    }

    @Override
    public <T> void print(@NotNull LoggerLevel level, @Nullable T arg, @NotNull Logger.SinFactory<T> messageFactory) {
    }

    @Override
    public <F, S> void print(
        @NotNull LoggerLevel level,
        @Nullable F first,
        @Nullable S second,
        @NotNull Logger.BiFactory<F, S> messageFactory
    ) {
    }

    @Override
    public <F, S, T> void print(
        @NotNull LoggerLevel level,
        @Nullable F first,
        @Nullable S second,
        @Nullable T third,
        @NotNull Logger.TriFactory<F, S, T> messageFactory
    ) {
    }
}
