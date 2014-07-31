package com.mch.registry.ccs.sms;

/**
 * Created by Isa on 29.07.2014.
 */

import com.mch.registry.ccs.server.MessageProcessor;
import com.mch.registry.ccs.server.com.mch.registry.ccs.server.data.MySqlHandler;
import com.mch.registry.ccs.server.com.mch.registry.ccs.server.data.Pregnancy;

import org.marre.sms.SmsAddress;
import org.marre.sms.SmsException;
import org.marre.sms.SmsTextMessage;
import org.marre.sms.transport.SmsTransport;
import org.marre.sms.transport.SmsTransportManager;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendSMS {

	public static final Logger logger = Logger.getLogger(MessageProcessor.class.getName());

	void SendSMS() {
	}

	public static void sendActivationCode(String phoneNumber) {


		// The username, password and apiid is sent to the clickatell transport
		// in a Properties
		Properties props = new Properties();

		props.setProperty("smsj.clickatell.username", "swisstph");
		props.setProperty("smsj.clickatell.password", "eBFSBRKUaKgXaP");
		props.setProperty("smsj.clickatell.apiid", "3475961"); //testing HTTP
		String sender = "41767658011";

		// Load the clickatell transport
		SmsTransport transport = null;
		try {
			transport = SmsTransportManager.getTransport("org.marre.sms.transport.clickatell.ClickatellTransport", props);
		} catch (SmsException e) {
			e.printStackTrace();
		}

		// Connect to clickatell
		try {
			transport.connect();
		} catch (SmsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String recipient = phoneNumber.replaceAll("[+]", "");
		// Create the sms message
		MySqlHandler mysql = new MySqlHandler();
		Pregnancy pregnancy = new Pregnancy();
		pregnancy = mysql.getPregnancyInfoByMobilePhone(recipient);
		SmsTextMessage textMessage = new SmsTextMessage(pregnancy.getActivationCode());

		try {
			transport.send(textMessage, new SmsAddress( recipient), new SmsAddress(sender));
			logger.log(Level.INFO, "Code sent by SMS to " + recipient);

		} catch (SmsException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Disconnect from clickatell
		try {
			transport.disconnect();
			System.out.println(" transport.disconnect();");
		} catch (SmsException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
