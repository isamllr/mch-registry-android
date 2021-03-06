package com.mch.registry.ccs.app;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.mch.registry.ccs.data.adapter.RecommendationArrayAdapter;
import com.mch.registry.ccs.data.entities.Recommendation;
import com.mch.registry.ccs.data.handler.RecommendationDataHandler;

import java.util.ArrayList;

/**
 * Created by Isa
 */
public class RecommendationsFragment extends Fragment {

	RecommendationArrayAdapter recAdapter;
	ArrayList<Recommendation> recommendations;
	ListView recommendationLV;
	Recommendation recommendation;
	View rootView;

	public RecommendationsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_recommendations, container, false);

		RecommendationDataHandler rdh = new RecommendationDataHandler(getActivity(), "loading recommendations list", null, 1);
		recommendations = rdh.getAllRecommendations();
		recommendationLV = (ListView) rootView.findViewById(R.id.listView);
		recAdapter = new RecommendationArrayAdapter(getActivity(), recommendations);
		recommendationLV.setAdapter(recAdapter);

		EditText inputSearch = (EditText)rootView.findViewById(R.id.searchRecommendation);
		inputSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// When user changed the Text
				recAdapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});

		recommendationLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				RecommendationDataHandler rdh = new RecommendationDataHandler(getActivity(), "edit, delete or share recommendation", null, 1);
				recommendations = rdh.getAllRecommendations();
				recAdapter = new RecommendationArrayAdapter(getActivity(), recommendations);
				recommendation = recAdapter.getItem(position);
				int recId = recommendation.get_id();
				shareRecommendationText(getString(R.string.pregnancy_week)
						+ " " + Integer.toString(rdh.findRecommendation(recId).get_pregnancyWeek())
						+ ": " + rdh.findRecommendation(recId).get_recommendationText());
			}
		});

		return rootView;
	}

	public void shareRecommendationText(String recommendationText) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.pregnancy_recommendation_share_title));
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, recommendationText);
		startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.abc_shareactionprovider_share_with)));
	}
}