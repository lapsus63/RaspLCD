package com.infovergne.rasp.lcd.util;

import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringUtils;

public class LcdUtils {

	
	public static String buildRow(String s, int maxLen, int swingAlign, char filler) {
		if (s == null) {
			s = "";
		}
		if (s.length() > maxLen) {
			s = s.substring(0,maxLen);
		}
		if (swingAlign == SwingConstants.LEFT) {
			s = StringUtils.rightPad(s, maxLen, filler);
		} else if (swingAlign == SwingConstants.CENTER) {
			s = StringUtils.center(s, maxLen, filler);
		} else if (swingAlign == SwingConstants.RIGHT) {
			s = StringUtils.leftPad(s, maxLen, filler);
		}
		return s;
	}
}
