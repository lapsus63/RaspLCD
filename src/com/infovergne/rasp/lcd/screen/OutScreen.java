package com.infovergne.rasp.lcd.screen;

import javax.swing.SwingConstants;

import com.infovergne.rasp.lcd.message.Tence;

/**
 * Simulates an LCD screen using standard output.
 * Useful for development.
 * <p>You can activate this screen passing -console argument to command line</p>
 * @author Olivier
 */
public class OutScreen extends AScreen {
	
	private static final char BLANK = '@';

	private final char[][] screen;
	
	public OutScreen(int rows, int cols) {
		super(rows, cols);
		this.screen = new char[rows][cols];
	}
	
	@Override
	public void cleanScreen() {
//		for (int i = 0 ; i < rows ; i++) {
//			Arrays.fill(screen[i], BLANK);
//		}
//		try {
//			Thread.sleep(100);	
//		} catch (Exception e) {
//		}
	}
	
	@Override
	public void setMessageAt(int row, Tence message) {
		if (message == null) {
			message = new Tence("", getCols(), SwingConstants.LEFT, BLANK);
		}
		if (row < 0 || row > getRows()) {
			return;
		}
		char[] cars = message.toString().toCharArray();
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
