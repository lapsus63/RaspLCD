package com.infovergne.api.ipapi;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Example or result using JSON format :
 * <p>{"as":"AS12322 PROXAD","city":"Clermont-Ferrand","country":"France","countryCode":"FR","isp":"Free SAS","lat":45.7797,"lon":3.0863,"org":"Free SAS","query":"88.187.102.96","region":"63","regionName":"Puy-de-Dôme","status":"success","timezone":"Europe/Paris","zip":"63100"}</p>
 * @author Olivier
 * @see <p>API url : http://ip-api.com/json</p>
 */
public class IpAPI {

	private final static String URL_API = "http://ip-api.com/json";
	
	private final static int BUFFER_SIZE = 1024;

	/** @return current place information based on your IP */
	public static JsonElement lookupTown() {
		JsonElement retour = null;
		try {
			URL url = new URL(URL_API);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			StringBuffer sb = new StringBuffer();
			byte[] buff = new byte[BUFFER_SIZE];
			int lu = 0;
			while ((lu = bis.read(buff)) > 0) {
				byte[] subarray = buff;
				if (lu < BUFFER_SIZE) {
					subarray = Arrays.copyOf(buff, lu);
				}
				sb.append(new String(subarray, StandardCharsets.UTF_8));
			}
			retour = new JsonParser().parse(sb.toString());
		} catch (IOException io) {
			io.printStackTrace();
		}
		return retour;
	}
	
}
