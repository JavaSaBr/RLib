package com.ss.rlib.common.test.util;

import com.ss.rlib.common.util.BufferUtils;
import com.ss.rlib.common.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * @author JavaSaBr
 */
class BufferUtilsTest {

    @Test
    void shouldAppendNewDataToSourceBuffer() {

        var additional = ByteBuffer.allocate(16);
        additional.putLong(2).putLong(3);
        additional.flip();

        var source = ByteBuffer.allocate(24);
        source.putLong(1);

        BufferUtils.append(source, additional);

        Assertions.assertFalse(additional.hasRemaining());
        Assertions.assertEquals(2, source.getLong());
        Assertions.assertEquals(3, source.getLong());

        source.clear();

        additional.clear();
        additional.putLong(2).putLong(3);
        additional.flip();
        additional.getLong();

        BufferUtils.append(source, additional);

        Assertions.assertFalse(additional.hasRemaining());
        Assertions.assertEquals(8, source.remaining());
        Assertions.assertEquals(3, source.getLong());
    }

    @Test
    void shouldLoadDataFromOtherBuffer() {

        var data = ByteBuffer.allocate(32);
        data.putInt(1);
        data.flip();

        var source = BufferUtils.loadFrom(data, ByteBuffer.allocate(12));

        Assertions.assertEquals(0, source.position());
        Assertions.assertEquals(4, source.limit());
        Assertions.assertEquals(1, source.getInt());
        Assertions.assertFalse(data.hasRemaining());

        data.clear();
        data.putInt(1).putInt(2).putInt(3).putInt(4);
        data.putInt(5).putInt(6).putInt(7).putInt(8);
        data.flip();

        BufferUtils.loadFrom(data, source);

        Assertions.assertEquals(source.capacity(), data.position());
        Assertions.assertEquals(0, source.position());
        Assertions.assertEquals(source.capacity(), source.remaining());
        Assertions.assertEquals(1, source.getInt());
        Assertions.assertEquals(2, source.getInt());
        Assertions.assertEquals(3, source.getInt());
    }
}
