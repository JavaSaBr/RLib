package com.ss.rlib.common.plugin;

import static java.lang.Math.min;
import com.ss.rlib.common.util.ArrayUtils;
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

    public Version(@NotNull String version) {
        this.segments = parseSegments(version);
    }

    @NotNull
    private int[] parseSegments(@NotNull String stringVersion) {
        return Stream.of(stringVersion.split("\\."))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    @Override
    public int compareTo(@NotNull Version other) {

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
