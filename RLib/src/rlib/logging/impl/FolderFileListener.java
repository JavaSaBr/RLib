package rlib.logging.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import rlib.logging.LoggerListener;

/**
 * Реализация слушателя логирования с записью в создаваемый файл в указанной
 * папке.
 * 
 * @author Ronn
 */
public class FolderFileListener implements LoggerListener {

	/** ссылка на папку, где нужно создать фаил для лога */
	private final File folder;

	/** записчик лога в фаил */
	private Writer writer;

	public FolderFileListener(File folder) {

		if(!folder.isDirectory()) {
			throw new IllegalArgumentException("file is not directory.");
		}

		if(!folder.exists()) {
			folder.mkdirs();
		}

		this.folder = folder;

	}

	/**
	 * @return записчик в фаил.
	 * @throws IOException
	 */
	public Writer getWriter() throws IOException {

		if(writer == null) {
			DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
			writer = new FileWriter(new File(folder, timeFormat.format(LocalTime.now()) + ".log"));
		}

		return writer;
	}

	@Override
	public void println(String text) {
		try {
			Writer writer = getWriter();
			writer.append(text);
			writer.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
