package rlib.util.os;

/**
 * Класс для описание ОС.
 * 
 * @author Ronn
 */
public class OperatingSystem {

	/** название ОС */
	private String name;
	/** версия ОС */
	private String version;
	/** архитектура ОС */
	private String arch;
	/** название дистрибутива ОС */
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
	 * @return название дистрибутива ОС.
	 */
	public String getDistribution() {
		return distribution;
	}

	/**
	 * @return название ОС.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return версия ОС.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param arch архитектура ОС.
	 */
	public void setArch(final String arch) {
		this.arch = arch;
	}

	/**
	 * @param platform название дистрибутива ОС.
	 */
	public void setDistribution(final String platform) {
		this.distribution = platform;
	}

	/**
	 * @param name название ОС.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param version версия ОС.
	 */
	public void setVersion(final String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [name=" + name + ", version=" + version + ", arch=" + arch + ", distribution=" + distribution + "]";
	}
}
