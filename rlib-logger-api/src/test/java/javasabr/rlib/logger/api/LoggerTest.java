package javasabr.rlib.logger.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoggerTest {

  @Test
  @DisplayName("should print all debug methods")
  void shouldPrintAllDebugMethods() {
    // given:
    var messages = new ArrayList<String>();
    var expectedMessages = List.of(
        "DEBUG_msg1",
        "DEBUG_msg2[str1]",
        "DEBUG_msg3[13]",
        "DEBUG_msg4[str2, 15]",
        "DEBUG_msg5[14, str3]",
        "DEBUG_msg6[22, 32]",
        "DEBUG_msg7[str4, str5]",
        "DEBUG_msg8[str5, str6, str7]",
        "DEBUG_msg9[str8, 54, str9]",
        "DEBUG_msg10[str10, 37, 76]",
        "DEBUG_msg11[str11, str12, str13, str14]",
        "DEBUG_msg12[str15, str16, 32]");

    Logger logger = new Logger() {

      @Override
      public boolean enabled(@NonNull LoggerLevel level) {
        return true;
      }

      @Override
      public void print(@NonNull LoggerLevel level, @NonNull String message) {
        messages.add(level + "_" + message);
      }

      @Override
      public void print(@NonNull LoggerLevel level, @NonNull Throwable exception) {
      }
    };

    // when:
    logger.debug("msg1");
    logger.debug("str1", "msg2[%s]"::formatted);
    logger.debug(13, "msg3[%d]"::formatted);
    logger.debug("str2", 15, "msg4[%s, %d]"::formatted);
    logger.debug(14, "str3", "msg5[%d, %s]"::formatted);
    logger.debug(22, 32, "msg6[%d, %d]"::formatted);
    logger.debug("str4", "str5", "msg7[%s, %s]"::formatted);
    logger.debug("str5", "str6", "str7", "msg8[%s, %s, %s]"::formatted);
    logger.debug("str8", 54, "str9", "msg9[%s, %d, %s]"::formatted);
    logger.debug("str10", 37, 76, "msg10[%s, %d, %d]"::formatted);
    logger.debug("str11", "str12", "str13", "str14", "msg11[%s, %s, %s, %s]"::formatted);
    logger.debug("str15", "str16", 32, "msg12[%s, %s, %d]"::formatted);

    // then:
    assertThat(messages.size()).isEqualTo(12);
    assertThat(messages).isEqualTo(expectedMessages);
  }

  @Test
  @DisplayName("should print all info methods")
  void shouldPrintAllInfoMethods() {
    // given:
    var messages = new ArrayList<String>();
    var expectedMessages = List.of(
        "INFO_msg1",
        "INFO_msg2[str1]",
        "INFO_msg3[13]",
        "INFO_msg4[str2, 15]",
        "INFO_msg5[14, str3]",
        "INFO_msg6[22, 32]",
        "INFO_msg7[str4, str5]",
        "INFO_msg8[str5, str6, str7]",
        "INFO_msg9[str8, 54, str9]",
        "INFO_msg10[str10, 37, 76]",
        "INFO_msg11[str11, str12, str13, str14]");

    Logger logger = new Logger() {

      @Override
      public boolean enabled(@NonNull LoggerLevel level) {
        return true;
      }

      @Override
      public void print(@NonNull LoggerLevel level, @NonNull String message) {
        messages.add(level + "_" + message);
      }

      @Override
      public void print(@NonNull LoggerLevel level, @NonNull Throwable exception) {
      }
    };

    // when:
    logger.info("msg1");
    logger.info("str1", "msg2[%s]"::formatted);
    logger.info(13, "msg3[%d]"::formatted);
    logger.info("str2", 15, "msg4[%s, %d]"::formatted);
    logger.info(14, "str3", "msg5[%d, %s]"::formatted);
    logger.info(22, 32, "msg6[%d, %d]"::formatted);
    logger.info("str4", "str5", "msg7[%s, %s]"::formatted);
    logger.info("str5", "str6", "str7", "msg8[%s, %s, %s]"::formatted);
    logger.info("str8", 54, "str9", "msg9[%s, %d, %s]"::formatted);
    logger.info("str10", 37, 76, "msg10[%s, %d, %d]"::formatted);
    logger.info("str11", "str12", "str13", "str14", "msg11[%s, %s, %s, %s]"::formatted);

    // then:
    assertThat(messages.size()).isEqualTo(11);
    assertThat(messages).isEqualTo(expectedMessages);
  }

  @Test
  @DisplayName("should print all warning methods")
  void shouldPrintAllWarningMethods() {
    // given:
    var messages = new ArrayList<String>();
    var expectedMessages = List.of(
        "WARN_msg1",
        "WARN_msg2[str1]",
        "WARN_msg3[13]",
        "WARN_msg4[str2, 15]",
        "WARN_msg5[14, str3]",
        "WARN_msg6[22, 32]",
        "WARN_msg7[str4, str5]",
        "WARN_msg8[str5, str6, str7]",
        "WARN_msg9[str8, 54, str9]",
        "WARN_msg10[str10, 37, 76]",
        "WARN_msg11[str11, str12, str13, str14]",
        "WARN_msg12[str15, str16, 22]");

    Logger logger = new Logger() {

      @Override
      public boolean enabled(@NonNull LoggerLevel level) {
        return true;
      }

      @Override
      public void print(@NonNull LoggerLevel level, @NonNull String message) {
        messages.add(level + "_" + message);
      }

      @Override
      public void print(@NonNull LoggerLevel level, @NonNull Throwable exception) {
      }
    };

    // when:
    logger.warn("msg1");
    logger.warn("str1", "msg2[%s]"::formatted);
    logger.warn(13, "msg3[%d]"::formatted);
    logger.warn("str2", 15, "msg4[%s, %d]"::formatted);
    logger.warn(14, "str3", "msg5[%d, %s]"::formatted);
    logger.warn(22, 32, "msg6[%d, %d]"::formatted);
    logger.warn("str4", "str5", "msg7[%s, %s]"::formatted);
    logger.warn("str5", "str6", "str7", "msg8[%s, %s, %s]"::formatted);
    logger.warn("str8", 54, "str9", "msg9[%s, %d, %s]"::formatted);
    logger.warn("str10", 37, 76, "msg10[%s, %d, %d]"::formatted);
    logger.warn("str11", "str12", "str13", "str14", "msg11[%s, %s, %s, %s]"::formatted);
    logger.warn("str15", "str16", 22, "msg12[%s, %s, %d]"::formatted);

    // then:
    assertThat(messages.size()).isEqualTo(12);
    assertThat(messages).isEqualTo(expectedMessages);
  }

  @Test
  @DisplayName("should print all error methods")
  void shouldPrintAllErrorMethods() {
    // given:
    var messages = new ArrayList<String>();
    var expectedMessages = List.of(
        "ERROR_msg1",
        "ERROR_msg2[str1]",
        "ERROR_msg3[13]",
        "ERROR_msg4[str2, 15]",
        "ERROR_msg5[14, str3]",
        "ERROR_msg6[22, 32]",
        "ERROR_msg7[str4, str5]",
        "ERROR_msg8[str5, str6, str7]",
        "ERROR_msg9[str8, 54, str9]",
        "ERROR_msg10[str10, 37, 76]",
        "ERROR_msg11[str11, str12, str13, str14]");

    Logger logger = new Logger() {

      @Override
      public boolean enabled(@NonNull LoggerLevel level) {
        return true;
      }

      @Override
      public void print(@NonNull LoggerLevel level, @NonNull String message) {
        messages.add(level + "_" + message);
      }

      @Override
      public void print(@NonNull LoggerLevel level, @NonNull Throwable exception) {
      }
    };

    // when:
    logger.error("msg1");
    logger.error("str1", "msg2[%s]"::formatted);
    logger.error(13, "msg3[%d]"::formatted);
    logger.error("str2", 15, "msg4[%s, %d]"::formatted);
    logger.error(14, "str3", "msg5[%d, %s]"::formatted);
    logger.error(22, 32, "msg6[%d, %d]"::formatted);
    logger.error("str4", "str5", "msg7[%s, %s]"::formatted);
    logger.error("str5", "str6", "str7", "msg8[%s, %s, %s]"::formatted);
    logger.error("str8", 54, "str9", "msg9[%s, %d, %s]"::formatted);
    logger.error("str10", 37, 76, "msg10[%s, %d, %d]"::formatted);
    logger.error("str11", "str12", "str13", "str14", "msg11[%s, %s, %s, %s]"::formatted);

    // then:
    assertThat(messages.size()).isEqualTo(11);
    assertThat(messages).isEqualTo(expectedMessages);
  }
}
