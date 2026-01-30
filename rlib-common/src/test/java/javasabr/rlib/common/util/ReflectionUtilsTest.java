package javasabr.rlib.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ReflectionUtilsTest {

  private static class Type1 {

    private String field1;
    private int field2;

    private static class Inner1 {
      private String field1;
      private int field2;
    }

    private static class Inner2 extends Inner1 {
      private int field3;
      private Object field4;
    }
  }

  private static class Type2 extends Type1 {
    private int field3;
    private Object field4;
  }

  @Test
  void getAllDeclaredFieldsTest() {
    var allFields = ReflectionUtils.getAllDeclaredFields(Type1.class);

    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field1"))
        .findAny()
        .orElse(null))
        .isNotNull();
    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field2"))
        .findAny()
        .orElse(null))
        .isNotNull();
    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field3"))
        .findAny()
        .orElse(null))
        .isNull();

    allFields = ReflectionUtils.getAllDeclaredFields(Type2.class);

    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field1"))
        .findAny()
        .orElse(null))
        .isNotNull();
    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field2"))
        .findAny()
        .orElse(null))
        .isNotNull();
    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field3"))
        .findAny()
        .orElse(null))
        .isNotNull();
    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field4"))
        .findAny()
        .orElse(null))
        .isNotNull();
    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field5"))
        .findAny()
        .orElse(null))
        .isNull();

    allFields = ReflectionUtils.getAllDeclaredFields(Type1.Inner1.class);

    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field1"))
        .findAny()
        .orElse(null))
        .isNotNull();
    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field2")))
        .isNotNull();
    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field3"))
        .findAny()
        .orElse(null))
        .isNull();

    allFields = ReflectionUtils.getAllDeclaredFields(Type1.Inner2.class);

    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field1"))
        .findAny()
        .orElse(null))
        .isNotNull();
    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field2"))
        .findAny()
        .orElse(null))
        .isNotNull();
    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field3"))
        .findAny()
        .orElse(null))
        .isNotNull();
    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field4"))
        .findAny()
        .orElse(null))
        .isNotNull();
    assertThat(allFields
        .stream()
        .filter(object -> object
            .getName()
            .equals("field5"))
        .findAny()
        .orElse(null))
        .isNull();
  }
}
