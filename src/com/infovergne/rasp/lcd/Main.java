package com.infovergne.rasp.lcd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.infovergne.rasp.lcd.screen.AScreen;
import com.infovergne.rasp.lcd.screen.LcdScreen;
import com.infovergne.rasp.lcd.screen.OutScreen;
import com.pi4j.wiringpi.Lcd;

public class Main {

	public static final void main(String... args) {
		Main main = new Main(args);
		main.start();
	}
	
	private final List<String> cliArgs = new ArrayList<String>();
	
	private AScreen screen = null;
	
	public Main(String... args) {
		parseArgs(args);
		System.out.println("Checking Lcd Lib.... " + Lcd.class);
	}
	
	private void start() {
		getScreen().initialize();
		if (getScreen().isInitialized()) {
			System.out.println("LCD correctly initialized :-)");
			new DialogController(getScreen()).start();
		} else {
			System.err.println("Failed to initialize LCD system properly");
		}
		
	}
	
	private void parseArgs(String... args) {
		if (args == null || args.length == 0) {
			return;
		}
		this.cliArgs.addAll(Arrays.asList(args));
	}
	
	private AScreen getScreen() {
		if (screen == null) {
			if (cliArgs.contains("-console")) {
				screen = new OutScreen(4, 20);
			} else {
				screen = new LcdScreen(4, 20);
			}
		}
		return screen;
	}
}
