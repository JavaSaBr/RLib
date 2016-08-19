package rlib.test.utils;

import org.junit.Assert;
import org.junit.Test;

import rlib.util.FileUtils;

/**
 * Реализация теста утильного класса по работе с файлами.
 *
 * @author JavaSaBr
 */
public class TestFileUtils extends Assert {

    @Test
    public void test() {

        final String result = FileUtils.getNameWithoutExtension("test/test.olooo/test/file.extension");
        final String result2 = FileUtils.getExtension("test/test.olooo/test/file.extension");
        System.out.println();
    }
}
