package javasabr.rlib.common.util;

import javasabr.rlib.common.util.os.OperatingSystem;
import javasabr.rlib.common.util.os.OperatingSystemResolver;
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
}
