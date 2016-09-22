package com.infovergne.rasp.lcd.screen;

import java.util.Arrays;

import javax.swing.SwingConstants;

import com.infovergne.rasp.lcd.util.LcdUtils;

public class OutScreen extends AScreen {
	
	private static final char BLANK = '@';

	private final char[][] screen;
	
	public OutScreen(int rows, int cols) {
		super(rows, cols);
		this.screen = new char[rows][cols];
	}
	
	@Override
	public void cleanScreen() {
		for (int i = 0 ; i < rows ; i++) {
			Arrays.fill(screen[i], BLANK);
		}
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
		char[] cars = message.toCharArray();
		for (int col = 0 ; col < cars.length && col < getCols(); col++) {
			char c = cars[col];
			screen[row][col] = c;
		}
	}

	@Override
	public void initView() {
		for (int i = 0 ; i < 20 ; i++) 
			System.out.println();
	}
	
	@Override
	public void commitView() {
		for (int row = 0 ; row < rows ; row++) {
			System.out.println(new String(screen[row]));
		}
	}

}
