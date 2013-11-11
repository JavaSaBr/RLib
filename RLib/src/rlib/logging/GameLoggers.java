package rlib.logging;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import rlib.util.array.Array;
import rlib.util.array.Arrays;

/**
 * Менеджер для создания игровых логгеров.
 * 
 * @author Ronn
 */
public abstract class GameLoggers {

	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

	/** директория лога */
	private static String directory;

	/** списоксозданных логеров */
	private static Array<GameLogger> loggers;

	/**
	 * Завершение работы всех логгеров.
	 */
	public static final void finish() {
		for(GameLogger logger : loggers) {
			logger.finish();
		}
	}

	/**
	 * Создает индивидуальный логгер с указаным именем.
	 * 
	 * @param name имя объекта который запрашивает логгер
	 * @return новый индивидуальный логгер
	 */
	public static final ByteGameLogger getByteLogger(String name) {

		File dir = new File(directory, name);

		if(!dir.exists()) {
			dir.mkdir();
		}

		if(!dir.isDirectory()) {
			throw new IllegalArgumentException("incorrect directory for game logger " + name);
		}

		File outFile = new File(dir.getAbsolutePath() + "/" + TIME_FORMAT.format(new Date()) + ".gamelog");

		try {
			ByteGameLogger logger = new ByteGameLogger(outFile);
			loggers.add(logger);
			return logger;
		} catch(IOException e) {
			throw new IllegalArgumentException("incorrect create log file for game logger " + name);
		}
	}

	/**
	 * Создает индивидуальный логгер с указаным именем.
	 * 
	 * @param name имя объекта который запрашивает логгер
	 * @return новый индивидуальный логгер
	 */
	public static final StringGameLogger getLogger(String name) {

		File dir = new File(directory + "/" + name);

		if(!dir.exists()) {
			dir.mkdir();
		}

		if(!dir.isDirectory()) {
			throw new IllegalArgumentException("incorrect directory for game logger " + name);
		}

		File outFile = new File(dir.getAbsolutePath() + "/" + TIME_FORMAT.format(new Date()) + ".gamelog");

		try {
			StringGameLogger logger = new StringGameLogger(outFile);
			loggers.add(logger);
			return logger;
		} catch(IOException e) {
			throw new IllegalArgumentException("incorrect create log file for game logger " + name);
		}
	}

	/**
	 * @param directory адресс директори лог папки.
	 */
	public static final void setDirectory(String directory) {
		GameLoggers.directory = directory;
		GameLoggers.loggers = Arrays.toConcurrentArray(GameLogger.class);
	}
}
