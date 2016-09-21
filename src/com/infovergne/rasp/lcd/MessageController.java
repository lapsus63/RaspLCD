package com.infovergne.rasp.lcd;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.pi4j.util.StringUtil;

public class MessageController extends Observable {

	private long delay;
	private TimeUnit unit;
	
	private final List<String> data = new ArrayList<String>();
	private final LcdController ctrl;

	public MessageController(LcdController ctrl, long delay, TimeUnit unit, String ... rows) {
		this.ctrl = ctrl;
		this.delay = delay;
		this.unit = unit;
		initData(rows);
	}
	
	private void initData(String[] rows) {
		if (rows == null) rows = new String[0];
		for (int i = 0 ; i < 4 ; i++) {
			String raw = i < rows.length ? rows[i] : "";
			if (raw.isEmpty()) {
			} else if (raw.length() > 20) {
				raw = raw.substring(0,20);
			} else {
				char first = raw.toCharArray()[0];
				raw = StringUtil.padCenter(raw, first, 20);
			}
			this.data.add(raw);
		}
	}

	public void start() {
		ctrl.sendMessage(true, data.toArray(new String[0]));
		Executors.newSingleThreadScheduledExecutor().schedule(() -> finished(), delay, unit);
	}
	
	private void finished() {
		setChanged();
		notifyObservers("end");
	}
	
}
