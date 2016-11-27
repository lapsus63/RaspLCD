package com.infovergne.utils.text;


public class TextUtils {

	public static String smsify(String message) {
		if (message == null || message.isEmpty()) {
			return message;
		}
		message = " " + message;
		message = message.replaceAll("(?i)j'ai", "g");
		message = message.replaceAll("(?i) un ", " 1 ");
		message = message.replaceAll("(?i) fait ", " ft ");
		message = message.replaceAll("(?i) je ", " j ");
		message = message.replaceAll("(?i) faire ", " fR ");
		message = message.replaceAll("(?i) petit ", " pt ");
		message = message.replaceAll("(?i) grand ", " gd ");
		message = message.replaceAll("(?i) grand ", " gd ");
		message = message.replaceAll("(?i) plus ", " + ");
		message = message.replaceAll("(?i) moins ", " - ");
		message = message.replaceAll("(?i) fois ", " * ");
		message = message.replaceAll("(?i) long ", " lg ");
		message = message.replaceAll("(?i) des ", " d ");
		message = message.replaceAll("(?i)clermont", "Clt");
		message = message.replaceAll("(?i) att[^ ]+", " att.");
		message = message.replaceAll("(?i) tempo[^ ]+", " temp.");
		message = message.replaceAll("(?i)ferrand", "Fd");
		message = message.replaceAll("(?i)auvergne", "auv");
		message = message.replaceAll("(?i)mauvais", "mv");
		message = message.replaceAll("(?i) part[^ ]+", " part.");
		message = message.replaceAll("(?i) lum[^ ]+", " lum.");
		
		message = message.substring(1);
		return message;
	}
}
