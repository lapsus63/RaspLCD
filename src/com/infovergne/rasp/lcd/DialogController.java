package com.infovergne.rasp.lcd;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.swing.SwingConstants;

import com.google.gson.JsonElement;
import com.infovergne.api.ipapi.IpAPI;
import com.infovergne.api.openwm.OpenWeatherMap;
import com.infovergne.api.twitter.TwitterAPI;
import com.infovergne.rasp.lcd.message.AMessageController;
import com.infovergne.rasp.lcd.message.MarqueeLineController;
import com.infovergne.rasp.lcd.message.ScreenMessageController;
import com.infovergne.rasp.lcd.message.StaticMessageController;
import com.infovergne.rasp.lcd.message.Tence;
import com.infovergne.rasp.lcd.screen.AScreen;
import com.infovergne.utils.json.JsonUtils;
import com.infovergne.utils.net.InetUtils;
import com.infovergne.utils.text.TextUtils;

/**
 * Configures the set of messages to display on the screen.
 * 
 * @author Olivier
 */
public class DialogController implements Observer {

	private final List<Supplier<AMessageController>> messages = new ArrayList<Supplier<AMessageController>>();
	private int currentIndex = -1;
	
	private AScreen ctrlLcd;
	
	private JsonElement ipData = null;
	private JsonElement meteoData = null;
	private JsonElement tweetData = null;
	
	public DialogController(AScreen ctrlLcd) {
		this.ctrlLcd  = ctrlLcd;
	}

	private JsonElement getIpData() {
		if (ipData == null) {
			ipData = IpAPI.lookupTown();
		}
		return ipData;
	}
	
	private JsonElement getTweetData() {
		if (tweetData == null) {
			tweetData = TwitterAPI.findTweets("lapsus63");
		}
		return ipData;
	}
	
	private JsonElement getMeteoData() {
		if (meteoData == null) {
			String city = "Paris";
			if (getIpData() != null) {
				city = JsonUtils.getAtPath(ipData, "city").getAsString();
			}
			meteoData = OpenWeatherMap.getForecast(city, "fr");
		}
		return meteoData;
	}

	public void start() {
		messages.add(() -> createMessageWelcome());
		messages.add(() -> createMessageLine());
		messages.add(() -> createMessageGeoloc());
		messages.add(() -> createMessageMeteo());
		messages.add(() -> createMessageTime());
		messages.add(() -> createMessageTweeter(0));
		messages.add(() -> createMessageTweeter(1));
		messages.add(() -> createMessageTweeter(2));
		messages.add(() -> createMessageTweeter(3));
		incrementAndStart();
	}
	
