package javasabr.rlib.common.util.os;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents information about the current operating system.
 *
 * @since 10.0.0
 */
@NullMarked
public class OperatingSystem {

  private String name;
  private String version;
  private String arch;
  private String distribution;

  public OperatingSystem() {
    final OperatingSystemResolver resolver = new OperatingSystemResolver();
    resolver.resolve(this);
  }

  public String getArch() {
    return arch;
  }

  public void setArch(final String arch) {
    this.arch = arch;
  }

  @Nullable
  public String getDistribution() {
    return distribution;
  }

  public void setDistribution(@Nullable final String distribution) {
    this.distribution = distribution;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(final String version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return "OperatingSystem{" + "name='" + name + '\'' + ", version='" + version + '\'' + ", arch='" + arch + '\''
        + ", distribution='" + distribution + '\'' + '}';
  }
}
