package com.mch.registry.ccs.data.adapter;

import android.content.Context;

import com.mch.registry.ccs.app.R;
import com.mch.registry.ccs.data.Note;

import java.util.ArrayList;

	/**
	 * Created by Isa on 02.08.2014.
	 */
	public class NoteArrayAdapter extends TwoLineArrayAdapter<Note> {
		Context _context;

		public NoteArrayAdapter(Context context, ArrayList<Note> notes) {
			super(context, notes);
			_context = context;
		}

		@Override
		public String lineOneText(Note n) {
			return _context.getString(R.string.pregnancy_week) +  ": " + Integer.toString(((int) Math.floor(n.get_noteDay() / 7)));
		}

		@Override
		public String lineTwoText(Note n) {
			return n.get_noteText();
		}
	}
