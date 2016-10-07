package com.infovergne.rasp.lcd.screen;

import com.infovergne.rasp.lcd.message.Tence;

/**
 * Defines an output stream.
 * Could be System.out, or the LCD screen.
 * <p>A Screen is delimitated by a row count and a column count</p>
 * @author Olivier
 */
public abstract class AScreen {
	
	protected final int rows;
	protected final int cols;
	protected boolean initialized = false;
	
	public AScreen(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
	}
	
	public int getRows() {
		return  rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	public void initialize() {
		initialized = true;
	}
	
	protected void checkInit() throws IllegalStateException {
		if (!initialized) {
			throw new IllegalStateException("System not properly initialized ; you must call initialize first.");
		}
	}
	
	public void sendMessage(boolean clear, Tence... messages) {
		if (clear) {
			cleanScreen();
		}
		initView();
		if (messages != null) {
			for (int  i = 0 ; i < messages.length && i < rows ; i++) {
				setMessageAt(i, messages[i]);
			}
		}
		commitView();
	}


	public abstract void setMessageAt(int row, Tence message);

	public abstract void initView();

	public abstract void commitView();

	public abstract void cleanScreen();
	
}
