package com.mch.registry.ccs.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Isa on 18.07.2014.
 */
public class VisitDataHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "visits.db";
    private static final String TABLE_VISITS = "visits";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VISITTEXT = "_visitText";
    public static final String COLUMN_RECEIVEDDATE = "_visitFacility";
    public static final String COLUMN_VISITDATE = "_visitDay";

    public VisitDataHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VISITS_TABLE =
                "CREATE TABLE " + TABLE_VISITS + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY,"
                        + COLUMN_VISITTEXT + " TEXT,"
                        + COLUMN_RECEIVEDDATE+ " DATETIME,"
                        + COLUMN_VISITDATE + " DATETIME"
                        + ")";
        db.execSQL(CREATE_VISITS_TABLE);

	    String INSERT_VISITS_TABLE =
			    "INSERT INTO " + TABLE_VISITS + "("
					    + COLUMN_ID + ", "
					    + COLUMN_VISITTEXT + ","
					    + COLUMN_RECEIVEDDATE+ ","
					    + COLUMN_VISITDATE
					    + ")VALUES(null, 'Future visit reminders will be shown here.', date('now'), date('now'));";
	    db.execSQL(INSERT_VISITS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISITS);
        onCreate(db);
    }

    public void addVisit(String visitText) {

	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss");
	    Calendar cal = Calendar.getInstance();

        ContentValues values = new ContentValues();
        values.put(COLUMN_VISITTEXT, visitText);
        values.put(COLUMN_RECEIVEDDATE,  dateFormat.format(cal.getTime()).toString());
        values.put(COLUMN_VISITDATE, extractDate(visitText));

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_VISITS, null, values);
        db.close();
    }

	private static String extractDate(String visitText) {
		int count = 0;
		String match = "";
		Matcher m = Pattern.compile("(0[1-9]|1[012])[- ..](0[1-9]|[12][0-9]|3[01])[- ..](19|20)\\d\\d").matcher(visitText);
		m.find();
		match = m.group();
		return match;
	}

    public String findVisitText(String visitDate) {
		String query = "Select * FROM " + TABLE_VISITS + " WHERE " + COLUMN_VISITDATE + " =  \"" + visitDate + "\"";

		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(query, null);

		Visit visitRecord = new Visit();

		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			visitRecord.setID(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
			visitRecord.set_visitText(cursor.getString(cursor.getColumnIndex(COLUMN_VISITTEXT)));
			visitRecord.set_receivedDate(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVEDDATE)));
			visitRecord.set_visitDate(cursor.getString(cursor.getColumnIndex(COLUMN_VISITDATE)));
			cursor.close();
		}

		db.close();
		return visitRecord.get_visitText();
	}

	public Visit findVisitById(int id) {
		String query = "Select * FROM " + TABLE_VISITS + " WHERE " + COLUMN_ID + " =  \"" + Integer.toString(id) + "\"";

		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(query, null);

		Visit visitRecord = new Visit();

		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			visitRecord.setID(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
			visitRecord.set_visitText(cursor.getString(cursor.getColumnIndex(COLUMN_VISITTEXT)));
			visitRecord.set_receivedDate(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVEDDATE)));
			visitRecord.set_visitDate(cursor.getString(cursor.getColumnIndex(COLUMN_VISITDATE)));
			cursor.close();
		}

		db.close();
		return visitRecord;
	}

    public ArrayList<Visit> getAllVisits(){

        String query = "Select * FROM " + TABLE_VISITS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Visit> resultList = new ArrayList<Visit>();
        Visit visitRecord = new Visit();

        if (cursor.moveToFirst()) {
            do {
                try {
                    visitRecord.setID(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    visitRecord.set_visitText(cursor.getString(cursor.getColumnIndex(COLUMN_VISITTEXT)));
                    visitRecord.set_receivedDate(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVEDDATE)));
                    visitRecord.set_visitDate(cursor.getString(cursor.getColumnIndex(COLUMN_VISITDATE)));
                    resultList.add(visitRecord);
                } catch (Exception e) {
                    Log.e("SQLLite getVisit Error", "Error " + e.toString());
                }
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return resultList;
    }

}