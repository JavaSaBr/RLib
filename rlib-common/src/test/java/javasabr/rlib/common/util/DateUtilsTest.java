package javasabr.rlib.common.util;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

public class DateUtilsTest {

  @Test
  void stringToLocalDateTest() {
    assertThat(DateUtils.toLocalDate("1800-05-20"))
        .isEqualTo(LocalDate.of(1800, 5, 20));

    assertThat(DateUtils.toLocalDate("2020-1-10"))
        .isNull();
    assertThat(DateUtils.toLocalDate("2020-01-10"))
        .isEqualTo(LocalDate.of(2020, 1, 10));

    assertThat(DateUtils.toLocalDate("2015-5-1"))
        .isNull();
    assertThat(DateUtils.toLocalDate("2015-05-01"))
        .isEqualTo(LocalDate.of(2015, 5, 1));

    assertThat(DateUtils.toLocalDate("invaliddate"))
        .isNull();
  }

  @Test
  void localDateToStringTest() {
    assertThat(DateUtils.toString(LocalDate.of(1800, 5, 20)))
        .isEqualTo("1800-05-20");

    assertThat(DateUtils.toString(LocalDate.of(2020, 1, 10)))
        .isEqualTo("2020-01-10");

    assertThat(DateUtils.toString(LocalDate.of(2015, 5, 1)))
        .isEqualTo("2015-05-01");

    assertThat(DateUtils.toString(null))
        .isNull();
  }

  @Test
  void temporalAccessorToStringTest() {
    assertThat(DateUtils.toString(LocalDate.of(1800, 5, 20), ISO_LOCAL_DATE))
        .isEqualTo("1800-05-20");

    assertThat(DateUtils.toString(LocalDateTime.of(2020, 1, 10, 23, 42), ISO_LOCAL_DATE_TIME))
        .isEqualTo("2020-01-10T23:42:00");

    assertThat(DateUtils.toString(null, ISO_LOCAL_DATE))
        .isNull();
  }

  @Test
  void formatTimestampTest() {
    var localDateTime = LocalDateTime.of(2010, 5, 12, 23, 10, 35, 0);

    assertThat(DateUtils.formatShortTimestamp(localDateTime))
        .isEqualTo("23:10:35:000");
    assertThat(DateUtils.formatShortTimestamp(localDateTime
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli()))
        .isEqualTo("23:10:35:000");

    var zonedDateTime = ZonedDateTime.of(2010, 5, 12, 23, 10, 35, 0, ZoneOffset.ofHours(3));

    assertThat(DateUtils.formatShortTimestamp(zonedDateTime))
        .isEqualTo("23:10:35:000");
    assertThat(DateUtils.formatShortTimestamp(zonedDateTime
            .toLocalDateTime()
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli()))
        .isEqualTo("23:10:35:000");
  }
}
