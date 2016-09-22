package com.infovergne.rasp.lcd.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.infovergne.rasp.lcd.screen.AScreen;

public abstract class AMessageController extends Observable {
	
	private static final ScheduledExecutorService POOL = Executors.newSingleThreadScheduledExecutor(Executors.defaultThreadFactory());

	protected long delay;
	protected TimeUnit unit;
	protected final List<String> data = new ArrayList<String>();
	protected final AScreen screen;

	public AMessageController(AScreen screen, long delay, TimeUnit unit) {
		this.screen = screen;
		this.delay = delay;
		this.unit = unit;
	}
	
	protected abstract void sendMessage();
	
	public AMessageController add(String message) {
		data.add(message);
		return this;
	}

	public void start() {
		POOL.schedule(() -> finished(), delay, unit);
		sendMessage();
	}
	
	protected void finished() {
		setChanged();
		notifyObservers("end");
	}
	
}
