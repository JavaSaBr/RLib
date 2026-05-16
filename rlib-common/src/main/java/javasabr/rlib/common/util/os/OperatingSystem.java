package javasabr.rlib.common.util.os;

import org.jspecify.annotations.Nullable;

/**
 * Represents information about the current operating system.
 *
 * @since 10.0.0
 */
public record OperatingSystem(
    @Nullable String name, 
    @Nullable String version,
    @Nullable String arch,
    @Nullable String distribution) {
}
