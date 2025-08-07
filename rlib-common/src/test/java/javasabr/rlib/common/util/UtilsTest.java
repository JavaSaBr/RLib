package javasabr.rlib.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link Utils} methods.
 *
 * @author JavaSaBr
 */
public class UtilsTest {

    @Test
    void shouldSafetyTryGet() {

        Assertions.assertEquals(Integer.valueOf(15), Utils.tryGet("15", Integer::valueOf));
        Assertions.assertNull(Utils.tryGet("invalidnumber", Integer::valueOf));

        Assertions.assertEquals(Integer.valueOf(15), Utils.tryGet("15", Integer::valueOf, 2));
        Assertions.assertEquals(Integer.valueOf(2), Utils.tryGet("invalidnumber", Integer::valueOf, 2));
    }

    @Test
    void shouldSafetyTryGetAndConvert() {
        Assertions.assertEquals("15", Utils.tryGetAndConvert("15", Integer::valueOf, Object::toString));
        Assertions.assertNull(Utils.tryGetAndConvert("invalidnumber", Integer::valueOf, Object::toString));
    }
}
