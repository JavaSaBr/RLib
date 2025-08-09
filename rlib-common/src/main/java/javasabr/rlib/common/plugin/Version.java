package javasabr.rlib.common.plugin;

import static java.lang.Math.min;

import java.util.stream.Stream;
import javasabr.rlib.common.util.ArrayUtils;
import org.jspecify.annotations.NullMarked;

/**
 * The class to present a version.
 *
 * @author JavaSaBr
 */
@NullMarked
public class Version implements Comparable<Version> {

  /**
   * The version segments.
   */
  private final int[] segments;

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
