package com.infovergne.utils.json;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Json tool to use syntax like XPath to grab data from Json strings.
 * @author Olivier
 */
public class JsonUtils {

	// http://stackoverflow.com/questions/36257106/extract-value-from-json-using-gson
	public static JsonElement getAtPath(JsonElement e, String path) {
		JsonElement current = e;
		String ss[] = path.split("/|\\[|\\]");
		for (int i = 0; i < ss.length; i++) {
			if (StringUtils.isEmpty(ss[i])) {
				continue;
			}
			if (current instanceof JsonObject) {
				current = current.getAsJsonObject().get(ss[i]);
			} else if (current instanceof JsonArray) {
				if (NumberUtils.isDigits(ss[i])) {
					current = current.getAsJsonArray().get(Integer.parseInt(ss[i]));
				} else {
					JsonElement jsonElement = current.getAsJsonArray().get(0);
					current = jsonElement.getAsJsonObject().get(ss[i]);
				}
			}
		}
		return current;
	}
}
