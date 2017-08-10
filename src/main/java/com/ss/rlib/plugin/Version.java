package com.ss.rlib.plugin;

import static java.lang.Math.min;
import com.ss.rlib.util.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * The class to present a version.
 *
 * @author JavaSaBr
 */
public class Version implements Comparable<Version> {

    /**
     * The version segments.
     */
    @NotNull
    private final int[] segments;

    public Version(@NotNull final String version) {
        this.segments = parseSegments(version);
    }

    @NotNull
    private int[] parseSegments(@NotNull final String stringVersion) {
        return Stream.of(stringVersion.split("\\."))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    @Override
    public int compareTo(@NotNull final Version other) {

        final int[] otherSegments = other.segments;

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
