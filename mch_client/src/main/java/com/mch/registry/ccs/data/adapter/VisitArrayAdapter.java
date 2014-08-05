package com.mch.registry.ccs.data.adapter;

import android.content.Context;

import com.mch.registry.ccs.data.Visit;

import java.util.ArrayList;

/**
 * Created by Isa on 05.08.2014.
 */
public class VisitArrayAdapter extends TwoLineArrayAdapter<Visit> {
	public VisitArrayAdapter(Context context, ArrayList<Visit> recommendations) {
		super(context, recommendations);
	}

	@Override
	public String lineOneText(Visit v) {
		return v.get_visitDate();
	}

	@Override
	public String lineTwoText(Visit v) {
		return v.get_visitText();
	}
}
