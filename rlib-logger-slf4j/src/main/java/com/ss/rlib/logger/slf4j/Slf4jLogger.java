package com.ss.rlib.logger.slf4j;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Slf4jLogger implements Logger {

    private final org.slf4j.Logger logger;

    public Slf4jLogger(@NotNull org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull Object owner, @NotNull String message) {
        print(level, message);
    }

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull String message) {

        switch (level) {
            case INFO:
                logger.info(message);
                return;
            case DEBUG:
                logger.debug(message);
                return;
            case ERROR:
                logger.error(message);
                return;
            case WARNING:
                logger.warn(message);
        }
    }

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull Object owner, @NotNull Throwable exception) {
        print(level, exception);
    }

    @Override
    public void print(@NotNull LoggerLevel level, @NotNull Throwable exception) {

        switch (level) {
            case INFO:
                logger.info(exception.getMessage(), exception);
                return;
            case DEBUG:
                logger.debug(exception.getMessage(), exception);
                return;
            case ERROR:
                logger.error(exception.getMessage(), exception);
                return;
            case WARNING:
                logger.warn(exception.getMessage(), exception);
        }
    }

    @Override
    public <T> void print(
        @NotNull LoggerLevel level,
        @NotNull Object owner,
        @Nullable T arg,
        @NotNull SinFactory<T> messageFactory
    ) {
        print(level, arg, messageFactory);
    }

    @Override
    public <F, S> void print(
        @NotNull LoggerLevel level,
        @NotNull Object owner,
        @Nullable F first,
        @Nullable S second,
        @NotNull BiFactory<F, S> messageFactory
    ) {
        print(level, first, second, messageFactory);
    }

    @Override
    public <F, S, T> void print(
        @NotNull LoggerLevel level,
        @NotNull Object owner,
        @Nullable F first,
        @Nullable S second,
        @Nullable T third,
        @NotNull TriFactory<F, S, T> messageFactory
    ) {
        print(level, first, second, third, messageFactory);
    }

    @Override
    public <T> void print(@NotNull LoggerLevel level, @Nullable T arg, @NotNull SinFactory<T> messageFactory) {

        switch (level) {
            case INFO:
                if (logger.isInfoEnabled()) {
                    logger.info(messageFactory.make(arg));
                }
                return;
            case DEBUG:
                if (logger.isDebugEnabled()) {
                    logger.debug(messageFactory.make(arg));
                }
                return;
            case ERROR:
                if (logger.isErrorEnabled()) {
                    logger.error(messageFactory.make(arg));
                }
                return;
            case WARNING:
                if (logger.isWarnEnabled()) {
                    logger.warn(messageFactory.make(arg));
                }
        }
    }

    @Override
    public <F, S> void print(
        @NotNull LoggerLevel level,
        @Nullable F first,
        @Nullable S second,
        @NotNull BiFactory<F, S> messageFactory
    ) {

        switch (level) {
            case INFO:
                if (logger.isInfoEnabled()) {
                    logger.info(messageFactory.make(first, second));
                }
                return;
            case DEBUG:
                if (logger.isDebugEnabled()) {
                    logger.debug(messageFactory.make(first, second));
                }
                return;
            case ERROR:
                if (logger.isErrorEnabled()) {
                    logger.error(messageFactory.make(first, second));
                }
                return;
            case WARNING:
                if (logger.isWarnEnabled()) {
                    logger.warn(messageFactory.make(first, second));
                }
        }
    }

    @Override
    public <F, S, T> void print(
        @NotNull LoggerLevel level,
        @Nullable F first,
        @Nullable S second,
        @Nullable T third,
        @NotNull TriFactory<F, S, T> messageFactory
    ) {

        switch (level) {
            case INFO:
                if (logger.isInfoEnabled()) {
                    logger.info(messageFactory.make(first, second, third));
                }
                return;
            case DEBUG:
                if (logger.isDebugEnabled()) {
                    logger.debug(messageFactory.make(first, second, third));
                }
                return;
            case ERROR:
                if (logger.isErrorEnabled()) {
                    logger.error(messageFactory.make(first, second, third));
                }
                return;
            case WARNING:
                if (logger.isWarnEnabled()) {
                    logger.warn(messageFactory.make(first, second, third));
                }
        }
    }
}
