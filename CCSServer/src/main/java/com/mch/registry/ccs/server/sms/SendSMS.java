package com.mch.registry.ccs.server.sms;

import com.mch.registry.ccs.server.Config;
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

/**
 * Created by Isa on 29.07.2014.
 */
public class SendSMS {

	public static final Logger logger = Logger.getLogger(SendSMS.class.getName());

	public SendSMS() {
	}

	public void sendActivationCode(String phoneNumber) {

		// The username, password and apiid is sent to the clickatell transport
		Properties props = new Properties();

		Config config = new Config();

		props.setProperty("smsj.clickatell.username", config.getClickatellUsername());
		props.setProperty("smsj.clickatell.password", config.getClickatellPassword());
		props.setProperty("smsj.clickatell.apiid", config.getClickatellAPIID()); //testing HTTP
		String sender = config.getSenderphone();


		// Load the clickatell transport
		SmsTransport transport = null;
		try {
			transport = SmsTransportManager.getTransport("org.marre.sms.transport.clickatell.ClickatellTransport", props);
		} catch (SmsException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		// Connect to clickatell
		try {
			transport.connect();
			logger.log(Level.INFO, "Connected.");
		} catch (SmsException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		// Create the sms message
		MySqlHandler mysql = new MySqlHandler();
		Pregnancy pregnancy = new Pregnancy();
		logger.log(Level.INFO, "Phone number is " + phoneNumber);
		pregnancy = mysql.getPregnancyInfoByMobilePhone(phoneNumber);
		String code = pregnancy.getActivationCode();
		SmsTextMessage textMessage = new SmsTextMessage(code);
		logger.log(Level.INFO, "Code is " + code);
		String recipient = phoneNumber.replaceAll("[+]", "");
		logger.log(Level.INFO, "Recipient is " + recipient);

		try {
			transport.send(textMessage, new SmsAddress(recipient), new SmsAddress(sender));
			logger.log(Level.INFO, "Activation Code sent by SMS to " + recipient);
		} catch (SmsException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		// Disconnect from clickatell
		try {
			transport.disconnect();
			logger.log(Level.INFO, "Disconnected");
		} catch (SmsException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
