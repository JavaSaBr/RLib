package rlib.logging;

/**
 * Перечисление уровней логгирования.
 * 
 * @author Ronn
 */
public enum LoggerLevel {
	INFO("INFO"),
	DEBUG("DEBUG"),
	WARNING("WARNING"),
	ERROR("ERROR");

	public static final int LENGTH = values().length;

	/** титул сообщения */
	private String title;

	/** активен ли уровень */
	private boolean enabled;

	private LoggerLevel(final String title) {
		this.title = title;
	}

	/**
	 * @return титул сообщения.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return активен ли уровень.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled активен ли уровень.
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param title титул сообщения.
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return title;
	}
}
