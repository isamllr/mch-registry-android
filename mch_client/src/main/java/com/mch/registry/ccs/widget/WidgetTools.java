package com.mch.registry.ccs.widget;

import android.content.Context;
import android.util.Log;

import com.mch.registry.ccs.data.Pregnancy;
import com.mch.registry.ccs.data.PregnancyDataHandler;
import com.mch.registry.ccs.data.Recommendation;
import com.mch.registry.ccs.data.RecommendationDataHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Isa on 07.08.2014.
 */
public class WidgetTools {

	public WidgetTools(){}

	public static int getPregnancyWeek(Context context) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss");
		Calendar cal = Calendar.getInstance();

		Date truncatedDate1 = null;
		Date truncatedDate2 = null;

		long timeDifference = 0;
		long daysInBetween = 0;

		try {
			PregnancyDataHandler pdh = new PregnancyDataHandler(context, "Widget", null, 1);
			Pregnancy pregnancy = pdh.getPregnancy();

			DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");

			String truncatedDateString1 = formatter.format(cal.getTime());
			String truncatedDateString2 = pregnancy.get_expectedDelivery().toString();
			truncatedDate1 = formatter.parse(truncatedDateString1);
			truncatedDate2 = formatter.parse(truncatedDateString2);

			timeDifference = truncatedDate2.getTime()- truncatedDate1.getTime();
			daysInBetween = timeDifference / (24*60*60*1000);

		} catch (ParseException e) {
			Log.i("Pregnancy Guide", "error " + e.getMessage());
		}

		return (int) Math.floor(daysInBetween / 7);
	}

	public static ArrayList<Recommendation> getRecommendationsOfCurrentWeek(Context context){
		PregnancyDataHandler pdh = new PregnancyDataHandler(context, null, null, 1);
		pdh.getPregnancy().get_expectedDelivery();

		RecommendationDataHandler rdh = new RecommendationDataHandler(context, null, null, 1);
		ArrayList<Recommendation> recommendations = rdh.findRecommendationByPregnancyWeek(WidgetTools.getPregnancyWeek(context));

		return recommendations;
	}
}
