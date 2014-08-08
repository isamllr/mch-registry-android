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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Dao {
    
    private final static Dao instance = new Dao();
    private final static Random sRandom = new Random();
    private final Set<Integer> mMessageIds = new HashSet<Integer>();
    private final Map<String, String> mNotificationKeyMap = new HashMap<String, String>();
    
    private Dao() {
    }
    
    public static Dao getInstance() {
        return instance;
    }

	///Mueller: Edited, unneeded functions removed
    public void addRegistration(String regId) {
	    MySqlHandler mysql = new MySqlHandler();

	    if (!mysql.findRegID(regId)) {
		    mysql.saveNewRegID(regId);
        }
    }

    public String getUniqueMessageId() {
        int nextRandom = sRandom.nextInt();
        while (mMessageIds.contains(nextRandom)) {
            nextRandom = sRandom.nextInt();
        }
        return Integer.toString(nextRandom);
    }
}
