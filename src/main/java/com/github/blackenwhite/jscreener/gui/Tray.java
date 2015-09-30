package com.github.blackenwhite.jscreener.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tray class represents background execution of the app in the tray
 * Created on 27.09.2015.
 */
public class Tray {
	private TrayIcon trayIcon;
	private Image trayImage;
	private GuiManager guiManager;

	// This is here: path="app.jar/TRAY_ICON_PATH"
	// in case if app.jar is within app-one-jar.jar: path="app-one-jar.jar/main/app.jar/TRAY_ICON_PATH"
	private static final String TRAY_ICON_PATH = "/tray_icon.png";

	public Tray(final GuiManager guiManager) {
		this.guiManager = guiManager;

		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			trayImage = GuiManager.createImageIcon(TRAY_ICON_PATH).getImage();

			ActionListener exitListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Exiting...");
					System.exit(0);
				}
			};

			PopupMenu popup = new PopupMenu();

			MenuItem helpItem = new MenuItem("Help");
			helpItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					guiManager.showHelpMessage();
				}
			});
			MenuItem configItem = new MenuItem("Configure");
			configItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					guiManager.showConfigManager();
				}
			});
			MenuItem exitItem = new MenuItem("Exit");
			exitItem.addActionListener(exitListener);
			popup.add(helpItem);
			popup.addSeparator();
			popup.add(configItem);
			popup.add(exitItem);

			trayIcon = new TrayIcon(trayImage, "JScreener", popup);

			ActionListener actionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					guiManager.showConfigManager();
				}
			};

			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(actionListener);

			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.err.println("TrayIcon could not be added.");
				System.exit(0);
			}

		} else {

			//  System Tray is not supported

		}
	}

	/**
	 * Returns tray image icon
	 */
	public Image getTrayImage() {
		return trayImage;
	}
}
