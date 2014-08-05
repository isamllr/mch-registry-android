package com.mch.registry.ccs.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mch.registry.ccs.data.adapter.RecommendationArrayAdapter;
import com.mch.registry.ccs.data.Recommendation;
import com.mch.registry.ccs.data.RecommendationDataHandler;

import java.util.ArrayList;

public class RecommendationsFragment extends Fragment {
	
	public RecommendationsFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recommendations, container, false);

		RecommendationDataHandler rdh = new RecommendationDataHandler(getActivity(),"fn received", null, 1);
		ArrayList<Recommendation> recommendations = rdh.getAllRecommendations();

		ListView recommendationLV = (ListView)rootView.findViewById(R.id.listView);
		recommendationLV.setAdapter(new RecommendationArrayAdapter(getActivity(), recommendations));

		return rootView;
    }

}
