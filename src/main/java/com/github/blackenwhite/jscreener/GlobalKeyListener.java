package com.github.blackenwhite.jscreener;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * GlobalKeyListener class implements NativeKeyListener interface
 * from jnativehook library by kwhat (https://github.com/kwhat/jnativehook)
 * Created on 27.09.2015.
 */
public class GlobalKeyListener implements NativeKeyListener {

	public void nativeKeyPressed(NativeKeyEvent e) {
		System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

		// Here we capture the screen when user hits print_screen
		if (e.getKeyCode() == NativeKeyEvent.VC_PRINTSCREEN) {
			ScreenCapturer.captureScreen();
		}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
	}

}
