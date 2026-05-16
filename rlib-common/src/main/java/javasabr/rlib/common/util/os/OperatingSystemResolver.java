package javasabr.rlib.common.util.os;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javasabr.rlib.common.util.StringUtils;
import lombok.CustomLog;
import org.jspecify.annotations.Nullable;

/**
 * Resolves operating system information from system properties and configuration files.
 *
 * @since 10.0.0
 */
@CustomLog
public class OperatingSystemResolver {

  public static final String FILE_PROC_VERSION = "/proc/version";
  public static final String FILE_ETC_ISSUE = "/etc/issue";
  public static final String FILE_ETC = "/etc/";
  public static final String FILE_ETC_SYSTEM_RELEASE = "/etc/system-release";
  public static final String FILE_ETC_LSB_RELEASE = "/etc/lsb-release";

  public static final String PROP_PRETTY_NAME = "PRETTY_NAME";
  public static final String PROP_DISTRIB_CODENAME = "DISTRIB_CODENAME";
  public static final String PROP_DISTRIB_DESCRIPTION = "DISTRIB_DESCRIPTION";

  private static final String NAME = System.getProperty("os.name");
  private static final String VERSION = System.getProperty("os.version");
  private static final String ARCH = System.getProperty("os.arch");

  private static final Map<String, String> MAC_OS_VERSION_MAPPING = new HashMap<>();
  private static final Map<Integer, String> DARWIN_VERSION_MAPPING = new HashMap<>();

  private static final List<String> LINUX_VERSION_NAMES = new ArrayList<>();

  static {
    MAC_OS_VERSION_MAPPING.put("10.0", "Puma");
    MAC_OS_VERSION_MAPPING.put("10.1", "Cheetah");
    MAC_OS_VERSION_MAPPING.put("10.2", "Jaguar");
    MAC_OS_VERSION_MAPPING.put("10.3", "Panther");
    MAC_OS_VERSION_MAPPING.put("10.4", "Tiger");
    MAC_OS_VERSION_MAPPING.put("10.5", "Leopard");
    MAC_OS_VERSION_MAPPING.put("10.6", "Snow Leopard");
    MAC_OS_VERSION_MAPPING.put("10.7", "Snow Lion");
    MAC_OS_VERSION_MAPPING.put("10.8", "Mountain Lion");
    MAC_OS_VERSION_MAPPING.put("10.9", "Mavericks");
    MAC_OS_VERSION_MAPPING.put("10.10", "Yosemite");
    MAC_OS_VERSION_MAPPING.put("10.11", "El Capitan ");
    MAC_OS_VERSION_MAPPING.put("10.12", "Sierra");
    MAC_OS_VERSION_MAPPING.put("10.13", "High Sierra");
    MAC_OS_VERSION_MAPPING.put("10.14", "Mojave");
    MAC_OS_VERSION_MAPPING.put("10.15", "Catalina");
    MAC_OS_VERSION_MAPPING.put("11", "Big Sur");
    MAC_OS_VERSION_MAPPING.put("12", "Monterey");
    MAC_OS_VERSION_MAPPING.put("13", "Ventura");
    MAC_OS_VERSION_MAPPING.put("14", "Sonoma");
    MAC_OS_VERSION_MAPPING.put("15", "Sequoia");
    MAC_OS_VERSION_MAPPING.put("26", "Tahoe");

    DARWIN_VERSION_MAPPING.put(5, "Puma");
    DARWIN_VERSION_MAPPING.put(6, "Jaguar");
    DARWIN_VERSION_MAPPING.put(7, "Panther");
    DARWIN_VERSION_MAPPING.put(8, "Tiger");
    DARWIN_VERSION_MAPPING.put(9, "Leopard");
    DARWIN_VERSION_MAPPING.put(10, "Snow Leopard");
    DARWIN_VERSION_MAPPING.put(11, "Lion");
    DARWIN_VERSION_MAPPING.put(12, "Mountain Lion");
    DARWIN_VERSION_MAPPING.put(13, "Mavericks");
    DARWIN_VERSION_MAPPING.put(14, "Yosemite");

    LINUX_VERSION_NAMES.addAll(Arrays.asList("Linux", "SunOS"));
  }

