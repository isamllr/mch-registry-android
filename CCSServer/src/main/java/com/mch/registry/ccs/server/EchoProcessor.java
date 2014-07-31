/*
 * Copyright 2014 Wolfram Rittmeyer.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mch.registry.ccs.server;

import com.mch.registry.ccs.server.com.mch.registry.ccs.server.data.MySqlHandler;
import com.mch.registry.ccs.server.com.mch.registry.ccs.server.data.Pregnancy;
import com.mch.registry.ccs.sms.SendSMS;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles an echo request.
 */
public class EchoProcessor implements PayloadProcessor{

	private CcsClient client = CcsClient.getInstance();
	private PseudoDao dao = PseudoDao.getInstance();
	public static final Logger logger = Logger.getLogger(MessageProcessor.class.getName());

	@Override
	public void handleMessage(CcsMessage msg) {

		String txtMsg = msg.getPayload().get("message");
		logger.log(Level.INFO, "Message received. Text: " + txtMsg);

		if(txtMsg.contains("_Phone: ")){
			String phoneNumber = txtMsg.replaceAll("_Phone: ","");
			MySqlHandler mysql = new MySqlHandler();
			mysql.setVerified(msg.getFrom(), false);
			if(mysql.updateAllPregnancyInfos(phoneNumber, msg.getFrom())){
				logger.log(Level.INFO, "Trying to send activation code by SMS.");
				SendSMS.sendActivationCode(phoneNumber);
			}else{
				sendPregnancyForMobileNumberNotFound(msg.getFrom());
				logger.log(Level.INFO, "Pregnancy not found");
			}

		}

		if(txtMsg.contains("_Verify: ")){
			txtMsg = txtMsg.replaceAll("_Verify: ","");
			MySqlHandler mysql = new MySqlHandler();
			Pregnancy pregnancy = new Pregnancy();
			pregnancy = mysql.getPregnancyInfoByGcmRegId(msg.getFrom());
			//Compare codes
			if (pregnancy.getActivationCode().compareTo(txtMsg)==0){
				mysql.setVerified(msg.getFrom(), true);
				sendVerificationMessage(msg.getFrom(), true);
				logger.log(Level.INFO, "Verification ok");
				sendPregnancyInfos(msg.getFrom());
			}else{
				mysql.setVerified(msg.getFrom(), true);
				sendVerificationMessage(msg.getFrom(), true);
				logger.log(Level.INFO, "Verification failed");
			}
		}
	}

	private void sendPregnancyForMobileNumberNotFound(String gcmRegId) {
		Map<String, String> payload = new HashMap<String, String>();
		String toRegId = gcmRegId;

		String message = "_PregnancyNotFound";
		String messageId = client.getRandomMessageId();

		payload = new HashMap<String, String>();
		payload.put("message", message);

		try {
			// Send the downstream message to a device.
			client.send(client.createJsonMessage(gcmRegId, messageId, payload, null, 10000L, true));
			logger.log(Level.INFO, "PregnancyNotFound message sent. IRegID: " + toRegId + ", Text: " + message);
		} catch (Exception e) {
			logger.log(Level.WARNING, "PregnancyNotFound message could not be sent! RegID: " + toRegId + ", Text: " + message);
		}
	}

	private void sendPregnancyInfos(String gcmRegId) {

		//TODO
		//Check if verified
		logger.log(Level.INFO, "Sending pregnancy infos");
		Map<String, String> payload = new HashMap<String, String>();
		String toRegId = gcmRegId;
		String messageId = client.getRandomMessageId();
		String message = "_PregnacyInfos";
		payload = new HashMap<String, String>();
		payload.put("message", message);

		try {
			// Send the downstream message to a device.
			client.send(client.createJsonMessage(gcmRegId, messageId, payload, null, 10000L, true));
			logger.log(Level.INFO, "PregnancyInfos sent. IRegID: " + toRegId + ", Text: " + message);
		} catch (Exception e) {
			logger.log(Level.WARNING, "PregnancyInfos not sent message could not be sent! RegID: " + toRegId + ", Text: " + message);
		}
	}

	private void sendVerificationMessage(String gcmRegId, boolean verificationAccepted) {
		Map<String, String> payload = new HashMap<String, String>();
		String toRegId = gcmRegId;

		String message = ( verificationAccepted ? "_Verified" : "_NotVerified" );
		String messageId = client.getRandomMessageId();

		payload = new HashMap<String, String>();
		payload.put("message", message);

		try {
			// Send the downstream message to a device.
			client.send(client.createJsonMessage(gcmRegId, messageId, payload, null, 10000L, true));
			logger.log(Level.INFO, "Verification message sent. IRegID: " + toRegId + ", Text: " + message);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Verification message couldnot be sent! RegID: " + toRegId + ", Text: " + message);
		}
	}

}