package javasabr.rlib.io;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
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
 
  @Test
  void shouldUnzipFileCorrectly() throws IOException {
    // given:
    Path zipFile = Files.createTempFile("test-archive", ".zip");

    try (var zout = new ZipOutputStream(Files.newOutputStream(zipFile, StandardOpenOption.CREATE))) {
      zout.putNextEntry(new ZipEntry("fileA.txt"));
      zout.write("test text".getBytes(StandardCharsets.UTF_8));

      zout.putNextEntry(new ZipEntry("../fileB.txt"));
      zout.write("test text 2".getBytes(StandardCharsets.UTF_8));

      ZipEntry dirAEntry = new ZipEntry("dir_a/");
      dirAEntry.setMethod(ZipEntry.STORED);
      dirAEntry.setSize(0);
      dirAEntry.setCrc(0);
      zout.putNextEntry(dirAEntry);

      zout.putNextEntry(new ZipEntry("dir_a/fileC.txt"));
      zout.write("test text 3".getBytes(StandardCharsets.UTF_8));

      zout.putNextEntry(new ZipEntry("dir_a/../fileD.txt"));
      zout.write("test text 4".getBytes(StandardCharsets.UTF_8));

      zout.putNextEntry(new ZipEntry("dir_a/../../../fileE.txt"));
      zout.write("test text 5".getBytes(StandardCharsets.UTF_8));
    }

    Path tempDirectory = Files.createTempDirectory("test-unzip");
    Path outputDir = tempDirectory
        .resolve("output")
        .resolve("folder");
    
    Files.createDirectories(outputDir);
    
    // when:
    int unpackedFiles = FileUtils.unzip(outputDir, zipFile);

    // then:
    assertThat(unpackedFiles).isEqualTo(3);
    assertThat(outputDir
        .resolve("fileA.txt"))
        .exists();
    assertThat(outputDir
        .resolve("dir_a")
        .resolve("fileC.txt"))
        .exists();
    assertThat(tempDirectory
        .resolve("output")
        .resolve("fileB.txt"))
        .doesNotExist();
    assertThat(tempDirectory
        .resolve("fileE.txt"))
        .doesNotExist();
  }
}
