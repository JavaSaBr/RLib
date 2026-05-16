package javasabr.rlib.classpath.impl;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Stream;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.ArrayCollectors;
import javasabr.rlib.collections.array.MutableArray;
import javasabr.rlib.common.util.Utils;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.experimental.FieldDefaults;

/**
 * @author JavaSaBr
 */
@CustomLog
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class ManifestClassPathScannerImpl extends ClassPathScannerImpl {

  Class<?> rootClass;
  String classPathKey;

  public ManifestClassPathScannerImpl(
      ClassLoader classLoader,
      Class<?> rootClass,
      String classPathKey) {
    super(classLoader);
    this.rootClass = rootClass;
    this.classPathKey = classPathKey;
  }

  protected Array<String> calculateManifestClassPath() {

    var result = MutableArray.ofType(String.class);
    var currentThread = Thread.currentThread();

    Path root = Utils.getRootFolderFromClass(rootClass);
    ClassLoader loader = currentThread.getContextClassLoader();
    Enumeration<URL> urls = Utils.uncheckedGet(loader, arg -> arg.getResources(JarFile.MANIFEST_NAME));

    while (urls.hasMoreElements()) {

      try {

        URL url = urls.nextElement();
        InputStream is = url.openStream();
        if (is == null) {
          log.warn(url, arg -> "not found input stream for the url " + arg);
          continue;
        }

        var manifest = new Manifest(is);

        Attributes attributes = manifest.getMainAttributes();
        String value = attributes.getValue(classPathKey);
        if (value == null) {
          continue;
        }

        String[] classpath = value.split(" ");

        for (String path : classpath) {
          Path file = root.resolve(path);
          if (Files.exists(file)) {
            result.add(file.toString());
          }
        }

      } catch (Exception exc) {
        log.warn(exc);
      }
    }

    return result;
  }

  @Override
  protected Array<String> calculatePathsToScan() {

    Stream<String> originalStream = super
        .calculatePathsToScan()
        .stream();
    Stream<String> extraStream = calculateManifestClassPath()
        .stream();

    return Stream
        .concat(originalStream, extraStream)
        .distinct()
        .collect(ArrayCollectors.toArray(String.class));
  }
}