  /**
   * Resolves details of the current operating system.
   *
   * @return resolved operating system details
   * @since 10.0.0
   */
  public OperatingSystem resolve() {
    if (NAME.startsWith("Mac")) {
      return new OperatingSystem(NAME, VERSION, ARCH, resolveMacDistribution());
    } else if (NAME.startsWith("Darwin")) {
      return new OperatingSystem(NAME, VERSION, ARCH, resolveDarwinDistribution());
    } else {
      for (String name : LINUX_VERSION_NAMES) {
        if (NAME.startsWith(name)) {
          return new OperatingSystem(NAME, VERSION, ARCH, resolveLinuxDistribution());
        }
      }
    }
    return new OperatingSystem(NAME, VERSION, ARCH, NAME);
  }

  private String resolveDarwinDistribution() {
    String[] versions = VERSION.split("\\.");
    return "OS X " + DARWIN_VERSION_MAPPING.get(parseInt(versions[0])) + " (" + VERSION + ")";
  }

  private String resolveLinuxDistribution() {
    // The most likely is to have a LSB compliant distro
    String distribution = resolveNameFromLsbRelease();
    if (StringUtils.isNotBlank(distribution)) {
      return distribution;
    }
    // Generic Linux platform name
    distribution = resolveNameFromFile(FILE_ETC_SYSTEM_RELEASE);
    if (StringUtils.isNotBlank(distribution)) {
      return distribution;
    }
    var etcDirectory = Path.of(FILE_ETC);
    if (Files.exists(etcDirectory)) {
      // if generic 'system-release' file is not present, then try to find
      // another one
      distribution = resolveNameFromFile(findFile(etcDirectory, "-release"));
      if (StringUtils.isNotBlank(distribution)) {
        return distribution;
      }

      // if generic 'system-release' file is not present, then try to find
      // '_version'
      distribution = resolveNameFromFile(findFile(etcDirectory, "-_version"));
      if (StringUtils.isNotBlank(distribution)) {
        return distribution;
      }

      // try with /etc/issue file
      distribution = resolveNameFromFile(FILE_ETC_ISSUE);
      if (StringUtils.isNotBlank(distribution)) {
        return distribution;
      }
    }

    // if nothing found yet, looks for the version info
    var fileVersion = Path.of(FILE_PROC_VERSION);
    if (Files.exists(fileVersion)) {
      distribution = resolveNameFromFile(fileVersion.toString());
      if (StringUtils.isNotBlank(distribution)) {
        return distribution;
      }
    }

    return NAME;
  }

  private String resolveMacDistribution() {
    double version = parseDouble(VERSION);
    if (version < 10) {
      return "Mac OS " + VERSION;
    } else {
      return "OS X " + MAC_OS_VERSION_MAPPING.get(VERSION) + " (" + VERSION + ")";
    }
  }

  @Nullable
  private String resolveNameFromFile(@Nullable String filePath) {
    if (filePath == null) {
      return null;
    }
    var file = Path.of(filePath);
    if (!Files.exists(file)) {
      return null;
    }
    String lastLine = null;
    try (Scanner scanner = new Scanner(Files.newBufferedReader(file))) {
      int lineNb = 0;
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (lineNb++ == 0) {
          lastLine = line;
        }
        if (line.startsWith(PROP_PRETTY_NAME)) {
          return line.substring(13, line.length() - 1);
        }
      }
    } catch (IOException e) {
      log.warn(e);
    }
    return lastLine;
  }

  @Nullable
  private String resolveNameFromLsbRelease() {
    var lsbReleaseFile = Path.of(FILE_ETC_LSB_RELEASE);
    if (!Files.exists(lsbReleaseFile)) {
      return null;
    }
    String description = null;
    String codename = null;
    try (Scanner scanner = new Scanner(Files.newBufferedReader(lsbReleaseFile))) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.startsWith(PROP_DISTRIB_DESCRIPTION)) {
          description = line
              .replace(PROP_DISTRIB_DESCRIPTION + "=", "")
              .replace("\"", "");
        } else if (line.startsWith(PROP_DISTRIB_CODENAME)) {
          codename = line.replace(PROP_DISTRIB_CODENAME + "=", "");
        }
        if (description != null && codename != null) {
          return description + " (" + codename + ")";
        }
      }
    } catch (IOException e) {
      log.warn(e);
    }
    return null;
  }
  
  @Nullable
  private String findFile(Path directory, String postfix) {
    try (var stream = Files.list(directory)) {
      return stream
          .map(Path::toString)
          .filter(fileName -> fileName.endsWith(postfix))
          .findFirst()
          .orElse(null);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
