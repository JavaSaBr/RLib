package javasabr.rlib.io;

import static org.assertj.core.api.Assertions.assertThat;

import javasabr.rlib.io.util.FileUtils;
import org.junit.jupiter.api.Test;

/**
 * @author JavaSaBr
 */
public class FileUtilsTest {

  @Test
  void shouldGetNameByPath() {
    var path = "/some/folder/some/name.ololo";
    var path2 = "D:\\some\\folder\\some\\name.ololo";

    assertThat(FileUtils.getName(path, '/'))
        .isEqualTo("name.ololo");
    assertThat(FileUtils.getName(path2, '\\'))
        .isEqualTo("name.ololo");
  }

  @Test
  void shouldGetParentByPath() {
    var path = "/some/folder/some/name.ololo";
    var path2 = "D:\\some\\folder\\some\\name.ololo";

    assertThat(FileUtils.getParent(path, '/'))
        .isEqualTo("/some/folder/some");
    assertThat(FileUtils.getParent(path2, '\\'))
        .isEqualTo("D:\\some\\folder\\some");
  }

  @Test
  void shouldNormalizeFileName() {
    var first = FileUtils.normalizeName("file*:?name!!@#$\"\"wefwef<>.png");
    assertThat(first).isEqualTo("file___name!!@#$__wefwef__.png");
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

    assertThat(FileUtils.getExtension(path1)).isEqualTo("txt");
    assertThat(FileUtils.getExtension(path2)).isEqualTo("gz");
    assertThat(FileUtils.getExtension(path3)).isEqualTo("png");
    assertThat(FileUtils.getExtension(path4)).isEqualTo("jpg");
    assertThat(FileUtils.getExtension(path5)).isEqualTo("TxT");
    assertThat(FileUtils.getExtension(path5, true)).isEqualTo("txt");
    assertThat(FileUtils.getExtension(path6)).isNull();
    assertThat(FileUtils.getExtension(path7)).isNull();
  }

  @Test
  void shouldCheckExistingExtension() {
    var path1 = "file.txt";
    var path2 = "file.tar.gz";
    var path3 = "folder/folder.subname/file.png";
    var path4 = "D:\\folder\\folder.subname\\file.jpg";
    var path6 = "D:\\folder\\folder.folder\\test";
    var path7 = "/folder/folder.folder/test";

    assertThat(FileUtils.hasExtension(path1)).isTrue();
    assertThat(FileUtils.hasExtension(path2)).isTrue();
    assertThat(FileUtils.hasExtension(path3)).isTrue();
    assertThat(FileUtils.hasExtension(path4)).isTrue();
    assertThat(FileUtils.hasExtension(path6)).isFalse();
    assertThat(FileUtils.hasExtension(path7)).isFalse();
  }
}
