package com.mch.registry.ccs.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.mch.registry.ccs.data.adapter.VisitArrayAdapter;
import com.mch.registry.ccs.data.entities.Visit;
import com.mch.registry.ccs.data.handler.VisitDataHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Isa
 */
public class VisitsFragment extends Fragment{

	View rootView;
	VisitArrayAdapter visitAdapter;
	EditText inputSearch;

	public VisitsFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.fragment_visits, container, false);

			final VisitDataHandler vdh = new VisitDataHandler(getActivity(),"visit list", null, 1);
			ArrayList<Visit> visits = vdh.getAllVisits();

			final ListView visitsLV = (ListView)rootView.findViewById(R.id.listView);
			visitAdapter = new VisitArrayAdapter(getActivity(), visits);
			visitsLV.setAdapter(visitAdapter);

			visitsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Visit vis = visitAdapter.getItem(position);
					showDialog(vdh.findVisitById(vis.getID()).get_visitDate(), vdh.findVisitById(vis.getID()).get_visitText());
			}
		});

		inputSearch = (EditText)rootView.findViewById(R.id.searchVisit);
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

				// When user changed the Text
				visitAdapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});

        return rootView;
    }

	private void showDialog(final String visitDate, final String visitText) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.pick_action)
				.setItems(R.array.action_array, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						switch (which) {
							case 0:
								shareVisitText(visitText);
								break;
							case 1:
								saveToCalendar(visitDate, visitText);
								break;
						}
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void saveToCalendar(String visitDate, String visitText) {

		Intent calIntent = new Intent(Intent.ACTION_INSERT);
		calIntent.setData(CalendarContract.Events.CONTENT_URI);
		calIntent.putExtra(CalendarContract.Events.TITLE, "Hospital Visit");
		calIntent.putExtra(CalendarContract.Events.DESCRIPTION, visitText);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss");
		Date start = null;
		Date end = null;
		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		try {
			start = dateFormat.parse(visitDate + " 08:00");
			end = dateFormat.parse(visitDate + " 19:00");
		} catch (ParseException e) {
			Log.e("No date to parse available in received message", e.getMessage());
			start = startTime.getTime();
			end = endTime.getTime();
		}
		startTime.setTime(start);
		endTime.setTime(end);

		calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,startTime.getTimeInMillis());
		calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime.getTimeInMillis());
		startActivity(calIntent);

	}

	public void shareVisitText(String visitText){
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.hospital_visit));
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, visitText);
		startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.abc_shareactionprovider_share_with_application)));
	}
}
