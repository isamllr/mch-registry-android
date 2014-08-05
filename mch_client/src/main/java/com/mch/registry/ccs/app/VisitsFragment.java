package com.mch.registry.ccs.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mch.registry.ccs.data.adapter.VisitArrayAdapter;
import com.mch.registry.ccs.data.Visit;
import com.mch.registry.ccs.data.VisitDataHandler;

import java.util.ArrayList;

public class VisitsFragment extends Fragment {
	
	public VisitsFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_visits, container, false);

			VisitDataHandler rdh = new VisitDataHandler(getActivity(),"fn received", null, 1);
			ArrayList<Visit> visits = rdh.getAllVisits();

			if(visits.size()>0){
				ListView visitsLV = (ListView)rootView.findViewById(R.id.listView);
				visitsLV.setAdapter(new VisitArrayAdapter(getActivity(), visits));
			}

        return rootView;
    }
}
