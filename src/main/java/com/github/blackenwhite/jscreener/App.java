package com.github.blackenwhite.jscreener;

import com.github.blackenwhite.jscreener.gui.GuiManager;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

/**
 * Runner class
 * Initializes listener and ui
 * Created on 25.09.2015.
 */
public class App  {

	/**
	 * Inits global key listener
	 */
	private static void initKeyListener() {
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}
		GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
	}

	/**
	 * Inits GUI
	 */
	private static void initGUI() {
		GuiManager guiManager = new GuiManager();
		guiManager.showConfigManager();
	}


	/**
	 * Starts two threads: listener and gui
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Thread listenerThread = new Thread(new Runnable() {
					@Override
					public void run() {
						initKeyListener();
					}
				});
				Thread guiThread = new Thread(new Runnable() {
					@Override
					public void run() {
						initGUI();
					}
				});

				listenerThread.start();
				guiThread.start();
			}
		});
	}
}
