package com.infovergne.rasp.lcd.screen;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.infovergne.rasp.lcd.message.Tence;

/**
 * Swing Screen simulating LCD display.
 * @author Olivier
 */
public class SwingScreen extends AScreen {
	
	private static final char BLANK = '+';
	
	private JFrame frame = null;
	private final JLabel[][] digits; /* rows x cols */

	public SwingScreen(int rows, int cols) {
		super(rows, cols);
		this.digits = new JLabel[rows][cols];
	}
	
	public void initialize() {
		frame = new JFrame();
		Font f = new Font("Courier New", Font.BOLD, 26);
		frame.setTitle("Swing LCD");
		JPanel panel = new JPanel(new GridLayout(getRows(), getCols()));
		frame.setContentPane(panel);
		for (int i = 0 ; i < getRows() ; i++) {
			for (int j = 0 ; j < getCols(); j++) {
				JLabel jlabel = new JLabel(String.valueOf(BLANK));
				jlabel.setFont(f);
				digits[i][j] = jlabel;
				panel.add(jlabel);
			}
		}
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		super.initialize();
	}
	
	@Override
	public void cleanScreen() {	}
	
	@Override
	public void setMessageAt(int row, Tence message) {
		if (message == null) {
			message = new Tence("", getCols(), SwingConstants.LEFT, BLANK, false);
		}
		if (row < 0 || row > getRows()) {
			return;
		}
		char[] chars = message.toString().toCharArray();
		for (int j = 0 ; j < chars.length && j < getCols() ; j++) {
			digits[row][j].setText(String.valueOf(chars[j]));
		}
	}
	
	@Override
	public void initView() {
	}
	
	@Override
	public void commitView() {
		//
	}
	
}
