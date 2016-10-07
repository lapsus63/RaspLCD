package com.infovergne.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Common tools for loading miscellanous APIs.
 * API keys and credentials are read from user.home/.rasplcd/apikeys.conf.
 * @author Olivier
 */
public class APICommons {

	private final static String API_FILE = System.getProperty("user.home") + File.separator + ".rasplcd" + File.separator + "apikeys.conf";

	private static Properties properties = null;

	/** @return the API key needed to call OpenWeatherMap API */
	public static String getApiKey(String key) {
		loadProperties();
		if (properties == null || properties.get(key) == null) {
			System.err.println("API key not found : " + key);
			return null;
		}
		return properties.get(key).toString();
	}

	/** @return the API key needed to call OpenWeatherMap API */
	public static void loadProperties() {
		if (properties != null) {
			return;
		}
		properties = new Properties();
		Path propPath = Paths.get(API_FILE);
		File propFile = new File(API_FILE);
		if (!Files.isRegularFile(propPath)) {
			System.err.println("API key file not found " + API_FILE);
			return;
		}
		try (FileInputStream fis = new FileInputStream(propFile)) {
			properties.load(fis);
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

}
