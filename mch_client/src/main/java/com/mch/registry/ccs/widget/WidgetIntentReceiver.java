package com.mch.registry.ccs.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mch.registry.ccs.app.R;
import com.mch.registry.ccs.app.Utils;
import com.mch.registry.ccs.data.entities.Recommendation;
import com.mch.registry.ccs.data.handler.RecommendationDataHandler;

import java.util.ArrayList;

public class WidgetIntentReceiver extends BroadcastReceiver {
	public static int clickCount = 0;
	private String msg[] = null;

	@Override
	public void onReceive(Context _context, Intent intent) {
		if (intent.getAction().equals(WidgetUtils.WIDGET_UPDATE_ACTION)) {
			updateWidgetPictureAndButtonListener(_context);
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
		RecommendationDataHandler rdh = new RecommendationDataHandler(context, null, null, 1);
		ArrayList<Recommendation> recommendations = rdh.findRecommendationByPregnancyWeek(Utils.getPregnancyWeek(context));

		if (recommendations.size() > 0){
			if (clickCount >= recommendations.size()) {
				clickCount = 0;
			}
			return recommendations.get(clickCount).get_recommendationText();
		}else{
			return context.getString(R.string.no_recommendations);
		}
	}

	private String getTitle(Context context) {
		return context.getString(R.string.widget_title_pregnancy_week) + " " + Integer.toString(Utils.getPregnancyWeek(context));
	}

}