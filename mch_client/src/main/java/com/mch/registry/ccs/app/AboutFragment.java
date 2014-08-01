package com.mch.registry.ccs.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutFragment extends Fragment {

	public AboutFragment(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

		/*
		* R.string.common_about_text,
				R.string.app_name,
				R.string.gcm_demo_copyright,
				R.string.repo_link
		*/
 
        View rootView = inflater.inflate(R.layout.fragment_visits, container, false);
         
        return rootView;
    }
}
