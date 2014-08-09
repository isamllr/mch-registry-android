package com.mch.registry.ccs.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mch.registry.ccs.data.adapter.NoteArrayAdapter;
import com.mch.registry.ccs.data.entities.Note;
import com.mch.registry.ccs.data.handler.NoteDataHandler;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Isa
 */
public class NotesFragment extends Fragment{

	NoteArrayAdapter noteAdapter;
	ListView noteLV;
	ArrayList<Note> notes;
	Note note;
	EditText inputSearch;

	public NotesFragment(){
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_notes, container, false);

		NoteDataHandler ndh = new NoteDataHandler(getActivity(),"loading notes list", null, 1);
		notes = ndh.getAllNotes();

		ListView noteLV = (ListView)rootView.findViewById(R.id.listView);
		noteAdapter = new NoteArrayAdapter(getActivity(), notes);
		noteLV.setAdapter(noteAdapter);

		final Button button = (Button) rootView.findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				//Add new note
				TextView tv = (EditText)rootView.findViewById(R.id.editText);
				NoteDataHandler ndh = new NoteDataHandler(getActivity(),"add note", null, 1);
				Calendar cal = Calendar.getInstance();
				ndh.addNote(tv.getText().toString(), Utils.getPregnancyDay(getActivity()));

				//Close keyboard and clear field after add
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);
				tv.setText("");

				//Reload list view
				ListView noteLV = (ListView)rootView.findViewById(R.id.listView);
				notes = ndh.getAllNotes();
				noteAdapter = new NoteArrayAdapter(getActivity(), notes);
				noteLV.setAdapter(noteAdapter);
				noteAdapter.notifyDataSetChanged();
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

								ArrayList<Note> notes = ndh.getAllNotes();
								noteAdapter = new NoteArrayAdapter(getActivity(), notes);
								note = noteAdapter.getItem(position);
								ListView noteLV = (ListView)rootView.findViewById(R.id.listView);

								int id = note.get_id();
								switch (which) {
									case 0:
										getEditedText(id , ndh.findNote(id).get_noteText(), rootView);
										break;
									case 1:
										ndh.deleteNote(id);

										//update list
										notes = ndh.getAllNotes();
										noteAdapter = new NoteArrayAdapter(getActivity(), notes);
										noteLV.setAdapter(noteAdapter);
										noteAdapter.notifyDataSetChanged();
										break;
									case 2:
										shareNoteText(ndh.findNote(id).get_noteText());
										break;
								}
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		inputSearch = (EditText)rootView.findViewById(R.id.searchNote);
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

	private void getEditedText(final int id, String oldText, final View rootView) {
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

		alert.setTitle(getString(R.string.edit_note));

		final EditText input = new EditText(getActivity());
		input.setText(oldText);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				NoteDataHandler ndh = new NoteDataHandler(getActivity(),"fn received", null, 1);
				ndh.updateNote(id, value);

				//Close Keyboard
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

				//update list
				ArrayList<Note> notes = ndh.getAllNotes();
				noteAdapter = new NoteArrayAdapter(getActivity(), notes);
				ListView noteLV = (ListView)rootView.findViewById(R.id.listView);
				noteLV.setAdapter(noteAdapter);
				noteAdapter.notifyDataSetChanged();
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
		startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.abc_shareactionprovider_share_with)));
	}

}