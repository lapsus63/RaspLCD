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
	
	public Tence(String message, int maxLen, int swingAlign, char filler) {
		this.message = message;
		this.maxLen = maxLen;
		this.swingAlign = swingAlign;
		this.filler = filler;
	}

	@Override
	public String toString() {
		String s = message.replaceAll("\\s", " ");
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
		return message;
	}

	public int getMaxLen() {
		return maxLen;
	}
	
}
