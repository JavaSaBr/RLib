package javasabr.rlib.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collection;
import java.util.Set;
import javasabr.rlib.common.AliasedEnum;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

class AliasedEnumMapTest {

  public enum TestEnum implements AliasedEnum<@NonNull TestEnum> {
    CONSTANT1(Set.of("cnst1", "constant1", "CONSTANT1")),
    CONSTANT2(Set.of("cnst2", "constant2", "CONSTANT2")),
    CONSTANT3(Set.of("cnst3", "constant3", "CONSTANT3")),
    CONSTANT4(Set.of("cnst4", "constant4", "CONSTANT4")),
    CONSTANT5(Set.of("cnst5", "constant5", "CONSTANT5"));

    private static final AliasedEnumMap<TestEnum> MAP = new AliasedEnumMap<>(TestEnum.class);

    private final Collection<String> aliases;

    TestEnum(Collection<String> aliases) {
      this.aliases = aliases;
    }

    @Override
    public Collection<String> aliases() {
      return aliases;
    }
  }

  @Test
  void shouldResolveEnumByAlias() {
    // when\then:
    assertThat(TestEnum.MAP.resolve("cnst1")).isEqualTo(TestEnum.CONSTANT1);
    assertThat(TestEnum.MAP.resolve("constant1")).isEqualTo(TestEnum.CONSTANT1);
    assertThat(TestEnum.MAP.resolve("CONSTANT1")).isEqualTo(TestEnum.CONSTANT1);
    assertThat(TestEnum.MAP.resolve("cnst2")).isEqualTo(TestEnum.CONSTANT2);
    assertThat(TestEnum.MAP.resolve("CONSTANT2")).isEqualTo(TestEnum.CONSTANT2);
    assertThat(TestEnum.MAP.resolve("constant3")).isEqualTo(TestEnum.CONSTANT3);
    assertThat(TestEnum.MAP.resolve("CONSTANT4")).isEqualTo(TestEnum.CONSTANT4);
    assertThat(TestEnum.MAP.resolve("unknown")).isNull();
    assertThat(TestEnum.MAP.resolve("")).isNull();
    assertThat(TestEnum.MAP.resolve("unknown", TestEnum.CONSTANT4)).isEqualTo(TestEnum.CONSTANT4);
  }

  @Test
  void shouldRequireEnumByAlias() {
    // when\then:
    assertThat(TestEnum.MAP.require("cnst1")).isEqualTo(TestEnum.CONSTANT1);
    assertThat(TestEnum.MAP.require("CONSTANT2")).isEqualTo(TestEnum.CONSTANT2);
    assertThat(TestEnum.MAP.require("cnst3")).isEqualTo(TestEnum.CONSTANT3);
    assertThat(TestEnum.MAP.require("constant4")).isEqualTo(TestEnum.CONSTANT4);
    assertThatThrownBy(() -> TestEnum.MAP.require("unknown"))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> TestEnum.MAP.require(""))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
