package com.mch.registry.ccs.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Isa on 18.07.2014.
 */
public class RecommendationDataHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "registry.db";
    private static final String TABLE_RECOMMENDATIONS = "recommendations";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RECOMMENDATIONTEXT = "_recomendationText";
    public static final String COLUMN_RECOMMENDATIONDAY = "_recommendationDay";
    public static final String COLUMN_RECEIVEDDATE = "_receivedDate";

    public RecommendationDataHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECOMMENDATIONS_TABLE =
                "CREATE TABLE " + TABLE_RECOMMENDATIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_RECOMMENDATIONTEXT + " TEXT,"
                + COLUMN_RECOMMENDATIONDAY + " INTEGER,"
                + COLUMN_RECEIVEDDATE + " DATETIME"
                + ")";
        db.execSQL(CREATE_RECOMMENDATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECOMMENDATIONS);
        onCreate(db);
    }

    public void addRecommendation(String recommendationText) {

	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss");
	    Calendar cal = Calendar.getInstance();

        ContentValues values = new ContentValues();
        values.put(COLUMN_RECOMMENDATIONTEXT,recommendationText);
        values.put(COLUMN_RECOMMENDATIONDAY, calculateRecommendationDay(cal.getTime()));
        values.put(COLUMN_RECEIVEDDATE, dateFormat.format(cal.getTime()).toString());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_RECOMMENDATIONS, null, values);
        db.close();
    }

	private int calculateRecommendationDay(Date today) {
		PatientDataHandler pdh = new PatientDataHandler(null, "Recommendation", null, 1);
		pdh.getPatient();
		Patient patient = new Patient();

		DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");

		String truncatedDateString1 = formatter.format(today);
		Date truncatedDate1 = null;
		String truncatedDateString2 = formatter.format(patient.get_expectedDelivery());
		Date truncatedDate2 = null;
		try {
			truncatedDate1 = formatter.parse(truncatedDateString1);
			truncatedDate2 = formatter.parse(truncatedDateString2);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long timeDifference = truncatedDate2.getTime()- truncatedDate1.getTime();
		long daysInBetween = timeDifference / (24*60*60*1000);
		
		return ((int) daysInBetween);
	}

	public ArrayList<Recommendation> getAllRecommendations(){

        String query = "Select * FROM " + TABLE_RECOMMENDATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Recommendation> resultList = new ArrayList<Recommendation>();
        Recommendation recommendationRecord = new Recommendation();

        if (cursor.moveToFirst()) {
            do {
                try {
                    recommendationRecord.setID(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    recommendationRecord.set_recommendationText(cursor.getString(cursor.getColumnIndex(COLUMN_RECOMMENDATIONTEXT)));
                    recommendationRecord.set_recommendationDay(cursor.getInt(cursor.getColumnIndex(COLUMN_RECOMMENDATIONDAY)));
                    recommendationRecord.set_receivedDate(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVEDDATE)));
                    resultList.add(recommendationRecord);
                } catch (Exception e) {
                    Log.e("SQLLite getRecommendation Error", "Error " + e.toString());
                }
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return resultList;
    }

    public boolean deleteRecommendation(int recommendationID) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_RECOMMENDATIONS + " WHERE " + COLUMN_ID + " =  \"" + Integer.toString(recommendationID) + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Recommendation recommendation = new Recommendation();

        if (cursor.moveToFirst()) {
            recommendation.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_RECOMMENDATIONS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(recommendation.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public Recommendation findRecommendation(int recommendationID) {
        String query = "Select * FROM " + TABLE_RECOMMENDATIONS + " WHERE " + COLUMN_ID + " =  \"" + Integer.toString(recommendationID) + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Recommendation recommendation = new Recommendation();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            recommendation.setID(Integer.parseInt(cursor.getString(0)));
            recommendation.set_recommendationText(cursor.getString(1));
            recommendation.set_recommendationDay(Integer.parseInt(cursor.getString(2)));
            recommendation.set_receivedDate(cursor.getString(3));
            cursor.close();
        } else {
            recommendation = null;
        }
        db.close();

        return recommendation;
    }

}
