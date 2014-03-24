package rlib.util;

import java.io.File;

/**
 * Конструктор файловых путей.
 * 
 * @author Ronn
 */
public class PathBuilder {

	/** итоговый путь */
	private final StringBuilder builder;

	public PathBuilder(String fullpath) {
		this.builder = new StringBuilder(fullpath);
	}

	/**
	 * Добавление к пути новых фрагментов.
	 * 
	 * @param path добавочный кусок пути.
	 */
	public PathBuilder append(String path) {

		if(StringUtils.isEmpty(path)) {
			throw new RuntimeException("incorrect path.");
		}

		builder.append(File.separatorChar).append(path);
		return this;
	}

	/**
	 * @return конструктор пути.
	 */
	private StringBuilder getBuilder() {
		return builder;
	}

	/**
	 * @return итоговый текущий путь.
	 */
	public String getPath() {
		return builder.toString();
	}

	@Override
	public String toString() {
		return builder.toString();
	}
}
