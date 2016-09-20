package com.infovergne.rasp.lcd;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

public class DialogController implements Observer{

	private MessageController msgWelcome = null;
	private MessageController msgIP = null;
	private MessageController msgTime = null;
	
	private MessageController currentMsg = null;
	private LcdController ctrlLcd;
	
	private static InetAddress getFirstNonLoopbackAddress(boolean preferIpv4, boolean preferIPv6) throws SocketException {
	    Enumeration en = NetworkInterface.getNetworkInterfaces();
	    while (en.hasMoreElements()) {
	        NetworkInterface i = (NetworkInterface) en.nextElement();
	        for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements();) {
	            InetAddress addr = (InetAddress) en2.nextElement();
	            if (!addr.isLoopbackAddress()) {
	                if (addr instanceof Inet4Address) {
	                    if (preferIPv6) {
	                        continue;
	                    }
	                    return addr;
	                }
	                if (addr instanceof Inet6Address) {
	                    if (preferIpv4) {
	                        continue;
	                    }
	                    return addr;
	                }
	            }
	        }
	    }
	    return null;
	}
	
	public DialogController(LcdController ctrlLcd) {
		this.ctrlLcd  = ctrlLcd;
	}

	private MessageController createMessageTime() {
		if (msgTime != null) { msgTime.deleteObserver(this); }
		SimpleDateFormat sdfD = new SimpleDateFormat("E dd/MM/yyyy");
		SimpleDateFormat sdfH = new SimpleDateFormat("HH:mm");
		this.msgTime = new MessageController(ctrlLcd, 10, TimeUnit.SECONDS,
				"",
				"# " + sdfD.format(new Date()) + " ",
				"# " + sdfH.format(new Date()) + " ",
				"");
		this.msgTime.addObserver(this);
		return msgTime;
	}

	private MessageController createMessageWelcome() {
		if (msgWelcome != null) { msgWelcome.deleteObserver(this); }
		this.msgWelcome = new MessageController(ctrlLcd, 4, TimeUnit.SECONDS, 
				"> Welcome", "-", " Oliver's Raspberry", "-");
		this.msgWelcome.addObserver(this);
		return msgWelcome;
	}

	private MessageController createMessageIp() {
		if (msgIP != null) { msgIP.deleteObserver(this); }
		String ip = "", host = "";
		try {
			InetAddress adr = DialogController.getFirstNonLoopbackAddress(true, false);
			ip = adr.getHostAddress();
			host = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.msgIP = new MessageController(ctrlLcd, 4, TimeUnit.SECONDS, 
				" IP : " + ip,
				" Host : " + host,
				"-",
				"  (c)2016 Infovergne");
		this.msgIP.addObserver(this);
		return msgIP;
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
			currentMsg = createMessageWelcome();
		}
		currentMsg.start();
	}

}
