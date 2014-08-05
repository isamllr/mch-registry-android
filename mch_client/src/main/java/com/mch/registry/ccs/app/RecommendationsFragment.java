package com.mch.registry.ccs.app;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mch.registry.ccs.data.Recommendation;
import com.mch.registry.ccs.data.RecommendationDataHandler;
import com.mch.registry.ccs.data.VisitDataHandler;
import com.mch.registry.ccs.data.adapter.RecommendationArrayAdapter;

import java.util.ArrayList;

public class RecommendationsFragment extends Fragment {
	
	public RecommendationsFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recommendations, container, false);

		final RecommendationDataHandler rdh = new RecommendationDataHandler(getActivity(),"fn received", null, 1);
		ArrayList<Recommendation> recommendations = rdh.getAllRecommendations();

		final ListView recommendationLV = (ListView)rootView.findViewById(R.id.listView);
		recommendationLV.setAdapter(new RecommendationArrayAdapter(getActivity(), recommendations));

		recommendationLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				shareRecommendationText(getString(R.string.pregnancy_week) +  " "
						+ Integer.toString(((int) Math.floor(rdh.findRecommendation(position+1).get_recommendationDay() / 7)))
						+ ": " + rdh.findRecommendation(position+1).get_recommendationText());
			}
		});

		return rootView;
    }

	public void shareRecommendationText(String recommendationText){
		VisitDataHandler vdh = new VisitDataHandler(getActivity(),"fn received", null, 1);

		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hospital Visit: ");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, recommendationText);
		startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.abc_shareactionprovider_share_with_application)));
	}

}
