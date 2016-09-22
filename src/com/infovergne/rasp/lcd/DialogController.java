package com.infovergne.rasp.lcd;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingConstants;

import com.infovergne.rasp.lcd.message.AMessageController;
import com.infovergne.rasp.lcd.message.MarqueeController;
import com.infovergne.rasp.lcd.message.StaticMessageController;
import com.infovergne.rasp.lcd.screen.AScreen;
import com.infovergne.rasp.lcd.util.LcdUtils;
import com.infovergne.rasp.net.InetUtils;

public class DialogController implements Observer {

	private AMessageController msgWelcome = null;
	private AMessageController msgIP = null;
	private AMessageController msgTime = null;
	private AMessageController msgMarquee = null;
	
	private AMessageController currentMsg = null;
	private AScreen ctrlLcd;
	
	public DialogController(AScreen ctrlLcd) {
		this.ctrlLcd  = ctrlLcd;
	}

	public void start() {
		this.currentMsg = createMessageWelcome();
		this.currentMsg.start();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (currentMsg == msgWelcome) {
			currentMsg = createMessageIp();
		} else if (currentMsg == msgIP) {
			currentMsg = createMessageTime();
		} else if (currentMsg == msgTime) {
			currentMsg = createMessageMarquee();
		} else if (currentMsg == msgMarquee) {
			currentMsg = createMessageWelcome();
		}
		currentMsg.start();
	}

	private AMessageController createMessageTime() {
		if (msgTime != null) { msgTime.deleteObserver(this); }
		SimpleDateFormat sdfD = new SimpleDateFormat("E dd/MM/yyyy");
		SimpleDateFormat sdfH = new SimpleDateFormat("HH:mm");
		this.msgTime = new StaticMessageController(ctrlLcd, 3, TimeUnit.SECONDS);
		this.msgTime.add(LcdUtils.buildRow("", ctrlLcd.getCols(), SwingConstants.CENTER, '#'))
			.add(LcdUtils.buildRow(" " + sdfD.format(new Date()) + " ", ctrlLcd.getCols(), SwingConstants.CENTER, '#'))
			.add(LcdUtils.buildRow(" " + sdfH.format(new Date()) + " ", ctrlLcd.getCols(), SwingConstants.CENTER, '#'))
			.add(LcdUtils.buildRow("", ctrlLcd.getCols(), SwingConstants.CENTER, '#'));
		this.msgTime.addObserver(this);
		return msgTime;
	}

	private AMessageController createMessageWelcome() {
		if (msgWelcome != null) { msgWelcome.deleteObserver(this); }
		this.msgWelcome = new StaticMessageController(ctrlLcd, 3, TimeUnit.SECONDS);
		this.msgWelcome.add(LcdUtils.buildRow(" Welcome ",ctrlLcd.getCols(),SwingConstants.CENTER, '#'))
			.add(LcdUtils.buildRow("",ctrlLcd.getCols(),SwingConstants.CENTER, '-'))
			.add(LcdUtils.buildRow("Oliver's Raspberry",ctrlLcd.getCols(),SwingConstants.RIGHT, ' '))
			.add(LcdUtils.buildRow("",ctrlLcd.getCols(),SwingConstants.CENTER, '-'));
		this.msgWelcome.addObserver(this);
		return msgWelcome;
	}
	
	private AMessageController createMessageMarquee() {
		if (msgMarquee != null) { msgMarquee.deleteObserver(this); }
		this.msgMarquee = new MarqueeController(ctrlLcd, 40, 500L, 10, TimeUnit.SECONDS);
		this.msgMarquee.add(LcdUtils.buildRow("Welcome TEST Marquee Message",40,SwingConstants.RIGHT, '#'))
			.add(LcdUtils.buildRow("",40,SwingConstants.CENTER, '-'))
			.add(LcdUtils.buildRow("Marqueeing",40,SwingConstants.LEFT, '.'))
			.add(LcdUtils.buildRow("",40,SwingConstants.CENTER, '-'));
		this.msgMarquee.addObserver(this);
		return msgMarquee;
	}

	private AMessageController createMessageIp() {
		if (msgIP != null) { msgIP.deleteObserver(this); }
		String ip = "", host = "";
		try {
			InetAddress adr = InetUtils.getFirstNonLoopbackAddress(true, false);
			ip = adr.getHostAddress();
			host = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.msgIP = new StaticMessageController(ctrlLcd, 3, TimeUnit.SECONDS);
		this.msgIP.add(LcdUtils.buildRow("IP : " + ip, ctrlLcd.getCols(), SwingConstants.LEFT, ' '))
				.add(LcdUtils.buildRow("Host : " + host, ctrlLcd.getCols(), SwingConstants.LEFT, ' '))
				.add(LcdUtils.buildRow("-", ctrlLcd.getCols(), SwingConstants.CENTER, '-'))
				.add(LcdUtils.buildRow("(c)2016 Infovergne", ctrlLcd.getCols(), SwingConstants.RIGHT, ' '));
		this.msgIP.addObserver(this);
		return msgIP;
	}
}
