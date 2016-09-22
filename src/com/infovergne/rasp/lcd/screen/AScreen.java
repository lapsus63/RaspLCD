package com.infovergne.rasp.lcd.screen;


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
			throw new IllegalStateException("LCD system not properly initialized ; run initialize at first.");
		}
	}
	
	public void sendMessage(boolean clear, String... messages) {
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


	public abstract void setMessageAt(int row, String message);

	public abstract void initView();

	public abstract void commitView();

	public abstract void cleanScreen();
	
}
