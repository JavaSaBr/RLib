package javasabr.rlib.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.io.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileUtilsTest {

  @Test
  void shouldGetNameByPath() {
    // given:
    var path = "/some/folder/some/name.ololo";
    var path2 = "D:\\some\\folder\\some\\name.ololo";

    // when/then:
    assertThat(FileUtils.getName(path, '/'))
        .isEqualTo("name.ololo");
    assertThat(FileUtils.getName(path2, '\\'))
        .isEqualTo("name.ololo");
  }

  @Test
  void shouldGetParentByPath() {
    // given:
    var path = "/some/folder/some/name.ololo";
    var path2 = "D:\\some\\folder\\some\\name.ololo";

    // when/then:
    assertThat(FileUtils.getParent(path, '/'))
        .isEqualTo("/some/folder/some");
    assertThat(FileUtils.getParent(path2, '\\'))
        .isEqualTo("D:\\some\\folder\\some");
  }

  @Test
  void shouldNormalizeFileName() {
    // given:
    var invalidFileName = "file*:?name!!@#$\"\"wefwef<>.png";

    // when:
    var normalized = FileUtils.normalizeName(invalidFileName);

    // then:
    assertThat(normalized).isEqualTo("file___name!!@#$__wefwef__.png");
  }

  @Test
  void shouldNormalizeEmptyFileName() {
    // when/then:
    assertThat(FileUtils.normalizeName(null)).isEqualTo("_");
    assertThat(FileUtils.normalizeName("")).isEqualTo("_");
  }

  @Test
  void shouldGetFileExtension() {
    // given:
    var path1 = "file.txt";
    var path2 = "file.tar.gz";
    var path3 = "folder/folder.subname/file.png";
    var path4 = "D:\\folder\\folder.subname\\file.jpg";
    var path5 = "file.TxT";
    var path6 = "D:\\folder\\folder.folder\\test";
    var path7 = "/folder/folder.folder/test";

    // when/then:
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
    // given:
    var path1 = "file.txt";
    var path2 = "file.tar.gz";
    var path3 = "folder/folder.subname/file.png";
    var path4 = "D:\\folder\\folder.subname\\file.jpg";
    var path6 = "D:\\folder\\folder.folder\\test";
    var path7 = "/folder/folder.folder/test";

    // when/then:
    assertThat(FileUtils.hasExtension(path1)).isTrue();
    assertThat(FileUtils.hasExtension(path2)).isTrue();
    assertThat(FileUtils.hasExtension(path3)).isTrue();
    assertThat(FileUtils.hasExtension(path4)).isTrue();
    assertThat(FileUtils.hasExtension(path6)).isFalse();
    assertThat(FileUtils.hasExtension(path7)).isFalse();
  }

  @Test
  void shouldValidateFileName() {
    // when/then: - valid filenames
    assertThat(FileUtils.isValidFileName("document.txt")).isTrue();
    assertThat(FileUtils.isValidFileName("my-file_123.pdf")).isTrue();
    assertThat(FileUtils.isValidFileName("file")).isTrue();

    // when/then: - invalid filenames
    assertThat(FileUtils.isValidFileName(null)).isFalse();
    assertThat(FileUtils.isValidFileName("")).isFalse();
    assertThat(FileUtils.isValidFileName("file<name>.txt")).isFalse();
    assertThat(FileUtils.isValidFileName("file:name.txt")).isFalse();
    assertThat(FileUtils.isValidFileName("file?name.txt")).isFalse();
    assertThat(FileUtils.isValidFileName("CON")).isFalse();
    assertThat(FileUtils.isValidFileName("PRN")).isFalse();
    assertThat(FileUtils.isValidFileName("NUL")).isFalse();
    assertThat(FileUtils.isValidFileName("COM1")).isFalse();
    assertThat(FileUtils.isValidFileName("LPT1")).isFalse();
  }

  @Test
  void shouldGetNameWithoutExtension() {
    // when/then:
    assertThat(FileUtils.getNameWithoutExtension("file.txt"))
        .isEqualTo("file");
    assertThat(FileUtils.getNameWithoutExtension("file.tar.gz"))
        .isEqualTo("file.tar");
    assertThat(FileUtils.getNameWithoutExtension("file"))
        .isEqualTo("file");
    assertThat(FileUtils.getNameWithoutExtension(""))
        .isEqualTo("");
    assertThat(FileUtils.getNameWithoutExtension((String) null))
        .isNull();
  }

  @Test
  void shouldGetNameWithoutExtensionFromPath(@TempDir Path tempDir) throws IOException {
    // given:
    Path fileWithExtension = tempDir.resolve("test.txt");
    Path fileWithoutExtension = tempDir.resolve("testfile");
    Files.createFile(fileWithExtension);
    Files.createFile(fileWithoutExtension);

    // when/then:
    assertThat(FileUtils.getNameWithoutExtension(fileWithExtension))
        .isEqualTo("test");
    assertThat(FileUtils.getNameWithoutExtension(fileWithoutExtension))
        .isEqualTo("testfile");
  }

  @Test
  void shouldGetFileName(@TempDir Path tempDir) throws IOException {
    // given:
    Path file = tempDir.resolve("myfile.txt");
    Files.createFile(file);

    // when:
    String fileName = FileUtils.fileName(file);

    // then:
    assertThat(fileName).isEqualTo("myfile.txt");
  }

  @Test
  void shouldCheckExtensionOnPath(@TempDir Path tempDir) throws IOException {
    // given:
    Path txtFile = tempDir.resolve("document.txt");
    Path pngFile = tempDir.resolve("image.png");
    Files.createFile(txtFile);
    Files.createFile(pngFile);

    // when/then:
    assertThat(FileUtils.hasExtension(txtFile, ".txt")).isTrue();
    assertThat(FileUtils.hasExtension(txtFile, ".pdf")).isFalse();
    assertThat(FileUtils.hasExtension(pngFile, ".png")).isTrue();
  }

  @Test
  void shouldDeleteFile(@TempDir Path tempDir) throws IOException {
    // given:
    Path fileToDelete = tempDir.resolve("to-delete.txt");
    Files.writeString(fileToDelete, "content");
    assertThat(fileToDelete).exists();

    // when:
    FileUtils.delete(fileToDelete);

    // then:
    assertThat(fileToDelete).doesNotExist();
  }

  @Test
  void shouldDeleteDirectoryRecursively(@TempDir Path tempDir) throws IOException {
    // given:
    Path dirToDelete = tempDir.resolve("dir-to-delete");
    Files.createDirectories(dirToDelete);
    Files.writeString(dirToDelete.resolve("file1.txt"), "content1");
    Files.writeString(dirToDelete.resolve("file2.txt"), "content2");
    
    Path subDir = dirToDelete.resolve("subdir");
    Files.createDirectories(subDir);
    Files.writeString(subDir.resolve("file3.txt"), "content3");
    
    assertThat(dirToDelete).exists();

    // when:
    FileUtils.delete(dirToDelete);

    // then:
    assertThat(dirToDelete).doesNotExist();
  }

  @Test
  void shouldGetFilesFromDirectory(@TempDir Path tempDir) throws IOException {
    // given:
    Files.writeString(tempDir.resolve("file1.txt"), "content1");
    Files.writeString(tempDir.resolve("file2.txt"), "content2");
    Files.writeString(tempDir.resolve("file3.png"), "content3");

    // when:
    var allFiles = FileUtils.getFiles(tempDir);
    var txtFiles = FileUtils.getFiles(tempDir, ".txt");

    // then:
    assertThat(allFiles).hasSize(3);
    assertThat(txtFiles).hasSize(2);
  }

  @Test
  void shouldCreateDirectories(@TempDir Path tempDir) {
    // given:
    Path nestedDir = tempDir
        .resolve("level1")
        .resolve("level2")
        .resolve("level3");
    assertThat(nestedDir).doesNotExist();

    // when:
    FileUtils.createDirectories(nestedDir);

    // then:
    assertThat(nestedDir).exists().isDirectory();
  }

  @Test
  void shouldStreamDirectory(@TempDir Path tempDir) throws IOException {
    // given:
    Files.writeString(tempDir.resolve("file1.txt"), "content1");
    Files.writeString(tempDir.resolve("file2.txt"), "content2");
    Path subDir = tempDir.resolve("subdir");
    Files.createDirectories(subDir);

    // when:
    var files = FileUtils
        .stream(tempDir)
        .toList();

    // then:
    assertThat(files).hasSize(3);
  }

  @Test
  void shouldThrowWhenStreamingNonDirectory(@TempDir Path tempDir) throws IOException {
    // given:
    Path file = tempDir.resolve("file.txt");
    Files.writeString(file, "content");

    // when/then:
    assertThatThrownBy(() -> FileUtils.stream(file))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldGetExtensionFromPath(@TempDir Path tempDir) throws IOException {
    // given:
    Path txtFile = tempDir.resolve("document.txt");
    Path directory = tempDir.resolve("subdir");
    Files.createFile(txtFile);
    Files.createDirectories(directory);

    // when/then:
    assertThat(FileUtils.getExtension(txtFile))
        .isEqualTo("txt");
    assertThat(FileUtils.getExtension(txtFile, true))
        .isEqualTo("txt");
    assertThat(FileUtils.getExtension(directory))
        .isNull();
  }

  @Test
  void shouldGetUri(@TempDir Path tempDir) throws IOException {
    // given:
    Path file = tempDir.resolve("test.txt");
    Files.createFile(file);

    // when:
    var uri = FileUtils.getUri(file);

    // then:
    assertThat(uri).isNotNull();
    assertThat(uri.toString()).contains("test.txt");
  }

  @Test
  void shouldRelativizePaths(@TempDir Path tempDir) throws IOException {
    // given:
    Path base = tempDir.resolve("base");
    Path other = tempDir
        .resolve("base")
        .resolve("sub")
        .resolve("file.txt");
    Files.createDirectories(other.getParent());
    Files.createFile(other);
    
    String expected = "sub/file.txt".replace("/", tempDir
        .getFileSystem()
        .getSeparator());
    
    // when:
    Path relative = FileUtils.relativize(base, other);

    // then:
    assertThat(relative.toString()).isEqualTo(expected);
  }

  @Test
  void shouldSafeRelativizeWithNulls() {
    // when/then:
    assertThat(FileUtils.safeRelativize(null, Path.of("/some/path"))).isNull();
    assertThat(FileUtils.safeRelativize(Path.of("/base"), null)).isNull();
    assertThat(FileUtils.safeRelativize(null, null)).isNull();
  }

  @Test
  void shouldUnzipFileCorrectly(@TempDir Path tempDir) throws IOException {
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

    Path outputDir = tempDir
        .resolve("output")
        .resolve("folder");

    Files.createDirectories(outputDir);

    // when:
    int unpackedFiles = FileUtils.unzip(outputDir, zipFile);

    // then:
    assertThat(unpackedFiles).isEqualTo(3);
    assertThat(outputDir.resolve("fileA.txt")).exists();
    assertThat(outputDir.resolve("dir_a").resolve("fileC.txt")).exists();
    assertThat(tempDir.resolve("output").resolve("fileB.txt")).doesNotExist();
    assertThat(tempDir.resolve("fileE.txt")).doesNotExist();
    
    // cleanup:
    FileUtils.delete(zipFile);
  }

  @Test
  void shouldThrowWhenUnzipToNonExistentDirectory(@TempDir Path tempDir) throws IOException {
    // given:
    Path zipFile = Files.createTempFile("test-archive", ".zip");
    try (var zout = new ZipOutputStream(Files.newOutputStream(zipFile, StandardOpenOption.CREATE))) {
      zout.putNextEntry(new ZipEntry("file.txt"));
      zout.write("content".getBytes(StandardCharsets.UTF_8));
    }
    Path nonExistentDir = tempDir.resolve("/non/existent/directory");

    // when/then:
    assertThatThrownBy(() -> FileUtils.unzip(nonExistentDir, zipFile))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldCheckMultipleExtensionsOnPath(@TempDir Path tempDir) throws IOException {
    // given:
    Path txtFile = tempDir.resolve("document.txt");
    Path pngFile = tempDir.resolve("image.png");
    Path jpgFile = tempDir.resolve("photo.jpg");
    Files.createFile(txtFile);
    Files.createFile(pngFile);
    Files.createFile(jpgFile);

    String[] imageExtensions = {".png", ".jpg", ".gif"};
    String[] textExtensions = {".txt", ".md", ".doc"};

    // when/then:
    assertThat(FileUtils.hasExtensions(txtFile, textExtensions)).isTrue();
    assertThat(FileUtils.hasExtensions(txtFile, imageExtensions)).isFalse();
    assertThat(FileUtils.hasExtensions(pngFile, imageExtensions)).isTrue();
    assertThat(FileUtils.hasExtensions(jpgFile, imageExtensions)).isTrue();
  }

  @Test
  void shouldCheckMultipleExtensionsOnString() {
    // given:
    var txtPath = "document.txt";
    var pngPath = "image.png";
    var noExtPath = "noextension";

    String[] imageExtensions = {".png", ".jpg", ".gif"};
    String[] textExtensions = {".txt", ".md", ".doc"};

    // when/then:
    assertThat(FileUtils.hasExtensions(txtPath, textExtensions)).isTrue();
    assertThat(FileUtils.hasExtensions(txtPath, imageExtensions)).isFalse();
    assertThat(FileUtils.hasExtensions(pngPath, imageExtensions)).isTrue();
    assertThat(FileUtils.hasExtensions(noExtPath, textExtensions)).isFalse();
    assertThat(FileUtils.hasExtensions(txtPath, (String[]) null)).isFalse();
  }

  @Test
  void shouldGetFirstFreeName(@TempDir Path tempDir) throws IOException {
    // given:
    Path existingFile = tempDir.resolve("file.txt");
    Files.createFile(existingFile);

    // when:
    String firstName = FileUtils.getFirstFreeName(tempDir, existingFile);

    // then:
    assertThat(firstName).isEqualTo("file_1.txt");

    // given: - create file_1.txt
    Files.createFile(tempDir.resolve("file_1.txt"));

    // when:
    String secondName = FileUtils.getFirstFreeName(tempDir, existingFile);

    // then:
    assertThat(secondName).isEqualTo("file_2.txt");
  }

  @Test
  void shouldGetFirstFreeNameWhenNotExists(@TempDir Path tempDir) {
    // given:
    Path nonExistingFile = tempDir.resolve("newfile.txt");

    // when:
    String name = FileUtils.getFirstFreeName(tempDir, nonExistingFile);

    // then:
    assertThat(name).isEqualTo("newfile.txt");
  }

  @Test
  void shouldCreateTempFile() throws IOException {
    // when:
    Path tempFile = FileUtils.createTempFile("test-prefix", ".tmp");

    // then:
    assertThat(tempFile).exists();
    assertThat(tempFile.getFileName().toString()).startsWith("test-prefix");
    assertThat(tempFile.getFileName().toString()).endsWith(".tmp");

    // cleanup
    Files.deleteIfExists(tempFile);
  }

  @Test
  void shouldGetUrl(@TempDir Path tempDir) throws IOException {
    // given:
    Path file = tempDir.resolve("test.txt");
    Files.createFile(file);

    // when:
    var url = FileUtils.getUrl(file);

    // then:
    assertThat(url).isNotNull();
    assertThat(url.toString()).contains("test.txt");
  }

  @Test
  void shouldGetLastModifiedTime(@TempDir Path tempDir) throws IOException {
    // given:
    Path file = tempDir.resolve("test.txt");
    Files.writeString(file, "content");

    // when:
    var lastModified = FileUtils.getLastModifiedTime(file);

    // then:
    assertThat(lastModified).isNotNull();
    assertThat(lastModified.toMillis()).isGreaterThan(0);
  }

  @Test
  void shouldGetFilesWithDirectoriesIncluded(@TempDir Path tempDir) throws IOException {
    // given:
    Path testDir = tempDir.resolve("testdir");
    Files.createDirectories(testDir);
    Files.writeString(testDir.resolve("file1.txt"), "content1");
    Files.writeString(testDir.resolve("file2.txt"), "content2");
    Path subDir = testDir.resolve("subdir");
    Files.createDirectories(subDir);

    // when:
    var filesWithDirs = FileUtils.getFiles(testDir, true, (String[]) null);
    var filesWithoutDirs = FileUtils.getFiles(testDir, false, (String[]) null);

    // then:
    // With dirs: testDir + subdir + file1.txt + file2.txt = 4
    assertThat(filesWithDirs).hasSize(4);
    // Without dirs: file1.txt + file2.txt = 2
    assertThat(filesWithoutDirs).hasSize(2);
  }

  @Test
  void shouldWalkFileTree(@TempDir Path tempDir) throws IOException {
    // given:
    Files.writeString(tempDir.resolve("file1.txt"), "content1");
    Path subDir = tempDir.resolve("subdir");
    Files.createDirectories(subDir);
    Files.writeString(subDir.resolve("file2.txt"), "content2");

    var visitedFiles = MutableArray.ofType(Path.class);

    // when:
    FileUtils.walkFileTree(tempDir, new SimpleFileVisitor<>() {
      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        visitedFiles.add(file);
        return FileVisitResult.CONTINUE;
      }
    });

    // then:
    assertThat(visitedFiles).hasSize(2);
  }

  @Test
  void shouldReturnNullFileNameForRootPath() {
    // given:
    Path rootPath = Path.of("/");

    // when:
    String fileName = FileUtils.fileName(rootPath);

    // then:
    assertThat(fileName).isNull();
  }

  @Test
  void shouldHandleShortPathsInGetName() {
    // when/then:
    assertThat(FileUtils.getName("a", '/'))
        .isEqualTo("a");
    assertThat(FileUtils.getName("", '/'))
        .isEqualTo("");
  }

  @Test
  void shouldHandleShortPathsInGetParent() {
    // when/then:
    assertThat(FileUtils.getParent("a", '/'))
        .isEqualTo("a");
    assertThat(FileUtils.getParent("", '/'))
        .isEqualTo("");
  }

  @Test
  void shouldHandlePathWithNoSeparatorInGetName() {
    // when/then:
    assertThat(FileUtils.getName("filename.txt", '/'))
        .isEqualTo("filename.txt");
  }

  @Test
  void shouldHandlePathWithNoSeparatorInGetParent() {
    // when/then:
    assertThat(FileUtils.getParent("filename.txt", '/'))
        .isEqualTo("filename.txt");
  }
}