	private void incrementAndStart() {
		currentIndex++;
		if (currentIndex >= messages.size()) {
			currentIndex = 0;
		}
		messages.get(currentIndex).get().start();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof AMessageController) {
			((AMessageController)o).deleteObserver(this);
		}
		try {
			incrementAndStart();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private AMessageController createMessageGeoloc() {
		String city = "", lan = "", wan = "", host = "";
		if (getIpData() != null) {
			city = JsonUtils.getAtPath(ipData, "city").getAsString();
			wan = JsonUtils.getAtPath(ipData, "query").getAsString();
		}
		try {
			InetAddress adr = InetUtils.getFirstNonLoopbackAddress(true, false);
			lan = adr.getHostAddress();
			host = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		AMessageController msg = new StaticMessageController(ctrlLcd, 3, TimeUnit.SECONDS);
		msg.add(new Tence(city, ctrlLcd.getCols(), SwingConstants.CENTER, '_')); // City
		msg.add(new Tence(host, ctrlLcd.getCols(), SwingConstants.CENTER, ' '));
		msg.add(new Tence("WAN:" + wan, ctrlLcd.getCols(), SwingConstants.LEFT, ' ')); // Public IP
		msg.add(new Tence("LAN:" + lan, ctrlLcd.getCols(), SwingConstants.LEFT, ' ')); // Local IP
		msg.addObserver(this);
		return msg;
	}

	
	private AMessageController createMessageMeteo() {
		String temp = "", hum = "", wind = "", desc = "", date="";
		if (getMeteoData() != null) {
			temp = JsonUtils.getAtPath(meteoData, "list[0]/main/temp").getAsString();
			hum = JsonUtils.getAtPath(meteoData, "list[0]/main/humidity").getAsString();
			wind = JsonUtils.getAtPath(meteoData, "list[0]/wind/speed").getAsString();
			desc = JsonUtils.getAtPath(meteoData, "list[0]/weather/description").getAsString();
			date = JsonUtils.getAtPath(meteoData, "list[0]/dt_txt").getAsString();
		}
		AMessageController msg = new StaticMessageController(ctrlLcd, 10, TimeUnit.SECONDS);
		msg.add(new Tence("T:" + temp + "°   H:" + hum + "%", ctrlLcd.getCols(), SwingConstants.CENTER, ' ')); // Temp, Humidity
		msg.add(new Tence("V:" + wind + "km/h" , ctrlLcd.getCols(), SwingConstants.CENTER, ' ')); // Wind
		msg.add(new Tence(desc, ctrlLcd.getCols(), SwingConstants.LEFT, ' ')); // Description
		msg.add(new Tence(date , ctrlLcd.getCols(), SwingConstants.CENTER, '~')); // Date
		msg.addObserver(this);
		return msg;
	}

	private AMessageController createMessageTime() {
		SimpleDateFormat sdfD = new SimpleDateFormat("E dd/MM/yyyy");
		SimpleDateFormat sdfH = new SimpleDateFormat("HH:mm");
		AMessageController msg = new StaticMessageController(ctrlLcd, 3, TimeUnit.SECONDS);
		msg.add(new Tence("", ctrlLcd.getCols(), SwingConstants.CENTER, '#'))
			.add(new Tence(" " + sdfD.format(new Date()) + " ", ctrlLcd.getCols(), SwingConstants.CENTER, '#'))
			.add(new Tence(" " + sdfH.format(new Date()) + " ", ctrlLcd.getCols(), SwingConstants.CENTER, '#'))
			.add(new Tence("", ctrlLcd.getCols(), SwingConstants.CENTER, '#'));
		msg.addObserver(this);
		return msg;
	}

	private AMessageController createMessageWelcome() {
		AMessageController msg = new StaticMessageController(ctrlLcd, 3, TimeUnit.SECONDS);
		msg.add(new Tence(" Welcome ",ctrlLcd.getCols(),SwingConstants.CENTER, '#'))
			.add(new Tence("Oliver's Raspberry",ctrlLcd.getCols(),SwingConstants.RIGHT, ' '))
			.add(new Tence("",ctrlLcd.getCols(),SwingConstants.CENTER, '-'))
			.add(new Tence("(c)2016 Infovergne", ctrlLcd.getCols(), SwingConstants.RIGHT, ' '));
		msg.addObserver(this);
		return msg;
	}

	private AMessageController createMessageLine() {
		AMessageController msg = new MarqueeLineController(ctrlLcd, 200L, 12L, TimeUnit.SECONDS);
		msg.add(new Tence(" Welcome ",ctrlLcd.getCols(),SwingConstants.CENTER, ' '))
			.add(new Tence("Oliver's Raspberry",ctrlLcd.getCols(),SwingConstants.RIGHT, ' '))
			.add(new Tence("",ctrlLcd.getCols(),SwingConstants.CENTER, '-'))
			.add(new Tence("(c)2016 Infovergne", ctrlLcd.getCols(), SwingConstants.RIGHT, ' '));
		msg.addObserver(this);
		return msg;
	}
	
	private AMessageController createMessageTweeter(int index) {
		String txt = "", auth = "";
		if (getTweetData() != null) {
			try {
				txt = JsonUtils.getAtPath(tweetData, "tweets[" + index + "]/text").getAsString();
				txt = TextUtils.smsify(txt);
				auth = "@" + JsonUtils.getAtPath(tweetData, "tweets[" + index + "]/username").getAsString();
			} catch (IndexOutOfBoundsException e) {}
		}
		AMessageController msg = new ScreenMessageController(ctrlLcd, 8, TimeUnit.SECONDS);
		msg .add(new Tence(txt,140,SwingConstants.LEFT, ' '))
			.add(new Tence("",140,SwingConstants.LEFT, '-'))
			.add(new Tence("",140,SwingConstants.LEFT, '.'))
			.add(new Tence(auth,140,SwingConstants.RIGHT, ' '));
		msg.addObserver(this);
		return msg;
	}

}
