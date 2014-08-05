package com.mch.registry.ccs.data.adapter;

import android.content.Context;

import com.mch.registry.ccs.app.R;
import com.mch.registry.ccs.data.Recommendation;

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
		return _context.getString(R.string.pregnancy_week) +  ": " + Integer.toString(((int) Math.floor(r.get_recommendationDay() / 7)));
	}

	@Override
	public String lineTwoText(Recommendation r) {
		return r.get_recommendationText();
	}
}
