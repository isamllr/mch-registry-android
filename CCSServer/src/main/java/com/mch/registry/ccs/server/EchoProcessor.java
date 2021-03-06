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
import com.mch.registry.ccs.server.sms.SendSMS;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/** New: Mueller
 * Handles an echo request.
 */
public class EchoProcessor implements PayloadProcessor{

	private CcsClient client = CcsClient.getInstance();
	private Dao dao = Dao.getInstance();
	public static final Logger logger = Logger.getLogger(EchoProcessor.class.getName());

	@Override
	public void handleMessage(CcsMessage msg) {

		String txtMsg = msg.getPayload().get("message");
		logger.log(Level.INFO, "Message received. Text: " + txtMsg);

		if(txtMsg.contains("_Phone: ")){
			String phoneNumber = txtMsg.replaceAll("_Phone: ","");
			MySqlHandler mysql = new MySqlHandler();
			mysql.setVerified(msg.getFrom(), false);
			if(mysql.updateAllPregnancyInfos(phoneNumber, msg.getFrom())){
				logger.log(Level.INFO, "Trying to send activation code by SMS. ");
				SendSMS sms = new SendSMS();
				sms.sendActivationCode(phoneNumber);
			}else{
				sendPregnancyForMobileNumberNotFound(msg.getFrom());
				logger.log(Level.INFO, "Pregnancy not found");
			}
		}else if(txtMsg.contains("_Verify: ")){
			String code = txtMsg.replaceAll("_Verify: ","");
			MySqlHandler mysql = new MySqlHandler();
			Pregnancy pregnancy = mysql.getPregnancyInfoByGcmRegId(msg.getFrom());
			logger.log(Level.INFO, "Comparing: " + code + " and " + pregnancy.getActivationCode());
			//Compare codes
			if (pregnancy.getActivationCode().compareTo(code)==0){
				mysql.setVerified(msg.getFrom(), true);
				sendVerificationMessage(msg.getFrom(), true);
				logger.log(Level.INFO, "Verification ok");
				preparePregnancyInfos(msg.getFrom());
			}else{
				mysql.setVerified(msg.getFrom(), true);
				sendVerificationMessage(msg.getFrom(), true);
				logger.log(Level.INFO, "Verification failed");
			}
		}else if(txtMsg.contains("_MobileAppOn")){
			MySqlHandler mysql = new MySqlHandler();
			mysql.setVerified(msg.getFrom(), false);
			logger.log(Level.INFO, "App turned on for user");
			}
		else if(txtMsg.contains("_MobileAppOff")){
			MySqlHandler mysql = new MySqlHandler();
			mysql.setVerified(msg.getFrom(), false);
			logger.log(Level.INFO, "App turned off for user");
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

	private void preparePregnancyInfos(String gcmRegId) {
		MySqlHandler mysql = new MySqlHandler();
		Pregnancy pregnancy = mysql.getPregnancyInfoByGcmRegId(gcmRegId);

		sendPregnancyInfos(gcmRegId, "_PregnancyInfosFacilityName: " + pregnancy.getFacilityName());
		sendPregnancyInfos(gcmRegId, "_PregnancyInfosFacilityPhone: " + pregnancy.getFacilityPhoneNumber());
		sendPregnancyInfos(gcmRegId, "_PregnancyInfosExpectedDelivery: " + pregnancy.getExpectedDelivery());
		sendPregnancyInfos(gcmRegId, "_PregnancyInfosPatientName: " + pregnancy.getPatientSurName() + " " + pregnancy.getPatientLastName());
	}

	private void sendPregnancyInfos(String gcmRegId, String message){

		Map<String, String> payload = new HashMap<String, String>();
		String messageId = client.getRandomMessageId();
		payload = new HashMap<String, String>();
		payload.put("message", message);

		try {
			// Send the downstream message to a device.
			client.send(client.createJsonMessage(gcmRegId, messageId, payload, null, 10000L, true));
			logger.log(Level.INFO, "PregnancyInfos sent. Text: " + message);
		} catch (Exception e) {
			logger.log(Level.WARNING, "PregnancyInfos not sent message could not be sent! RegID: " + gcmRegId + ", Text: " + message + " " + e.getMessage());
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
			logger.log(Level.WARNING, "Verification message could not be sent! RegID: " + toRegId + ", Text: " + message);
		}
	}

}