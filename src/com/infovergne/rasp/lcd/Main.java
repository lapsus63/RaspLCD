package com.infovergne.rasp.lcd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.slf4j.LoggerFactory;

import com.infovergne.rasp.lcd.screen.AScreen;
import com.infovergne.rasp.lcd.screen.LcdScreen;
import com.infovergne.rasp.lcd.screen.OutScreen;
import com.infovergne.rasp.lcd.screen.SwingScreen;
import com.pi4j.wiringpi.Lcd;

/**
 * Startup class.
 * Use -console to enable simulation mode (uses System.out instead of sending GPIO commands).
 * 
 * @author Olivier
 */
public class Main {
	
	private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static final void main(String... args) {
		initLog4j();
		Main main = new Main(args);
		main.start();
	}
	
	private static void initLog4j() {
		Logger root = Logger.getRootLogger();
		Layout layout = new PatternLayout("%p %d{yyyy-MM-dd HH:mm:ss,SSS} [%t: (%F:%L)] %m%n");
		root.addAppender(new ConsoleAppender(layout, ConsoleAppender.SYSTEM_OUT));
		root.setLevel(Level.INFO);
	}
	
	private final List<String> cliArgs = new ArrayList<String>();
	
	private AScreen screen = null;
	
	public Main(String... args) {
		parseArgs(args);
		LOGGER.debug("Checking Lcd Lib.... " + Lcd.class);
	}
	
	private void start() {
		getScreen().initialize();
		if (getScreen().isInitialized()) {
			LOGGER.debug("LCD correctly initialized :-)");
			new DialogController(getScreen()).start();
		} else {
			LOGGER.error("Failed to initialize LCD system properly");
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
			} else if (cliArgs.contains("-swing")) {
				screen = new SwingScreen(4, 20);
			} else {
				screen = new LcdScreen(4, 20);
			}
		}
		return screen;
	}
}
