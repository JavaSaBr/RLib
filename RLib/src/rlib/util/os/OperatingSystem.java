package rlib.util.os;

/**
 * Класс для описание ОС.
 *
 * @author JavaSaBr
 */
public class OperatingSystem {

    /**
     * Название ОС.
     */
    private String name;

    /**
     * Версия ОС.
     */
    private String version;

    /**
     * Архитектура ОС.
     */
    private String arch;

    /**
     * Название дистрибутива ОС.
     */
    private String distribution;

    public OperatingSystem() {
        final OperatingSystemResolver resolver = new OperatingSystemResolver();
        resolver.resolve(this);
    }

    /**
     * @return архитектура ОС.
     */
    public String getArch() {
        return arch;
    }

    /**
     * @param arch архитектура ОС.
     */
    public void setArch(final String arch) {
        this.arch = arch;
    }

    /**
     * @return название дистрибутива ОС.
     */
    public String getDistribution() {
        return distribution;
    }

    /**
     * @param platform название дистрибутива ОС.
     */
    public void setDistribution(final String platform) {
        this.distribution = platform;
    }

    /**
     * @return название ОС.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name название ОС.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return версия ОС.
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version версия ОС.
     */
    public void setVersion(final String version) {
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
