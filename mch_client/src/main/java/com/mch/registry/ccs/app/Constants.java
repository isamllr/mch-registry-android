/*
 * Copyright (C) 2014 Wolfram Rittmeyer
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
package com.mch.registry.ccs.app;

public interface Constants {

    String PROJECT_ID = "210193935586";

    String KEY_STATE = "keyState";
    String KEY_REG_ID = "keyRegId";
    String KEY_MSG_ID = "keyMsgId";
    String KEY_ACCOUNT = "keyAccount";
    String KEY_MESSAGE_TXT = "keyMessageTxt";
    String KEY_EVENT_TYPE = "keyEventbusType";
    
    String ACTION = "action";

    // TODO: very simply notification handling
    int NOTIFICATION_NR = 10;

    long GCM_DEFAULT_TTL = 30 * 24 * 60 * 60 * 1000; // 30 days
    
    String PACKAGE = "com.mch.registry.ccs.app";
    // actions for server interaction
    String ACTION_REGISTER = PACKAGE + ".REGISTER";
    String ACTION_UNREGISTER = PACKAGE + ".UNREGISTER";
    String ACTION_ECHO = PACKAGE + ".ECHO";
    
    // action for notification intent
    String NOTIFICATION_ACTION = PACKAGE + ".NOTIFICATION";
    
    String DEFAULT_USER = ""; //should be phonenumber

    enum EventbusMessageType {
       REGISTRATION_FAILED, REGISTRATION_SUCCEEDED, UNREGISTRATION_SUCCEEDED, UNREGISTRATION_FAILED;
    }
    
    enum State {
		REGISTERED, UNREGISTERED;
	}

}
