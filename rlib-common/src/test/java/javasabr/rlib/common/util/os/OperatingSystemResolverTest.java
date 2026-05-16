package javasabr.rlib.common.util.os;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OperatingSystemResolverTest {

  @Test
  void shouldResolveOperatingSystem() {
    // given:
    var resolver = new OperatingSystemResolver();

    //when:
    OperatingSystem system = resolver.resolve();

    // then:
    Assertions
        .assertThat(system)
        .isNotNull()
        .hasNoNullFieldsOrProperties();
  }
  
  @Test
  void shouldNormalizeMacVersion10WithPatchPart() {
    // given:
    String version = "10.15.7";

    // when:
    String normalized = OperatingSystemResolver.normalizeMacVersion(version);

    // then:
    assertThat(normalized)
        .isEqualTo("10.15");
  }

  @Test
  void shouldNormalizeMacVersion11PlusToMajor() {
    // given:
    String version = "15.1";

    // when:
    String normalized = OperatingSystemResolver.normalizeMacVersion(version);

    // then:
    assertThat(normalized)
        .isEqualTo("15");
  }

  @Test
  void shouldKeepLegacyMacVersionAsIs() {
    // given:
    String version = "9.2.1";

    // when:
    String normalized = OperatingSystemResolver.normalizeMacVersion(version);

    // then:
    assertThat(normalized)
        .isEqualTo("9.2.1");
  }

  @Test
  void shouldReturnNullForInvalidVersion() {
    // given:
    String version = "unknown";

    // when:
    String normalized = OperatingSystemResolver.normalizeMacVersion(version);

    // then:
    assertThat(normalized)
        .isNull();
  }
}
