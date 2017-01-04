package rlib.util.os;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * THe class with description of an Operation System.
 *
 * @author JavaSaBr
 */
public class OperatingSystem {

    /**
     * The name.
     */
    private String name;

    /**
     * The version.
     */
    private String version;

    /**
     * The arch.
     */
    private String arch;

    /**
     * The distribution.
     */
    private String distribution;

    public OperatingSystem() {
        final OperatingSystemResolver resolver = new OperatingSystemResolver();
        resolver.resolve(this);
    }

    /**
     * @return the arch.
     */
    @NotNull
    public String getArch() {
        return arch;
    }

    /**
     * @param arch the arch.
     */
    public void setArch(@NotNull final String arch) {
        this.arch = arch;
    }

    /**
     * @return the distribution.
     */
    @Nullable
    public String getDistribution() {
        return distribution;
    }

    /**
     * @param distribution the distribution.
     */
    public void setDistribution(@Nullable final String distribution) {
        this.distribution = distribution;
    }

    /**
     * @return the name.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * @param name the name.
     */
    public void setName(@NotNull final String name) {
        this.name = name;
    }

    /**
     * @return the version.
     */
    @NotNull
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version.
     */
    public void setVersion(@NotNull final String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "OperatingSystem{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", arch='" + arch + '\'' +
                ", distribution='" + distribution + '\'' +
                '}';
    }
}
