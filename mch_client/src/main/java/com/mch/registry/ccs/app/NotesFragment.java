package com.mch.registry.ccs.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mch.registry.ccs.data.Note;
import com.mch.registry.ccs.data.NoteDataHandler;
import com.mch.registry.ccs.data.Pregnancy;
import com.mch.registry.ccs.data.PregnancyDataHandler;
import com.mch.registry.ccs.data.adapter.NoteArrayAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotesFragment extends Fragment{

	EditText inputSearch;
	NoteArrayAdapter noteAdapter;

	public NotesFragment(){
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        final View rootView = inflater.inflate(R.layout.fragment_notes, container, false);

		NoteDataHandler ndh = new NoteDataHandler(getActivity(),"loading notes list", null, 1);
		ArrayList<Note> notes = ndh.getAllNotes();

		final ListView noteLV = (ListView)rootView.findViewById(R.id.listView);
		noteAdapter = new NoteArrayAdapter(getActivity(), notes);
		noteLV.setAdapter(noteAdapter);

		inputSearch = (EditText)rootView.findViewById(R.id.searchNote);

		final Button button = (Button) rootView.findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TextView tv = (EditText)rootView.findViewById(R.id.editText);
				NoteDataHandler ndh = new NoteDataHandler(getActivity(),"add note", null, 1);
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss");
				Calendar cal = Calendar.getInstance();
				ndh.addNote(tv.getText().toString(), calculateNoteDay(cal.getTime()), cal.getTime());
			}
		});

		noteLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(R.string.pick_action)
						.setItems(R.array.note_action_array, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								NoteDataHandler ndh = new NoteDataHandler(getActivity(),"edit, delete or share note", null, 1);
								switch (which) {
									//TODO: real id of note
									case 0:
										getEditedText(position+1,ndh.findNote(position+1).get_noteText());
										noteAdapter.notifyDataSetChanged();
										break;
									case 1:
										ndh.deleteNote(position+1);
										noteAdapter.notifyDataSetChanged();
										break;
									case 2:
										shareNoteText(ndh.findNote(position+1).get_noteText());
										break;
								}
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// When user changed the Text
				noteAdapter.getFilter().filter(cs);
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

	private void getEditedText(final int position, String oldText) {
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

		alert.setTitle("Title");
		alert.setMessage("Message");

		final EditText input = new EditText(getActivity());
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				NoteDataHandler ndh = new NoteDataHandler(getActivity(),"fn received", null, 1);
				ndh.updateNote(position, value);
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();
	}

	public void shareNoteText(String noteText){
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.note_share_title));
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, noteText);
		startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.abc_shareactionprovider_share_with_application)));
	}

	private int calculateNoteDay(Date today) {

		Date truncatedDate1 = null;
		Date truncatedDate2 = null;

		long timeDifference = 0;
		long daysInBetween = 0;

		try {
			PregnancyDataHandler pdh = new PregnancyDataHandler(getActivity(), "Note", null, 1);
			Pregnancy pregnancy = pdh.getPregnancy();

			DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");

			String truncatedDateString1 = formatter.format(today);
			String truncatedDateString2 = pregnancy.get_expectedDelivery().toString();
			truncatedDate1 = formatter.parse(truncatedDateString1);
			truncatedDate2 = formatter.parse(truncatedDateString2);

			timeDifference = truncatedDate2.getTime()- truncatedDate1.getTime();
			daysInBetween = timeDifference / (24*60*60*1000);

		} catch (ParseException e) {
			Log.i("Pregnancy Guide", "error parsing dates" + e.getMessage());
		}


		return ((int) daysInBetween);
	}


}