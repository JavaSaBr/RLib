package javasabr.rlib.plugin.system;

import static java.lang.Math.min;

import java.util.stream.Stream;
import javasabr.rlib.common.util.ArrayUtils;

/**
 * Represents a version number with comparable segments (e.g., "1.2.3").
 *
 * @since 10.0.0
 */
public class Version implements Comparable<Version> {

  private final int[] segments;

  /**
   * Creates a new version from a version string.
   *
   * @param version the version string (e.g., "1.2.3")
   * @since 10.0.0
   */
  public Version(String version) {
    this.segments = parseSegments(version);
  }

  private int[] parseSegments(String stringVersion) {
    return Stream
        .of(stringVersion.split("\\."))
        .mapToInt(Integer::parseInt)
        .toArray();
  }

  @Override
  public int compareTo(Version other) {

    int[] otherSegments = other.segments;

    for (int i = 0, min = min(segments.length, otherSegments.length); i < min; i++) {
      if (segments[i] < otherSegments[i]) {
        return -1;
      } else if (segments[i] > otherSegments[i]) {
        return 1;
      }
    }

    if (otherSegments.length == segments.length) {
      return 0;
    }

    return otherSegments.length - segments.length;
  }

  @Override
  public String toString() {
    return ArrayUtils.toString(segments, ".", false, false);
  }
}
