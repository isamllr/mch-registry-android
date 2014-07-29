package com.mch.registry.ccs.server.com.mch.registry.ccs.server.sms;

/**
 * Created by Isa on 29.07.2014.
 */

import com.mch.registry.ccs.server.com.mch.registry.ccs.server.data.MySqlHandler;
import com.mch.registry.ccs.server.com.mch.registry.ccs.server.data.Pregnancy;

import org.marre.sms.SmsAddress;
import org.marre.sms.SmsException;
import org.marre.sms.SmsTextMessage;
import org.marre.sms.transport.SmsTransport;
import org.marre.sms.transport.SmsTransportManager;

import java.io.IOException;
import java.util.Properties;

public class SendSMS {

	void SendSMS(){
	}

	public static void sendActivationCode(){
// The username, password and apiid is sent to the clickatell transport
// in a Properties
		Properties props = new Properties();

		props.setProperty("smsj.clickatell.username", "swisstph");
		props.setProperty("smsj.clickatell.password", "eBFSBRKUaKgXaP");
		props.setProperty("smsj.clickatell.apiid", "3475961"); //testing HTTP

// Load the clickatell transport
		SmsTransport transport = null;
		try {
			transport = SmsTransportManager.getTransport("org.marre.sms.transport.clickatell.ClickatellTransport", props);
		} catch (SmsException e) {
// TODO Auto-generated catch block
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

		String recipient = "+41792374811";
		String sender = "41767658011";

		// Create the sms message
		MySqlHandler mysql = new MySqlHandler();
		Pregnancy pregnancy = new Pregnancy();
		//TODO
		pregnancy = mysql.getPregnancyInfo(recipient);
		SmsTextMessage textMessage = new SmsTextMessage("ABC35D5");//new SmsTextMessage(pregnancy.getActivationCode());

		recipient = recipient.replaceAll("[+]", "");

		try {
			transport.send(textMessage, new SmsAddress(recipient), new SmsAddress(sender));
			//System.out.println(" transport.send(textMessage, new SmsAddress('918089360844'), new SmsAddress('919847833022'));");
		} catch (SmsException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Disconnect from clickatell
		try {
			transport.disconnect();

			System.out.println(" transport.disconnect();");
		} catch (SmsException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
