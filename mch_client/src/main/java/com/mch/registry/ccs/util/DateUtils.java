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
package com.mch.registry.ccs.apputil;

import java.util.Date;

public class DateUtils {

	public static String formatDateTime(String formatStr, Date date) {
		if (date == null) {
			return null;
		}
    	return android.text.format.DateFormat.format(formatStr, date).toString();
	}
	
	public static String formatDateTimeForIO(Date date) {
    	return formatDateTime("yyyy-MM-dd_hh-mm-ss", date);
	}
	
}