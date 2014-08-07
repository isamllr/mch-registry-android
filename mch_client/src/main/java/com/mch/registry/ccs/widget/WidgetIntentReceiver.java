package com.mch.registry.ccs.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.mch.registry.ccs.app.R;
import com.mch.registry.ccs.data.Pregnancy;
import com.mch.registry.ccs.data.PregnancyDataHandler;
import com.mch.registry.ccs.data.Recommendation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WidgetIntentReceiver extends BroadcastReceiver {
	public static int clickCount = 0;
	private String msg[] = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(WidgetUtils.WIDGET_UPDATE_ACTION)) {
			updateWidgetPictureAndButtonListener(context);
		}
	}

	private void updateWidgetPictureAndButtonListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),	R.layout.widget_layout);

		// updating view
		remoteViews.setTextViewText(R.id.title, getTitle(context));
		remoteViews.setTextViewText(R.id.desc, getDesc(context));

		// re-registering for click listener
		remoteViews.setOnClickPendingIntent(R.id.sync_button, WidgetProvider.buildButtonPendingIntent(context));

		WidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}

	private String getDesc(Context context) {
		// some static jokes from xml
		msg = context.getResources().getStringArray(R.array.news_headlines);
		if (clickCount >= msg.length) {
			clickCount = 0;
		}
		return msg[clickCount];
	}

	private String getTitle(Context context) {
		return "Pregnancy Week: " + calculatePregnancyWeek(context);
	}

	private String calculatePregnancyWeek(Context context) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss");
		Calendar cal = Calendar.getInstance();

		Date truncatedDate1 = null;
		Date truncatedDate2 = null;

		long timeDifference = 0;
		long daysInBetween = 0;

		try {
			PregnancyDataHandler pdh = new PregnancyDataHandler(context, "Rec", null, 1);
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

		return Integer.toString((((int) daysInBetween) / 7));
	}

	private ArrayList<Recommendation> getRecommendationsOfCurrentWeek(){
		Recommendation recommendation = new Recommendation();
		ArrayList<Recommendation> recommendations = new ArrayList<Recommendation>();
		//TODO
		return recommendations;
	}
}