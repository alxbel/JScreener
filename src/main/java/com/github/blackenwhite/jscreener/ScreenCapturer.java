package com.github.blackenwhite.jscreener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * ScreenCapturer class captures and saves screenshot
 * Created on 25.09.2015.
 */
public class ScreenCapturer {
	private static final String FILE_EXTENSION = "png";
	private static String screenshotName;
	private static String screenshotDirectory;

	// Init screens directory and name
	static {
		setFieldsFromCfg();
	}

	/**
	 * Reads config and set screens directory and name
 	 */
	public static void setFieldsFromCfg() {
		HashMap<String, String> data = Shared.readCfg();
		if (data != null) {
			screenshotName = data.get(Shared.FILENAME_MARK);
			screenshotDirectory = data.get(Shared.DIRECTORY_MARK);
		}
	}

	/**
	 * Captures and saves screenshot with unique name
	 */
	public static void captureScreen() {
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage capture = null;
		try {
			capture = new Robot().createScreenCapture(screenRect);
			ImageIO.write(capture, FILE_EXTENSION, new File(generateFilename()));
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates filename for the screenshot based on timestamp
	 * @return {@String filename}
	 */
	private static String generateFilename() {
		StringBuffer filename = new StringBuffer();
		GregorianCalendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);

		filename.append(screenshotDirectory).append("\\").append(screenshotName).
				append("_").append(year).append("-").append(month).append("-").append(day).append("-").
				append(hours).append("-").append(minutes).append("-").append(seconds).append(".").append(FILE_EXTENSION);

		return filename.toString();
	}
}
