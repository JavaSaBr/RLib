package javasabr.rlib.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The test to test file utils.
 *
 * @author JavaSaBr
 */
public class FileUtilsTest {

  @Test
  void shouldGetNameByPath() {

    var path = "/some/folder/some/name.ololo";
    var path2 = "D:\\some\\folder\\some\\name.ololo";

    Assertions.assertEquals("name.ololo", FileUtils.getName(path, '/'));
    assertEquals("name.ololo", FileUtils.getName(path2, '\\'));
  }

  @Test
  void shouldGetParentByPath() {

    var path = "/some/folder/some/name.ololo";
    var path2 = "D:\\some\\folder\\some\\name.ololo";

    assertEquals("/some/folder/some", FileUtils.getParent(path, '/'));
    assertEquals("D:\\some\\folder\\some", FileUtils.getParent(path2, '\\'));
  }

  @Test
  void shouldNormalizeFileName() {
    var first = FileUtils.normalizeName("file*:?name!!@#$\"\"wefwef<>.png");
    assertEquals("file___name!!@#$__wefwef__.png", first);
  }

  @Test
  void shouldGetFileExtension() {

    var path1 = "file.txt";
    var path2 = "file.tar.gz";
    var path3 = "folder/folder.subname/file.png";
    var path4 = "D:\\folder\\folder.subname\\file.jpg";
    var path5 = "file.TxT";
    var path6 = "D:\\folder\\folder.folder\\test";
    var path7 = "/folder/folder.folder/test";

    assertEquals("txt", FileUtils.getExtension(path1));
    assertEquals("gz", FileUtils.getExtension(path2));
    assertEquals("png", FileUtils.getExtension(path3));
    assertEquals("jpg", FileUtils.getExtension(path4));
    assertEquals("TxT", FileUtils.getExtension(path5));
    assertEquals("txt", FileUtils.getExtension(path5, true));
    assertEquals("", FileUtils.getExtension(path6));
    assertEquals("", FileUtils.getExtension(path7));
  }

  @Test
  void shouldCheckExistingExtension() {

    var path1 = "file.txt";
    var path2 = "file.tar.gz";
    var path3 = "folder/folder.subname/file.png";
    var path4 = "D:\\folder\\folder.subname\\file.jpg";
    var path6 = "D:\\folder\\folder.folder\\test";
    var path7 = "/folder/folder.folder/test";

    assertTrue(FileUtils.hasExtension(path1));
    assertTrue(FileUtils.hasExtension(path2));
    assertTrue(FileUtils.hasExtension(path3));
    assertTrue(FileUtils.hasExtension(path4));
    assertFalse(FileUtils.hasExtension(path6));
    assertFalse(FileUtils.hasExtension(path7));
  }
}
