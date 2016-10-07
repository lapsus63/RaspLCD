package com.infovergne.api.openwm;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.infovergne.api.APICommons;

/**
 * <p>Weather API. Grabs 5 days forecast.</p>
 * <p> API url : http://api.openweathermap.org</p>
 * <p>Need to configure your API key in user.home/.rasplcd/apikeys.conf : <code>OWM = xxxx</code></p>
 * @author Olivier
 */
public class OpenWeatherMap {
	
	private final static String API_KEY = "OWM";

	private final static String URL_GET_BY_CITYNAME = "http://api.openweathermap.org/data/2.5/forecast?q={city},{country}&mode=json&cnt=1&units=metric&lang=fr&APPID={api}";
	
	private final static int BUFFER = 1024;
	
	@SuppressWarnings("unused")
	/* For debugging purposes */
	private static String getSample() throws IOException {
		try (Scanner sc = new Scanner(OpenWeatherMap.class.getResourceAsStream("/com/infovergne/api/openwm/owm.sample"))) {
			sc.useDelimiter("\\Z");
			return sc.next();
		}
	}
	
	/** @return 5 days forecast in JSON format */
	public static final JsonElement getForecast(String city, String countryCode) {
		String api = APICommons.getApiKey(API_KEY);
		JsonElement retour = null;
		if (api == null) {
			return retour;
		}
		try {
			String http = URL_GET_BY_CITYNAME.replace("{city}", city).replace("{country}", countryCode).replace("{api}", api);
			URL url = new URL(http);
			URLConnection conn = url.openConnection();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			StringBuffer sb = new StringBuffer(); // getSample());
			byte[] buff = new byte[BUFFER];
			int lu = 0;
			while ((lu = bis.read(buff)) > 0) {
				byte[] chunk = buff;
				if (lu < BUFFER) {
					chunk = Arrays.copyOf(buff, lu);
				}
				sb.append(new String(chunk, StandardCharsets.UTF_8));
			}
			retour = new JsonParser().parse(sb.toString());
		} catch (IOException io) {
			io.printStackTrace();
		}
		return retour;
	}

}
