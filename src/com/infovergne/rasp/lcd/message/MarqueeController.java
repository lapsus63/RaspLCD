package com.infovergne.rasp.lcd.message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.infovergne.rasp.lcd.screen.AScreen;

public class MarqueeController extends AMessageController {
	
	private int marqueeWidth = 40;
	private Long marqueeDelayMs = 500L;
	private boolean continueMarquee = true;
	
	public MarqueeController(AScreen screen, int marqueeWidth, long marqueeDelayMs, long delay, TimeUnit unit) {
		super(screen, delay, unit);
		this.marqueeWidth = marqueeWidth;
		this.marqueeDelayMs = marqueeDelayMs;
	}
	
	private List<String> getSlippedData(int slip) {
		List<String> newData = new ArrayList<String>();
		for (String s : data) {
			String fix = s.substring(0, slip);
			s = s.substring(slip) + fix;
			newData.add(s);
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
					List<String> newData = getSlippedData(slip);
					try { Thread.sleep(marqueeDelayMs); } catch (Throwable t) { }
					screen.sendMessage(true, newData.toArray(new String[0]));
					slip++;
					slip = slip % marqueeWidth;
				}
			}
		}.start();
		
	}
	
	@Override
	public void finished() {
		continueMarquee = false;
		super.finished();
	}
	
}
