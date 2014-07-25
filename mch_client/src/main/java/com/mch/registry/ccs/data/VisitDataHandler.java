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
public class VisitDataHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "registry.db";
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISITS);
        onCreate(db);
    }

    public void addVisit(Visit visit) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_VISITTEXT,visit.get_visitText());
        values.put(COLUMN_RECEIVEDDATE, visit.get_receivedDate());
        values.put(COLUMN_VISITDATE, visit.get_visitDate());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_VISITS, null, values);
        db.close();
    }

    public Visit findVisit(int visitDate) {
        String query = "Select * FROM " + TABLE_VISITS + " WHERE " + COLUMN_VISITDATE + " =  \"" + Integer.toString(visitDate) + "\"";

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

    public boolean deleteVisit(int visitID) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_VISITS + " WHERE " + COLUMN_ID + " =  \"" + Integer.toString(visitID) + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Visit visit = new Visit();

        if (cursor.moveToFirst()) {
            visit.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_VISITS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(visit.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
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