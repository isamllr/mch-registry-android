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
import com.mch.registry.ccs.server.com.mch.registry.ccs.server.sms.SendSMS;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles an echo request.
 */
public class MessageProcessor implements PayloadProcessor{

	private CcsClient client = CcsClient.getInstance();
	private PseudoDao dao = PseudoDao.getInstance();
	public static final Logger logger = Logger.getLogger(CcsClient.class.getName());

    @Override
    public void handleMessage(CcsMessage msg) {
	    //Receive verification , check verification, send message to phone
        //client.send(jsonRequest);
	    //Send Pregnancy Info

	    String txtMsg = msg.getPayload().get("message");
	    logger.log(Level.INFO, "Message received. Text: " + txtMsg);
	    if(txtMsg.matches("^Phone: ")){
		    String phoneNumber = txtMsg.replaceAll("^Phone: ","");
		    MySqlHandler mysql = new MySqlHandler();
		    mysql.setVerified(msg.getFrom(), false);
		    SendSMS sms = new SendSMS();
		    sms.sendActivationCode(phoneNumber);
		    logger.log(Level.INFO, "Activationcode sent.");
	    }

	    if(txtMsg.matches("^Verify: ")){
		    txtMsg = txtMsg.replaceAll("^Verify: ","");
		    MySqlHandler mysql = new MySqlHandler();
		    //Compare codes
		    if (mysql.getVerificationCode(msg.getFrom()).compareTo(txtMsg)==0){
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

	private void sendPregnancyInfos(String gcmRegId) {
		logger.log(Level.INFO, "Sending pregnancy infos");
		//TODO
	}

	private void sendVerificationMessage(String gcmRegId, boolean verificationAccepted) {
		Map<String, String> payload = new HashMap<String, String>();
		String toRegId = gcmRegId;

		String message = ( verificationAccepted ? "[Verified]" : "[NotVerified]" );
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
