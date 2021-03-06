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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles a user registration.
 */
public class RegisterProcessor implements PayloadProcessor{

	public static final Logger logger = Logger.getLogger(RegisterProcessor.class.getName());
	//TODO
    @Override
    public void handleMessage(CcsMessage msg) {
        //String accountName = msg.getPayload().get("account");
        Dao.getInstance().addRegistration(msg.getFrom());
	    logger.log(Level.INFO, "registerprocessor is registering");
    }

}
