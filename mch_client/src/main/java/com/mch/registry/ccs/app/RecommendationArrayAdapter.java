package com.mch.registry.ccs.app;

import android.content.Context;

import com.mch.registry.ccs.data.Recommendation;

import java.util.ArrayList;

/**
 * Created by Isa on 02.08.2014.
 */
public class RecommendationArrayAdapter extends TwoLineArrayAdapter<Recommendation> {
	public RecommendationArrayAdapter(Context context, ArrayList<Recommendation> recommendations) {
		super(context, recommendations);
	}

	@Override
	public String lineOneText(Recommendation r) {
		return Integer.toString(r.get_recommendationDay());
	}

	@Override
	public String lineTwoText(Recommendation r) {
		return r.get_recommendationText();
	}
}
