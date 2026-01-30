package javasabr.rlib.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

/**
 * Test methods in {@link StringUtils}
 *
 * @author JavaSaBr
 */
class StringUtilsTest {

  @Test
  void shouldConvertStringToHex() {

    var original = "EWfwefsbrt34532#%#$^$#^@gfh\"P{\">K<CCZA";
    var hex = StringUtils.toHex(original);
    var parsed = StringUtils.fromHex(hex);

    assertThat(parsed).isEqualTo(original);
  }

  @Test
  void shouldConvertHexStringToString() {

    var original = new byte[]{
        5,
        2,
        8,
        120,
        21,
        -40,
        -120,
        63,
        70
    };
    var hex = StringUtils.bytesToHexString(original);
    var parsed = StringUtils.hexStringToBytes(hex);

    assertThat(parsed).isEqualTo(original);
  }

  @Test
  void shouldReplaceOneVariableInString() {

    var original = "some text ${test_var} with one variable";
    var expected = "some text var_val with one variable";

    assertThat(StringUtils.replace(original, "${test_var}", "var_val"))
        .isEqualTo(expected);
  }

  @Test
  void shouldReplaceTwoVariablesInString() {

    var original = "some text ${test_var1} with two variable ${test_var2}";
    var expected = "some text var_val_1 with two variable var_val_2";

    assertThat(StringUtils.replace(original, "${test_var1}", "var_val_1", "${test_var2}", "var_val_2"))
        .isEqualTo(expected);
  }

  @Test
  void shouldReplaceTwoVariablesInStringTwice() {

    var original = "some text ${test_var1} with two variable ${test_var2}, and ${test_var1} and ${test_var2}";
    var expected = "some text var_val_1 with two variable var_val_2, and var_val_1 and var_val_2";

    assertThat(StringUtils.replace(original, "${test_var1}", "var_val_1", "${test_var2}", "var_val_2"))
        .isEqualTo(expected);
  }

  @Test
  void shouldReplaceVariablesInString() {

    var original = "some text ${test_var1} with variables ${test_var2}, ${test_var3}";
    var expected = "some text var_val_1 with variables var_val_2, var_val_3";

    assertThat(StringUtils.replace(
            original,
            "${test_var1}",
            "var_val_1",
            "${test_var2}",
            "var_val_2",
            "${test_var3}",
            "var_val_3"))
        .isEqualTo(expected);
  }

  @Test
  void shouldThrownIllegalArgumentExceptionDuringReplacingStringWithWrongArgs() {

    assertThatThrownBy(() -> StringUtils.replace("string", "name1", "val1", "name2"))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> StringUtils.replace("string", "name1"))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> StringUtils.replace("string"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldVerifyEmails() {
    assertThat(StringUtils.isValidEmail("test@test.com")).isTrue();
    assertThat(StringUtils.isValidEmail("тест@test.com")).isTrue();
    assertThat(StringUtils.isValidEmail("тест@тест.рф")).isTrue();
    assertThat(StringUtils.isValidEmail("my-name.test@test.com")).isTrue();

    assertThat(StringUtils.isValidEmail("@test.com")).isFalse();
    assertThat(StringUtils.isValidEmail("test@.com")).isFalse();
    assertThat(StringUtils.isValidEmail("%$%&$&%@$%&&%$&.com")).isFalse();
  }

  @Test
  void shouldDetectEmails() {
    assertThat(StringUtils.isEmail("test@test.com")).isTrue();
    assertThat(StringUtils.isEmail("test.test@test.com")).isTrue();
    assertThat(StringUtils.isEmail("test.test@test.test.com")).isTrue();

    assertThat(StringUtils.isEmail("@test.com")).isFalse();
    assertThat(StringUtils.isEmail("test-test.com")).isFalse();
    assertThat(StringUtils.isEmail("test@test")).isFalse();
    assertThat(StringUtils.isEmail("test@test.")).isFalse();
  }

  @Test
  void shouldCheckIfStringIsEmpty() {
    assertThat(StringUtils.isEmpty(null)).isTrue();
    assertThat(StringUtils.isNotEmpty(null)).isFalse();
    
    assertThat(StringUtils.isEmpty("")).isTrue();
    assertThat(StringUtils.isNotEmpty("")).isFalse();
    
    assertThat(StringUtils.isEmpty(" ")).isFalse();
    assertThat(StringUtils.isNotEmpty(" ")).isTrue();
    
    assertThat(StringUtils.isEmpty("123")).isFalse();
    assertThat(StringUtils.isNotEmpty("123")).isTrue();
  }

  @Test
  void shouldReturnAnotherStringIfEmpty() {
    assertThat(StringUtils.ifEmpty(null, "alt")).isEqualTo("alt");
    assertThat(StringUtils.ifEmpty("", "alt")).isEqualTo("alt");
    assertThat(StringUtils.ifEmpty(" ", "alt")).isEqualTo(" ");
    assertThat(StringUtils.ifEmpty("123", "alt")).isEqualTo("123");
  }

  @Test
  void shouldCheckIfStringIsBlank() {
    assertThat(StringUtils.isBlank(null)).isTrue();
    assertThat(StringUtils.isNotBlank(null)).isFalse();
    
    assertThat(StringUtils.isBlank("")).isTrue();
    assertThat(StringUtils.isNotBlank("")).isFalse();
    
    assertThat(StringUtils.isBlank(" ")).isTrue();
    assertThat(StringUtils.isNotBlank(" ")).isFalse();
    
    assertThat(StringUtils.isBlank("       ")).isTrue();
    assertThat(StringUtils.isNotBlank("       ")).isFalse();
    
    assertThat(StringUtils.isBlank("   1")).isFalse();
    assertThat(StringUtils.isNotBlank("   1")).isTrue();
    
    assertThat(StringUtils.isBlank("123")).isFalse();
    assertThat(StringUtils.isNotBlank("123")).isTrue();
  }
  
  @Test
  void shouldReturnAnotherStringIfBlank() {
    assertThat(StringUtils.ifBlank(null, "alt")).isEqualTo("alt");
    assertThat(StringUtils.ifBlank("", "alt")).isEqualTo("alt");
    assertThat(StringUtils.ifBlank(" ", "alt")).isEqualTo("alt");
    assertThat(StringUtils.ifBlank("   ", "alt")).isEqualTo("alt");
    assertThat(StringUtils.ifBlank("    1", "alt")).isEqualTo("    1");
    assertThat(StringUtils.ifBlank("123", "alt")).isEqualTo("123");
  }
}
