package com.infovergne.rasp.lcd;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.Lcd;

public class LcdController {
	
	private final int rows;
	private final int cols;
	private final int bits = 4;

	private int lcdHandle = -1;
	private boolean initialized = false;
	
	public LcdController(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	public void initialize() {
		if (Gpio.wiringPiSetup() == -1) {
			System.err.println("Failed to wire GPIO");
			return;
		} else {
			System.out.println("GPIO wired !");
		}
		this.lcdHandle = Lcd.lcdInit(rows, // number of row supported by LCD
				cols, // number of columns supported by LCD
				bits, // number of bits used to communicate to LCD
				11, // LCD RS pin
				10, // LCD strobe pin
				6, // 0, // LCD data bit 1
				5, // 1, // LCD data bit 2
				4, // 2, // LCD data bit 3
				1, // 3, // LCD data bit 4
//				0, // LCD data bit 1
//				1, // LCD data bit 2
//				2, // LCD data bit 3
//				3, // LCD data bit 4
//				
				0, // LCD data bit 5 (set to 0 if using 4 bit communication)
				0, // LCD data bit 6 (set to 0 if using 4 bit communication)
				0, // LCD data bit 7 (set to 0 if using 4 bit communication)
				0); // LCD data bit 8 (set to 0 if using 4 bit communication)
		if (this.lcdHandle == -1) {
			System.err.println("Failed to initialize LCD system");
			return;
		} else {
			System.out.println("LCD System initialized !");
		}
		initialized = true;
	}
	
	public void sendMessage(boolean clear, String... messages) {
		if (!initialized) {
			System.err.println("LCD system not properly initialized ; run initialize at first.");
			return;
		}
		if (clear) {
			Lcd.lcdClear(lcdHandle);
			try {
				Thread.sleep(1000);	
			} catch (Exception e) {
				
			}
		}
		
		Lcd.lcdHome(lcdHandle);
		if (messages != null) {
			for (int  i = 0 ; i < messages.length && i < rows ; i++) {
				String message = messages[i] == null ? "" : messages[i].trim();
				Lcd.lcdPosition(lcdHandle, 0, i);
				Lcd.lcdPuts(lcdHandle, message);
			}
		}
		try { Thread.sleep(3000); } catch (Exception e) {}
	}
	
}
