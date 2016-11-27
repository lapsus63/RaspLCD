package com.infovergne.rasp.lcd.message;

import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringUtils;

/**
 * Defines a message. 
 * <p>A message is a text, a maximum length, an align attribute, and a filler char (to fit max length)</p>
 * @author Olivier
 */
public class Tence {

	private final String message;
	private final int maxLen;
	private final int swingAlign;
	private final char filler;
	private final boolean cleanSpaces;
	
	public Tence(String message, int maxLen, int swingAlign, char filler) {
		this(message, maxLen, swingAlign, filler, true);
	}

	public Tence(String message, int maxLen, int swingAlign, char filler, boolean cleanSpaces) {
		this.cleanSpaces = cleanSpaces;
		this.message = message;
		this.maxLen = maxLen;
		this.swingAlign = swingAlign;
		this.filler = filler;
	}

	@Override
	public String toString() {
		String s = getMessageASCII();
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

	public char getFiller() {
		return filler;
	}

	public int getSwingAlign() {
		return swingAlign;
	}

	public String getMessage() {
		return message;
	}

	public String getMessageASCII() {
		String message = this.message;
		message = StringUtils.stripAccents(message);
		message = message.replace('°', (char)39);
		StringBuilder ascii = new StringBuilder();
		for (char c : message.toCharArray()) {
			if (c == '#') {
				ascii.append(String.valueOf((char)127));
			} else if (c < 127) {
				ascii.append(String.valueOf(c));
			} else if (c == '°') {
				ascii.append(String.valueOf((char)39));
			} else {
				ascii.append(String.valueOf(' '));
			}
		}
		message = ascii.toString();
		if (cleanSpaces) {
				message= message.replaceAll("\\s+", " ");
		}
		return message;
	}

	public int getMaxLen() {
		return maxLen;
	}
	
}
