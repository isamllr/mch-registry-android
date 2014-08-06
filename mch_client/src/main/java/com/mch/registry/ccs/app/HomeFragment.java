package com.mch.registry.ccs.app;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mch.registry.ccs.data.Pregnancy;
import com.mch.registry.ccs.data.PregnancyDataHandler;

public class HomeFragment extends Fragment {

	TextView textFacilityPhone;

	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

		PregnancyDataHandler pdh = new PregnancyDataHandler(getActivity(),"fn received", null, 1);
		Pregnancy preg = pdh.getPregnancy();

		TextView textPatientName = (TextView)rootView.findViewById(R.id.textPatientNameValue);
		TextView textPatientPhone = (TextView)rootView.findViewById(R.id.textPatientPhoneValue);
		TextView textExpectedDelivery = (TextView)rootView.findViewById(R.id.textExpectedDeliveryValue);
		TextView textFacilityName = (TextView)rootView.findViewById(R.id.textFacilityNameValue);
		textFacilityPhone = (TextView)rootView.findViewById(R.id.textFacilityPhoneValue);

		textPatientName.setText(preg.get_patientName());
		textPatientPhone.setText(preg.get_mobileNumber());
		textExpectedDelivery.setText(preg.get_expectedDelivery());
		textFacilityName.setText(preg.get_facilityName());
		textFacilityPhone.setText(preg.get_facilityPhoneNumber());

		final Button button = (Button) rootView.findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				callFacility();
			}
		});
         
        return rootView;
    }

	private void callFacility(){
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + textFacilityPhone.getText().toString().trim()));
		startActivity(callIntent);
	}
}
