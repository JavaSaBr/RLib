package javasabr.rlib.common.util;

import org.junit.jupiter.api.Assertions;
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

    Assertions.assertEquals(original, parsed);
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

    Assertions.assertArrayEquals(original, parsed);
  }

  @Test
  void shouldReplaceOneVariableInString() {

    var original = "some text ${test_var} with one variable";
    var expected = "some text var_val with one variable";

    Assertions.assertEquals(expected, StringUtils.replace(original, "${test_var}", "var_val"));
  }

  @Test
  void shouldReplaceTwoVariablesInString() {

    var original = "some text ${test_var1} with two variable ${test_var2}";
    var expected = "some text var_val_1 with two variable var_val_2";

    Assertions.assertEquals(
        expected,
        StringUtils.replace(original, "${test_var1}", "var_val_1", "${test_var2}", "var_val_2"));
  }

  @Test
  void shouldReplaceTwoVariablesInStringTwice() {

    var original = "some text ${test_var1} with two variable ${test_var2}, and ${test_var1} and ${test_var2}";
    var expected = "some text var_val_1 with two variable var_val_2, and var_val_1 and var_val_2";

    Assertions.assertEquals(
        expected,
        StringUtils.replace(original, "${test_var1}", "var_val_1", "${test_var2}", "var_val_2"));
  }

  @Test
  void shouldReplaceVariablesInString() {

    var original = "some text ${test_var1} with variables ${test_var2}, ${test_var3}";
    var expected = "some text var_val_1 with variables var_val_2, var_val_3";

    Assertions.assertEquals(
        expected,
        StringUtils.replace(
            original,
            "${test_var1}",
            "var_val_1",
            "${test_var2}",
            "var_val_2",
            "${test_var3}",
            "var_val_3"));
  }

  @Test
  void shouldThrownIllegalArgumentExceptionDuringReplacingStringWithWrongArgs() {

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> StringUtils.replace("string", "name1", "val1", "name2"));

    Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.replace("string", "name1"));

    Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtils.replace("string"));
  }

  @Test
  void shouldVerifyEmails() {
    Assertions.assertTrue(StringUtils.isValidEmail("test@test.com"));
    Assertions.assertTrue(StringUtils.isValidEmail("тест@test.com"));
    Assertions.assertTrue(StringUtils.isValidEmail("тест@тест.рф"));
    Assertions.assertTrue(StringUtils.isValidEmail("my-name.test@test.com"));

    Assertions.assertFalse(StringUtils.isValidEmail("@test.com"));
    Assertions.assertFalse(StringUtils.isValidEmail("test@.com"));
    Assertions.assertFalse(StringUtils.isValidEmail("%$%&$&%@$%&&%$&.com"));
  }

  @Test
  void shouldDetectEmails() {
    Assertions.assertTrue(StringUtils.isEmail("test@test.com"));
    Assertions.assertTrue(StringUtils.isEmail("test.test@test.com"));
    Assertions.assertTrue(StringUtils.isEmail("test.test@test.test.com"));

    Assertions.assertFalse(StringUtils.isEmail("@test.com"));
    Assertions.assertFalse(StringUtils.isEmail("test-test.com"));
    Assertions.assertFalse(StringUtils.isEmail("test@test"));
    Assertions.assertFalse(StringUtils.isEmail("test@test."));
  }

  @Test
  void shouldCheckIfStringIsEmpty() {
    Assertions.assertTrue(StringUtils.isEmpty(null));
    Assertions.assertFalse(StringUtils.isNotEmpty(null));
    
    Assertions.assertTrue(StringUtils.isEmpty(""));
    Assertions.assertFalse(StringUtils.isNotEmpty(""));
    
    Assertions.assertFalse(StringUtils.isEmpty(" "));
    Assertions.assertTrue(StringUtils.isNotEmpty(" "));
    
    Assertions.assertFalse(StringUtils.isEmpty("123"));
    Assertions.assertTrue(StringUtils.isNotEmpty("123"));
  }

  @Test
  void shouldReturnAnotherStringIfEmpty() {
    Assertions.assertEquals("alt", StringUtils.ifEmpty(null, "alt"));
    Assertions.assertEquals("alt", StringUtils.ifEmpty("", "alt"));
    Assertions.assertEquals(" ", StringUtils.ifEmpty(" ", "alt"));
    Assertions.assertEquals("123", StringUtils.ifEmpty("123", "alt"));
  }

  @Test
  void shouldCheckIfStringIsBlank() {
    Assertions.assertTrue(StringUtils.isBlank(null));
    Assertions.assertFalse(StringUtils.isNotBlank(null));
    
    Assertions.assertTrue(StringUtils.isBlank(""));
    Assertions.assertFalse(StringUtils.isNotBlank(""));
    
    Assertions.assertTrue(StringUtils.isBlank(" "));
    Assertions.assertFalse(StringUtils.isNotBlank(" "));
    
    Assertions.assertTrue(StringUtils.isBlank("       "));
    Assertions.assertFalse(StringUtils.isNotBlank("       "));
    
    Assertions.assertFalse(StringUtils.isBlank("   1"));
    Assertions.assertTrue(StringUtils.isNotBlank("   1"));
    
    Assertions.assertFalse(StringUtils.isBlank("123"));
    Assertions.assertTrue(StringUtils.isNotBlank("123"));
  }
  
  @Test
  void shouldReturnAnotherStringIfBlank() {
    Assertions.assertEquals("alt", StringUtils.ifBlank(null, "alt"));
    Assertions.assertEquals("alt", StringUtils.ifBlank("", "alt"));
    Assertions.assertEquals("alt", StringUtils.ifBlank(" ", "alt"));
    Assertions.assertEquals("alt", StringUtils.ifBlank("   ", "alt"));
    Assertions.assertEquals("    1", StringUtils.ifBlank("    1", "alt"));
    Assertions.assertEquals("123", StringUtils.ifBlank("123", "alt"));
  }
}
