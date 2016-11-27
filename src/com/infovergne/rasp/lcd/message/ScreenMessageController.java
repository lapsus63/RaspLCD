package com.infovergne.rasp.lcd.message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingConstants;

import com.infovergne.rasp.lcd.screen.AScreen;
import com.infovergne.rasp.lcd.screen.OutScreen;

/**
 * Displays a short or a long message to the screen (delimited by rows / cols).
 * If the text is too long to fit the width of the screen, it is wrapped to the next line, 
 * except if the next line contains data to show.
 * <p>If a word is larger than screen width, it is truncated.</p>
 * <p>Example to show a tweet over 3 lines : </p><ul><li>data.add(message);</li><li>data.add("");</li><li>data.add("");</li><li>data.add(author):</li></ul>
 * @author Olivier
 */
public class ScreenMessageController extends AMessageController {

	public ScreenMessageController(AScreen screen, long delay, TimeUnit unit) {
		super(screen, delay, unit);
	}
	
	public static final void main(String... args) {
		AScreen scr = new OutScreen(4, 20);
		ScreenMessageController ctrl = new ScreenMessageController(scr, 1L, TimeUnit.SECONDS);
//		ctrl.add(new Tence("A     B     C", 999, SwingConstants.RIGHT, '_'));
		ctrl.add(new Tence("aVeryVeryLongMessageMoreThanTwentyCharacters", 999, SwingConstants.CENTER, '-'));
		// formattedMessage = ctrl.getFormattedData("");
//		ctrl.add(new Tence("a row ending by a quitelongword", 999, SwingConstants.LEFT, '.'));
//		ctrl.add(new Tence("A five lines very long message, sounds quite great, but overflow needstobemanaged", 999, SwingConstants.LEFT, ' '));
		ctrl.add(new Tence("", 999, SwingConstants.CENTER, ' '));
		ctrl.add(new Tence("", 999, SwingConstants.CENTER, ' '));
		ctrl.add(new Tence("Furax", scr.getCols(), SwingConstants.RIGHT, ' '));	
		ctrl.sendMessage();
	}
	
	private List<Tence> getFormattedData() {
		List<Tence> retour = new ArrayList<Tence>();
		int cols = screen.getCols();
		int rows = screen.getRows();
		
		Tence currentMessage = null;
		int currentIndex = 0;
		
		while (currentIndex < data.size()) {
			currentMessage = data.get(currentIndex);
			String[] words = currentMessage.getMessageASCII().split(" ");
			String currentRow = "";
			int i = 0;
			for (i = 0 ; i < words.length ; i++) {
				String s = words[i];
				if (i < words.length - 1) {
					s += " ";
				}
				if (currentRow.length() > 0 && (currentRow + s).length() > cols) {
					retour.add(new Tence(currentRow, cols,currentMessage.getSwingAlign(), currentMessage.getFiller()));
					if (currentIndex + 1 < data.size() && data.get(currentIndex + 1).getMessage().length() >0) {
						currentRow= "";
						break;
					}
					currentRow = s;
					if (currentIndex + 1 >= rows) {
						break;
					}
					currentIndex++;
				} else {
					currentRow += s;
				}
			}
			currentIndex++;
			if ((currentMessage.getMessage().isEmpty() || currentRow.length() > 0) && retour.size() < rows) {
				retour.add(new Tence(currentRow, cols, currentMessage.getSwingAlign(), currentMessage.getFiller()));
			} else if (i < words.length) {
				/* Overflow */
				Tence lastTence = retour.get(retour.size() - 1);
				String lastRow = lastTence.toString();
				lastRow = lastRow.substring(0, Math.min(lastRow.length(), cols - 4)) + " ...";
				retour.set(retour.size()- 1, new Tence(lastRow, lastTence.getMaxLen(), lastTence.getSwingAlign(), lastTence.getFiller()));
			}
		}

		return retour;
	}

	@Override
	protected void sendMessage() {
		List<Tence> newData = getFormattedData();
		screen.sendMessage(true, newData.toArray(new Tence[0]));
	}

}
