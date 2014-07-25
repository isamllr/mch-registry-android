package com.mch.registry.ccs.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

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

    public void addRecommendation(Recommendation recommendation) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_RECOMMENDATIONTEXT,recommendation.get_recommendationText());
        values.put(COLUMN_RECOMMENDATIONDAY, recommendation.get_recommendationDay());
        values.put(COLUMN_RECEIVEDDATE, recommendation.get_receivedDate());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_RECOMMENDATIONS, null, values);
        db.close();
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
