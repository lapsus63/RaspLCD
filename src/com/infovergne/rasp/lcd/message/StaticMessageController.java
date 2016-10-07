package com.infovergne.rasp.lcd.message;

import java.util.concurrent.TimeUnit;

import com.infovergne.rasp.lcd.screen.AScreen;

/**
 * Simple text.
 * If the line is larger than the screen width, it is truncated.
 * @author Olivier
 */
public class StaticMessageController extends AMessageController {

	public StaticMessageController(AScreen screen, long delay, TimeUnit unit) {
		super(screen, delay, unit);
	}

	@Override
	public void sendMessage() {
		screen.sendMessage(true, data.toArray(new Tence[0]));
	}
}
