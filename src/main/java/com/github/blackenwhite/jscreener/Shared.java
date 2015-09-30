package com.github.blackenwhite.jscreener;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Created on 29.09.2015.
 */
public class Shared {
	public static final String CONFIG_FILE = "config.cfg";
	public static final String DIRECTORY_MARK = "directory";
	public static final String FILENAME_MARK = "name";
	public static final String SEPARATOR = "::";

	public static HashMap<String, String> readCfg() {
		HashMap<String, String> data = new HashMap<>();

		if (!(new File(CONFIG_FILE).exists())) {
			return null;
		}

		try (BufferedReader reader =
					 new BufferedReader(new FileReader(CONFIG_FILE))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(DIRECTORY_MARK + SEPARATOR)) {
					String directory = line.replaceAll(String.format("%s|%s", DIRECTORY_MARK, SEPARATOR), "");
					Path path = Paths.get(directory);
					if (!Files.exists(path)) {
						return null;
					}
					data.put(DIRECTORY_MARK, directory);
				} else if (line.startsWith(FILENAME_MARK + SEPARATOR)) {
					String filename = line.replaceAll(String.format("%s|%s", FILENAME_MARK, SEPARATOR), "");
					data.put(FILENAME_MARK, filename);
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		return data;
	}

	public static void writeCfg(String directory, String filename) {
		try {
			File file = new File(CONFIG_FILE);
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			StringBuffer line = new StringBuffer();
			line.append(DIRECTORY_MARK).append(SEPARATOR).append(directory);
			bw.write(line.toString());
			bw.write(System.getProperty("line.separator"));
			line.delete(0, line.length());
			line.append(FILENAME_MARK).append(SEPARATOR).append(filename);
			bw.write(line.toString());

			bw.close();
			fw.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path,
											   String description) {
		java.net.URL imgURL = Shared.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public static void main(String[] args) {
		readCfg();
		writeCfg("dir111", "file1111");
		readCfg();
	}
}
