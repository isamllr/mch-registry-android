package com.mch.registry.ccs.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mch.registry.ccs.app.R;

/**
 * Created by Isa on 07.08.2014.
 */
public class WidgetProvider extends AppWidgetProvider {
	public static PendingIntent buildButtonPendingIntent(Context context) {
		++WidgetIntentReceiver.clickCount;

		// initiate widget update request
		Intent intent = new Intent();
		intent.setAction(WidgetUtils.WIDGET_UPDATE_ACTION);
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	private static CharSequence getDesc() {
		return "Sync to see some of our funniest joke collections";
	}

	private static CharSequence getTitle() {
		return "Funny Jokes";
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName widget = new ComponentName(context, WidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(widget, remoteViews);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

		// initializing widget layout
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),	R.layout.widget_layout);

		// register for button event
		remoteViews.setOnClickPendingIntent(R.id.sync_button, buildButtonPendingIntent(context));

		// updating view with initial data
		remoteViews.setTextViewText(R.id.title, getTitle());
		remoteViews.setTextViewText(R.id.desc, getDesc());

		// request for widget update
		pushWidgetUpdate(context, remoteViews);
	}
}