package com.ss.rlib.common.util;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * The class with utility methods to work with dates and times.
 *
 * @author JavaSaBr
 */
public class DateUtils {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
        DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    /**
     * Format a time to a string by pattern HH:mm:ss:SSS
     *
     * @param timestamp the timestamp.
     * @return the string presentation.
     * @since 9.3.0
     */
    public static @NotNull String formatShortTimestamp(long timestamp) {
        return TIMESTAMP_FORMATTER.format(LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timestamp),
            ZoneOffset.UTC
        ));
    }

    /**
     * Format some temporal accessor to a string by pattern HH:mm:ss:SSS
     *
     * @param temporal the timestamp.
     * @return the string presentation.
     * @since 9.3.0
     */
    public static @NotNull String formatShortTimestamp(@NotNull TemporalAccessor temporal) {
        return TIMESTAMP_FORMATTER.format(temporal);
    }

    /**
     * Convert a date string to a {@link LocalDate}.
     *
     * @param string the string to convert.
     * @return the local date or null if this string cannot be converted.
     * @since 9.3.0
     */
    public static @Nullable LocalDate toLocalDate(@Nullable String string) {
        if (StringUtils.isEmpty(string)) {
            return null;
        } else {
            return Utils.tryGetAndConvert(string, ISO_LOCAL_DATE::parse, LocalDate::from);
        }
    }

    /**
     * Convert a local date to a string by ISO_LOCAL_DATE formatter.
     *
     * @param localDate the local date.
     * @return the string presentation of the local date or null.
     * @since 9.3.0
     */
    public static @Nullable String toString(@Nullable LocalDate localDate) {
        if (localDate == null) {
            return null;
        } else {
            return ISO_LOCAL_DATE.format(localDate);
        }
    }

    /**
     * Convert a temporal accessor to a string by passed formatter.
     *
     * @param temporalAccessor the temporal accessor.
     * @param formatter        the formatter.
     * @return the string presentation or null.
     * @since 9.3.0
     */
    public static @Nullable String toString(
        @Nullable TemporalAccessor temporalAccessor,
        @NotNull DateTimeFormatter formatter
    ) {
        if (temporalAccessor == null) {
            return null;
        } else {
            return formatter.format(temporalAccessor);
        }
    }
}
