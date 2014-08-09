package com.mch.registry.ccs.data.adapter;

import android.content.Context;

import com.mch.registry.ccs.app.R;
import com.mch.registry.ccs.data.entities.Recommendation;

import java.util.ArrayList;

/**
 * Created by Isa on 02.08.2014.
 */
public class RecommendationArrayAdapter extends TwoLineArrayAdapter<Recommendation> {
	Context _context;

	public RecommendationArrayAdapter(Context context, ArrayList<Recommendation> recommendations) {
		super(context, recommendations);
		_context = context;
	}

	@Override
	public String lineOneText(Recommendation r) {
		return _context.getString(R.string.pregnancy_week) +  ": " + Integer.toString(r.get_pregnancyWeek()) + ", #" + r.get_id();
	}

	@Override
	public String lineTwoText(Recommendation r) {
		return r.get_recommendationText();
	}
}
