package javasabr.rlib.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Tests of {@link Utils} methods.
 *
 * @author JavaSaBr
 */
public class UtilsTest {

  @Test
  void shouldSafetyTryGet() {
    // when/then:
    Integer number = Utils.tryGet("15", Integer::valueOf);
    assertThat(number).isEqualTo(Integer.valueOf(15));

    // when/then:
    Integer invalidNumber = Utils.tryGet("invalidnumber", Integer::valueOf);
    assertThat(invalidNumber).isNull();
    
    // when/then:
    assertThat(Utils.tryGet("15", Integer::valueOf, 2))
        .isEqualTo(Integer.valueOf(15));
    assertThat(Utils.tryGet("invalidnumber", Integer::valueOf, 2))
        .isEqualTo(Integer.valueOf(2));
  }

  @Test
  void shouldSafetyTryGetAndConvert() {
    assertThat(Utils.tryGetAndConvert("15", Integer::valueOf, Object::toString))
        .isEqualTo("15");
    assertThat(Utils.tryGetAndConvert("invalidnumber", Integer::valueOf, Object::toString))
        .isNull();
  }
}
