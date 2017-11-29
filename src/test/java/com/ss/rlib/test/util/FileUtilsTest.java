package com.ss.rlib.test.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.ss.rlib.util.FileUtils;
import org.junit.jupiter.api.Test;

/**
 * The test to test file utils.
 *
 * @author JavaSaBr
 */
public class FileUtilsTest {

    @Test
    public void testGetNameByPath() {

        final String path = "/some/folder/some/name.ololo";
        final String path2 = "D:\\some\\folder\\some\\name.ololo";

        assertEquals("name.ololo", FileUtils.getName(path, '/'));
        assertEquals("name.ololo", FileUtils.getName(path2, '\\'));
    }

    @Test
    public void testGetParentByPath() {

        final String path = "/some/folder/some/name.ololo";
        final String path2 = "D:\\some\\folder\\some\\name.ololo";

        assertEquals("/some/folder/some", FileUtils.getParent(path, '/'));
        assertEquals("D:\\some\\folder\\some", FileUtils.getParent(path2, '\\'));
    }

    @Test
    public void testNormalizeFileName() {
        final String first = FileUtils.normalizeName("file*:?name!!@#$\"\"wefwef<>.png");
        assertEquals("file___name!!@#$__wefwef__.png", first);
    }
}
