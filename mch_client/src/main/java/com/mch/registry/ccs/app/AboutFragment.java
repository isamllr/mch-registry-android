package com.mch.registry.ccs.app;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {

	public AboutFragment(){}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
		TextView textAbout = (TextView)rootView.findViewById(R.id.about_text);
		textAbout.setText(Html.fromHtml(getString(R.string.common_about_text)));
		textAbout.setMovementMethod(LinkMovementMethod.getInstance());
         
        return rootView;
    }
}
