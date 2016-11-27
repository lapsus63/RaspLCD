package com.infovergne.rasp.lcd.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingConstants;

import com.infovergne.rasp.lcd.screen.AScreen;

/**
 * Displays the messages with big letters and scrolls (if nb rows of the screen is handled).
 * All the messages from the list are concatenated, separated by as much spaces as the screen's width.
 * The last message is 2 screens width postfixed, before rotating back to begining.
 * <p>The speed of the scrolling is customizable.</p>
 * @author Olivier
 */
public class MarqueeLineController extends AMessageController {
	
	private final static String alpha4 = "/com/infovergne/rasp/lcd/message/alpha4.txt";
	
	private Long marqueeDelayMs = 50L;
	private boolean continueMarquee = true;
	private int charWidth = 1;
	private final Map<Character, List<String>> charMap = new HashMap<Character, List<String>>();
	
	public MarqueeLineController(AScreen screen, long marqueeDelayMs, long delay, TimeUnit unit) {
		super(screen, delay, unit);
		this.marqueeDelayMs = marqueeDelayMs;
		init();
	}
	
	private void init() {
		this.charWidth = 1;
		if (screen.getRows() == 4) {
			charWidth = 5;
			try (Scanner sc = new Scanner(getClass().getResourceAsStream(alpha4))) {
				completeMap(charMap, sc, 4, "abcdefghijklmnopqrstuvwxyz");
				completeMap(charMap, sc, 4, "0123456789");
				completeMap(charMap, sc, 4, " @:/!()");
			}
		} else {
			Scanner sc = new Scanner("abcdefghijklmnopqrstuvwxyz0123456789 @:/!()");
			completeMap(charMap, sc, 1, "abcdefghijklmnopqrstuvwxyz0123456789 @:/!()");
		}
	}

	private void completeMap(Map<Character, List<String>> map, Scanner sc, int letterHeight, String charMap) {
		for (int i = 0 ; i <  letterHeight ; i++) {
			String row = sc.nextLine();
			int j = 0;
			for (char c : charMap.toCharArray()) {
				String chunk = row.substring(j * charWidth, j * charWidth + charWidth);
				if (map.get(c) == null) {
					map.put(c, new ArrayList<String>());
				}
				map.get(c).add(chunk);
				j++;
			}
		}
	}
	
	private String mergeData() {
		StringBuilder sb = new StringBuilder();
		int whiteChars = screen.getCols() / charWidth;
		for (Tence d : data) {
			String message = d.getMessageASCII().toLowerCase();
			sb.append(message);
			sb.append(String.format("%1$" + whiteChars + "s", " "));
		}
		sb.append(String.format("%1$" + whiteChars + "s", " "));
		return sb.toString();
	}
	
	private List<Tence> getSlippedData(int slip, String mergedData) {
		String fix = mergedData.substring(0, slip);
		fix = mergedData.substring(slip) + fix;
		
		List<StringBuilder> rows = new ArrayList<StringBuilder>();
		for (char c : fix.toCharArray()) {
			List<String> letterDefinition = charMap.get(c);
			if (letterDefinition == null) {
				continue;
			}
			for (int i = 0 ; i < letterDefinition.size() ; i++) {
				if (i <= rows.size()) {
					rows.add(new StringBuilder());
				}
				rows.get(i).append(letterDefinition.get(i));
			}
		}
		
		List<Tence> newData = new ArrayList<Tence>();
		for (StringBuilder s : rows) {
			newData.add(new Tence(s.toString(), screen.getCols(), SwingConstants.LEFT, ' ', false));
		}
		return newData;
	}
	
	@Override
	public void sendMessage() {
		
		String merge = mergeData();
		new Thread("MarqueeLine") {
			@Override
			public void run() {
				int slip = 0;
				while (continueMarquee) {
					List<Tence> newData = getSlippedData(slip, merge);
					try { Thread.sleep(marqueeDelayMs); } catch (Throwable t) { }
					screen.sendMessage(true, newData.toArray(new Tence[0]));
					slip++;
					slip = slip % merge.length();
				}
				MarqueeLineController.super.finished();
			}
		}.start();
	}
	
	@Override
	public void finished() {
		continueMarquee = false;
	}
	
}
