package com.infovergne.rasp.lcd.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingConstants;

import com.infovergne.rasp.lcd.screen.AScreen;

/**
 * Reads an ASCII animation file and plays it as a marquee 
 * <p>The speed of the scrolling is customizable.</p>
 * @author Olivier
 */
public class AsciiArtController extends AMessageController {
	
	private Long marqueeDelayMs = 50L;
	private boolean continueMarquee = true;
	private final List<String> animationModel = new ArrayList<String>();
	private int frameIndex = 0;
	
	public AsciiArtController(AScreen screen, long marqueeDelayMs, long delay, TimeUnit unit, String animationFile) {
		super(screen, delay, unit);
		this.marqueeDelayMs = marqueeDelayMs;
		try (InputStream stream = AsciiArtController.class.getResourceAsStream(animationFile)) {
			new BufferedReader(new InputStreamReader(stream)).lines().forEach(s -> animationModel.add(s));
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
	
	private List<Tence> getNextTences() {
		int rows = screen.getRows() + 1 /* 1 row for separation */;
		int readFrom = 0;
		int readTo = frameIndex * rows + (rows - 1);
		if (readTo > animationModel.size()) {
			frameIndex = 0;
			readTo = frameIndex * rows + (rows - 1);
		}
		readFrom = readTo - rows + 1;
		
		List<Tence> newData = new ArrayList<Tence>();
		for (int i = readFrom ; i < readTo ; i++) {
			String row = animationModel.get(i).replace("_", " ");
			Tence rowTence = new Tence(row, screen.getCols(), SwingConstants.LEFT, ' ', false);
			newData.add(rowTence);
		}
		return newData;
	}
	
	@Override
	public void sendMessage() {
		
		new Thread("MarqueeLine") {
			@Override
			public void run() {
				while (continueMarquee) {
					List<Tence> newData = getNextTences();
					frameIndex++;
					try { Thread.sleep(marqueeDelayMs); } catch (Throwable t) { }
					screen.sendMessage(true, newData.toArray(new Tence[0]));
				}
				AsciiArtController.super.finished();
			}
		}.start();
	}
	
	@Override
	public void finished() {
		continueMarquee = false;
	}
	
}
