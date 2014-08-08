package com.mch.registry.ccs.widget;

import android.content.Context;

import com.mch.registry.ccs.app.Utils;
import com.mch.registry.ccs.data.entities.Recommendation;
import com.mch.registry.ccs.data.handler.RecommendationDataHandler;

import java.util.ArrayList;

/**
 * Created by Isa on 07.08.2014.
 */
public class WidgetUtils {
	final static String WIDGET_UPDATE_ACTION ="com.mch.intent.action.UPDATE_WIDGET";

	public static ArrayList<Recommendation> getRecommendationsOfCurrentWeek(Context context){

		RecommendationDataHandler rdh = new RecommendationDataHandler(context, null, null, 1);
		ArrayList<Recommendation> recommendations = rdh.findRecommendationByPregnancyWeek(Utils.getPregnancyWeek(context));

		return recommendations;
	}
}
