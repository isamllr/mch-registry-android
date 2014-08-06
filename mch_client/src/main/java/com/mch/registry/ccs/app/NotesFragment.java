package com.mch.registry.ccs.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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

import com.mch.registry.ccs.data.Note;
import com.mch.registry.ccs.data.NoteDataHandler;
import com.mch.registry.ccs.data.VisitDataHandler;
import com.mch.registry.ccs.data.adapter.NoteArrayAdapter;

import java.util.ArrayList;

public class NotesFragment extends Fragment{

	EditText inputSearch;
	NoteArrayAdapter recAdapter;

	public NotesFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);

		final NoteDataHandler ndh = new NoteDataHandler(getActivity(),"fn received", null, 1);
		ArrayList<Note> notes = ndh.getAllNotes();

		final ListView noteLV = (ListView)rootView.findViewById(R.id.listView);
		recAdapter = new NoteArrayAdapter(getActivity(), notes);
		noteLV.setAdapter(recAdapter);

		inputSearch = (EditText)rootView.findViewById(R.id.searchNote);

		noteLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(R.string.pick_action)
						.setItems(R.array.note_action_array, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

								switch (which) {
									case 0:
										getEditedText(position+1,ndh.findNote(position+1).get_noteText());
										break;
									case 1:
										ndh.deleteNote(position+1);
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
				recAdapter.getFilter().filter(cs);
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

// Set an EditText view to get user input
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
		VisitDataHandler vdh = new VisitDataHandler(getActivity(),"fn received", null, 1);

		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.note_share_title));
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, noteText);
		startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.abc_shareactionprovider_share_with_application)));
	}


}