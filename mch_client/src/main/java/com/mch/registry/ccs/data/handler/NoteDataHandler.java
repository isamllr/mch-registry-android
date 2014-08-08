package com.mch.registry.ccs.data.handler;

/**
 * Created by Isa on 06.08.2014.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mch.registry.ccs.data.entities.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Isa on 18.07.2014.
 */
public class NoteDataHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "notes.db";
	private static final String TABLE_NOTES = "notes";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NOTETEXT = "_noteText";
	public static final String COLUMN_NOTEDAY = "_noteDay";
	public static final String COLUMN_CREATEDDATE = "_createdDate";

	public NoteDataHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_NOTES_TABLE =
				"CREATE TABLE " + TABLE_NOTES + "("
						+ COLUMN_ID + " INTEGER PRIMARY KEY,"
						+ COLUMN_NOTETEXT + " TEXT,"
						+ COLUMN_NOTEDAY + " INTEGER,"
						+ COLUMN_CREATEDDATE + " DATETIME"
						+ ")";
		db.execSQL(CREATE_NOTES_TABLE);

		String INSERT_NOTE_TABLE =
				"INSERT INTO " + TABLE_NOTES + "("
						+ COLUMN_ID + ", "
						+ COLUMN_NOTETEXT + ","
						+ COLUMN_NOTEDAY + ","
						+ COLUMN_CREATEDDATE
						+ " )VALUES(null, '" + "This is the first diary note. These notes are personal, but the diary will help you to remember when talking to your doctor.'" + ", 1, date('now'));";
		db.execSQL(INSERT_NOTE_TABLE);

		Log.i("Pregnancy Guide", "NoteDB created & 1 note inserted");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
		onCreate(db);
	}

	public void addNote(String noteText, int noteDay) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss");

		ContentValues values = new ContentValues();
		values.put(COLUMN_NOTETEXT,noteText);
		values.put(COLUMN_NOTEDAY, noteDay);
		values.put(COLUMN_CREATEDDATE, dateFormat.format(Calendar.getInstance().getTime()));

		SQLiteDatabase db = this.getWritableDatabase();

		db.insert(TABLE_NOTES, null, values);
		db.close();
	}

	public ArrayList<Note> getAllNotes(){

		String query = "Select * FROM " + TABLE_NOTES;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		ArrayList<Note> resultList = new ArrayList<Note>();
		Note noteRecord = new Note();
		try {
			if (cursor.moveToFirst()) {
				do {
						noteRecord = new Note();
						noteRecord.set_id(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
						noteRecord.set_noteText(cursor.getString(cursor.getColumnIndex(COLUMN_NOTETEXT)));
						noteRecord.set_noteDay(cursor.getInt(cursor.getColumnIndex(COLUMN_NOTEDAY)));
						noteRecord.set_createdDate(cursor.getString(cursor.getColumnIndex(COLUMN_CREATEDDATE)));
						resultList.add(noteRecord);

				}while (cursor.moveToNext());
			}

			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("SQLLite getNote Error", "Error " + e.toString());
		}

		return resultList;
	}

	public boolean deleteNote(int noteID) {
		boolean status = false;
		try{
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("Delete FROM " + TABLE_NOTES + " WHERE " + COLUMN_ID + " =  \"" + Integer.toString(noteID) + "\";");
			db.close();
			status = true;
		}		catch (Exception e){
			Log.e("SQLLite deleteNote Error", "Error " + e.toString());
		}
		return status;
	}

	public Note findNote(int noteID) {
		String query = "Select * FROM " + TABLE_NOTES + " WHERE " + COLUMN_ID + " =  \"" + Integer.toString(noteID) + "\"";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		Note note = new Note();

		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			note.set_id(Integer.parseInt(cursor.getString(0)));
			note.set_noteText(cursor.getString(1));
			note.set_noteDay(Integer.parseInt(cursor.getString(2)));
			note.set_createdDate(cursor.getString(3));
			cursor.close();
		} else {
			note = null;
		}
		db.close();

		return note;
	}

	public boolean updateNote(int position, String value) {
		boolean status = false;
		try{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_NOTETEXT,value);
			db.update(TABLE_NOTES, cv, COLUMN_ID + "=" + Integer.toString(position), null);
			db.close();
			status = true;
		}
		catch (Exception e){
			Log.e("SQLLite updateNote Error", "Error " + e.toString());
		}
		return status;
	}
}

