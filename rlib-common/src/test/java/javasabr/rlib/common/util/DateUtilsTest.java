package javasabr.rlib.common.util;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DateUtilsTest {

    @Test
    void stringToLocalDateTest() {

        Assertions.assertEquals(
            LocalDate.of(1800, 5, 20),
            DateUtils.toLocalDate("1800-05-20")
        );

        Assertions.assertNull(DateUtils.toLocalDate("2020-1-10"));
        Assertions.assertEquals(
            LocalDate.of(2020, 1, 10),
            DateUtils.toLocalDate("2020-01-10")
        );

        Assertions.assertNull(DateUtils.toLocalDate("2015-5-1"));
        Assertions.assertEquals(
            LocalDate.of(2015, 5, 1),
            DateUtils.toLocalDate("2015-05-01")
        );

        Assertions.assertNull(DateUtils.toLocalDate("invaliddate"));
    }

    @Test
    void localDateToStringTest() {

        Assertions.assertEquals(
            "1800-05-20",
            DateUtils.toString(LocalDate.of(1800, 5, 20))
        );

        Assertions.assertEquals(
            "2020-01-10",
            DateUtils.toString(LocalDate.of(2020, 1, 10))
        );

        Assertions.assertEquals(
            "2015-05-01",
            DateUtils.toString(LocalDate.of(2015, 5, 1))
        );

        Assertions.assertNull(DateUtils.toString(null));
    }

    @Test
    void temporalAccessorToStringTest() {

        Assertions.assertEquals(
            "1800-05-20",
            DateUtils.toString(LocalDate.of(1800, 5, 20),  ISO_LOCAL_DATE)
        );

        Assertions.assertEquals(
            "2020-01-10T23:42:00",
            DateUtils.toString(
                LocalDateTime.of(2020, 1, 10, 23, 42),
                ISO_LOCAL_DATE_TIME
            )
        );

        Assertions.assertNull(DateUtils.toString(null, ISO_LOCAL_DATE));
    }

    @Test
    void formatTimestampTest() {

        var localDateTime = LocalDateTime.of(
            2010,
            5,
            12,
            23,
            10,
            35,
            0
        );

        Assertions.assertEquals("23:10:35:000", DateUtils.formatShortTimestamp(localDateTime));
        Assertions.assertEquals("23:10:35:000", DateUtils.formatShortTimestamp(localDateTime
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli())
        );

        var zonedDateTime = ZonedDateTime.of(
            2010,
            5,
            12,
            23,
            10,
            35,
            0,
            ZoneOffset.ofHours(3)
        );

        Assertions.assertEquals("23:10:35:000", DateUtils.formatShortTimestamp(zonedDateTime));
        Assertions.assertEquals("23:10:35:000", DateUtils.formatShortTimestamp(zonedDateTime
            .toLocalDateTime()
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli())
        );
    }
}
