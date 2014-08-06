package com.mch.registry.ccs.app;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mch.registry.ccs.data.Pregnancy;
import com.mch.registry.ccs.data.PregnancyDataHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
		TextView textWeek = (TextView)rootView.findViewById(R.id.textWeekValue);

		textPatientName.setText(preg.get_patientName());
		textPatientPhone.setText(preg.get_mobileNumber());
		textExpectedDelivery.setText(preg.get_expectedDelivery());
		textFacilityName.setText(preg.get_facilityName());
		textFacilityPhone.setText(preg.get_facilityPhoneNumber());
		textWeek.setText(Integer.toString(((int) Math.floor(calculatePregnancyDay() / 7))));

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

	private int calculatePregnancyDay() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss");
		Calendar cal = Calendar.getInstance();

		Date truncatedDate1 = null;
		Date truncatedDate2 = null;

		long timeDifference = 0;
		long daysInBetween = 0;

		try {
			PregnancyDataHandler pdh = new PregnancyDataHandler(getActivity(), "Rec", null, 1);
			Pregnancy pregnancy = pdh.getPregnancy();

			DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");

			String truncatedDateString1 = formatter.format(cal.getTime());
			String truncatedDateString2 = pregnancy.get_expectedDelivery().toString();
			truncatedDate1 = formatter.parse(truncatedDateString1);
			truncatedDate2 = formatter.parse(truncatedDateString2);

			timeDifference = truncatedDate2.getTime()- truncatedDate1.getTime();
			daysInBetween = timeDifference / (24*60*60*1000);

		} catch (ParseException e) {
			Log.i("Pregnancy Guide", "error " + e.getMessage());
		}

		return ((int) daysInBetween);
	}
}
