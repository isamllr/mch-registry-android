package com.mch.registry.ccs.data.adapter;

import android.content.Context;

import com.mch.registry.ccs.app.R;
import com.mch.registry.ccs.data.entities.Visit;

import java.util.ArrayList;

/**
 * Created by Isa on 05.08.2014.
 */
public class VisitArrayAdapter extends TwoLineArrayAdapter<Visit> {
	Context _context;
	public VisitArrayAdapter(Context context, ArrayList<Visit> recommendations) {
		super(context, recommendations);
		_context = context;
	}

	@Override
	public String lineOneText(Visit v) {
		return _context.getString(R.string.visit_date) +  ": " + v.get_visitDate() + ", #" + v.getID();
	}

	@Override
	public String lineTwoText(Visit v) {
		return v.get_visitText();
	}
}
