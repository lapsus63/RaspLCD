package com.infovergne.rasp.lcd.message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.infovergne.rasp.lcd.screen.AScreen;

/**
 * Displays n lines of messages and scrolls horizontally.
 * The speed of scrolling can be customized.
 * @author Olivier
 */
public class MarqueeController extends AMessageController {
	
	private int marqueeWidth = 40;
	private Long marqueeDelayMs = 500L;
	private boolean continueMarquee = true;
	
	public MarqueeController(AScreen screen, int marqueeWidth, long marqueeDelayMs, long delay, TimeUnit unit) {
		super(screen, delay, unit);
		this.marqueeWidth = marqueeWidth;
		this.marqueeDelayMs = marqueeDelayMs;
	}
	
	private List<Tence> getSlippedData(int slip) {
		List<Tence> newData = new ArrayList<Tence>();
		for (Tence s : data) {
			String fix = s.toString().substring(0, slip);
			fix = s.toString().substring(slip) + fix;
			newData.add(new Tence(fix, s.getMaxLen(), s.getSwingAlign(), s.getFiller()));
		}
		return newData;
	}
	
	@Override
	public void sendMessage() {
		new Thread("Marquee") {
			
			@Override
			public void run() {
				int slip = 0;
				while (continueMarquee) {
					List<Tence> newData = getSlippedData(slip);
					try { Thread.sleep(marqueeDelayMs); } catch (Throwable t) { }
					screen.sendMessage(true, newData.toArray(new Tence[0]));
					slip++;
					slip = slip % marqueeWidth;
				}
				MarqueeController.super.finished();
			}
		}.start();
		
	}
	
	@Override
	public void finished() {
		continueMarquee = false;
	}
	
}
