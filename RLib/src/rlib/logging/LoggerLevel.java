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

	private LoggerLevel(String title) {
		this.title = title;
	}

	/**
	 * @return титул сообщения.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title титул сообщения.
	 */
	public void setTitle(String title) {
		this.title = title;
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
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return title;
	}
}
