package com.infovergne.utils.text;

import org.apache.commons.lang3.StringUtils;

public class TextUtils {

	public static String smsify(String message) {
		if (message == null || message.isEmpty()) {
			return message;
		}
		message = " " + message;
		message = message.replaceAll("(?i)j'ai", "g");
		message = message.replaceAll("(?i) à ", " ");
		message = message.replaceAll("(?i) la ", " ");
		message = message.replaceAll("(?i) de ", " ");
		message = message.replaceAll("(?i) du ", " ");
		message = message.replaceAll("(?i) en ", " ");
		message = message.replaceAll("(?i) au ", " ");
		message = message.replaceAll("(?i) un ", " 1 ");
		message = message.replaceAll("(?i) fait ", " fé ");
		message = message.replaceAll("(?i) je ", " j ");
		message = message.replaceAll("(?i) faire ", " fR ");
		message = message.replaceAll("(?i) petit ", " pt ");
		message = message.replaceAll("(?i) grand ", " gd ");
		message = message.replaceAll("(?i) après ", " Ap ");
		message = message.replaceAll("(?i) long ", " Lg ");
		message = message.replaceAll("(?i) des ", " d ");
		message = message.replaceAll("(?i)clermont", "clt");
		message = message.replaceAll("(?i)ferrand", "fd");
		message = message.replaceAll("(?i) part[^ ]+", " part.");
		message = message.replaceAll("(?i) lum[^ ]+", " lum.");
		
		message = message.substring(1);
		return message;
	}
}
