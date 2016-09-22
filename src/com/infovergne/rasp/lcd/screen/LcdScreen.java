package com.infovergne.rasp.lcd.screen;

import javax.swing.SwingConstants;

import com.infovergne.rasp.lcd.util.LcdUtils;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.Lcd;

public class LcdScreen extends AScreen {
	
	private static final char BLANK = ' ';

	private final int bits = 4;
	private int lcdHandle = -1;
	
	public LcdScreen(int rows, int cols) {
		super(rows, cols);
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
		if (lcdHandle == -1) {
			System.err.println("Failed to initialize LCD system");
			return;
		} else {
			System.out.println("LCD System initialized !");
		}
		initialized = true;
	}
	
	@Override
	public void cleanScreen() {
		Lcd.lcdClear(lcdHandle);
		try {
			Thread.sleep(1000);	
		} catch (Exception e) {
		}
	}
	
	@Override
	public void setMessageAt(int row, String message) {
		if (message == null) {
			message = "";
		}
		message = LcdUtils.buildRow(message, getCols(), SwingConstants.CENTER, BLANK);
		if (row < 0 || row > getRows()) {
			return;
		}
		Lcd.lcdPosition(lcdHandle, 0, row);
		Lcd.lcdPuts(lcdHandle, message);		
	}
	
	@Override
	public void initView() {
		Lcd.lcdHome(lcdHandle);
	}
	
	@Override
	public void commitView() {
		//
	}
	
}
