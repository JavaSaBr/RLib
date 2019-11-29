package com.ss.rlib.logger.slf4j;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class Slf4jLogger implements Logger {

    private final org.slf4j.Logger logger;

    @Override
    public boolean isEnabled(@NotNull LoggerLevel level) {

        switch (level) {
            case INFO:
                return logger.isInfoEnabled();
            case DEBUG:
                return logger.isDebugEnabled();
            case ERROR:
                return logger.isErrorEnabled();
            case WARNING:
                return logger.isWarnEnabled();
        }

        return false;
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
}
