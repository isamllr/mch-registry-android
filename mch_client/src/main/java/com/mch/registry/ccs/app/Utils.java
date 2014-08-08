package com.mch.registry.ccs.app;

import android.content.Context;
import android.util.Log;

import com.mch.registry.ccs.data.handler.PregnancyDataHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Isa on 08.08.2014.
 */
public class Utils {

	public static int getPregnancyDay(Context context) {

		Date expectedDeliveryDate;
		long diffDays = 0;

		try {
			PregnancyDataHandler pdh = new PregnancyDataHandler(context, "Widget", null, 1);
			String deliveryString = pdh.getPregnancy().get_expectedDelivery();
			DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
			expectedDeliveryDate = formatter.parse(deliveryString);

			Calendar calE = Calendar.getInstance();
			calE.setTime(expectedDeliveryDate);
			calE.add(Calendar.DAY_OF_MONTH, -280);

			Calendar calN = Calendar.getInstance();

			long milliseconds1 = calN.getTimeInMillis();
			long milliseconds2 = calE.getTimeInMillis();
			long diff = milliseconds1 - milliseconds2;
			diffDays = diff / (24 * 60 * 60 * 1000);

		} catch (ParseException e) {
			Log.i("Pregnancy Guide", "error " + e.getMessage());
		}

		return (int)diffDays;
	}

	public static int getPregnancyWeek(Context context) {
		return (int) Math.floor(getPregnancyDay(context) / 7);
	}
}
