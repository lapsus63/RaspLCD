package com.infovergne.rasp.lcd;

import com.pi4j.wiringpi.Lcd;

public class Main {

	
	public static final void main(String... args) {
		System.out.println("Hello World ! Lcd Lib = " + Lcd.class);
		
		LcdController ctrl = new LcdController(4, 20);
		ctrl.initialize();
		if (ctrl.isInitialized()) {
			System.out.println("LCD correctly initialized :-)");
			new DialogController(ctrl).start();
		} else {
			System.err.println("Failed to initialize LCD system properly");
		}
	}
}
