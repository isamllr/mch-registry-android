package com.mch.registry.ccs.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Isa on 18.07.2014.
 */
public class PatientDataHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "registry.db";
    private static final String TABLE_PATIENT = "patient";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PATIENTID = "_patientID";
    public static final String COLUMN_MESSAGEID = "_messageID";
    public static final String COLUMN_MOBILENUMBER = "_mobileNumber";
    public static final String COLUMN_PATIENTNAME = "_patientName";
    public static final String COLUMN_EXPECTEDDELIVERY = "_expectedDelivery";
    public static final String COLUMN_FACILITYNAME = "_facilityName";
    public static final String COLUMN_FACILITYPHONENUMBER = "_facilityPhoneNumber";
    public static final String COLUMN_REGID = "_regID";
	public static final String COLUMN_ACCOUNT = "_account";
	public static final String COLUMN_PASSWORD = "_password";

    public PatientDataHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PATIENT_TABLE =
                "CREATE TABLE " + TABLE_PATIENT + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY,"
                        + COLUMN_PATIENTID + " INTEGER,"
                        + COLUMN_MESSAGEID + " INTEGER,"
                        + COLUMN_MOBILENUMBER + " TEXT,"
                        + COLUMN_PATIENTNAME + " TEXT,"
                        + COLUMN_EXPECTEDDELIVERY + " DATETIME,"
                        + COLUMN_FACILITYNAME + " TEXT,"
                        + COLUMN_FACILITYPHONENUMBER + " TEXT,"
                        + COLUMN_REGID + " TEXT,"
		                + COLUMN_ACCOUNT + " TEXT,"
		                + COLUMN_PASSWORD + " TEXT"
                        + ")";
        db.execSQL(CREATE_PATIENT_TABLE);

	    String INSERT_PATIENT_TABLE =
			    "INSERT INTO " + TABLE_PATIENT + "("
					    + COLUMN_ID + ", "
					    + COLUMN_PATIENTID + ", "
					    + COLUMN_MESSAGEID + ", "
					    + COLUMN_MOBILENUMBER + ", "
					    + COLUMN_PATIENTNAME + ", "
					    + COLUMN_EXPECTEDDELIVERY + ", "
					    + COLUMN_FACILITYNAME + ", "
					    + COLUMN_FACILITYPHONENUMBER + ", "
					    + COLUMN_REGID + ", "
					    + COLUMN_ACCOUNT + ", "
					    + COLUMN_PASSWORD
						+ ")VALUES(null, 1, 1, '', '', '2000-01-01', '', '', '', '', '');";
	    db.execSQL(INSERT_PATIENT_TABLE);

	    Log.i("Pregnancy Guide", "PatientDB created & patient inserted");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
        onCreate(db);
    }

    private void addPatient(Patient patient) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENTID,patient.get_patientID());
        values.put(COLUMN_MESSAGEID, patient.get_latestMessageID());
        values.put(COLUMN_MOBILENUMBER, patient.get_mobileNumber());
        values.put(COLUMN_PATIENTNAME, patient.get_patientName());
        values.put(COLUMN_EXPECTEDDELIVERY, patient.get_expectedDelivery());
        values.put(COLUMN_FACILITYNAME, patient.get_facilityName());
        values.put(COLUMN_FACILITYPHONENUMBER, patient.get_facilityPhoneNumber());
        values.put(COLUMN_REGID, patient.get_regID());
	    values.put(COLUMN_ACCOUNT, patient.get_account());
	    values.put(COLUMN_PASSWORD, patient.get_password());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PATIENT, null, values);
        db.close();
	    Log.i("Pregnancy Guide", "Patient added");
    }

    public Patient getPatient() {
        String query = "Select * FROM " + TABLE_PATIENT;
	    Patient patient = new Patient();
		try{
	        SQLiteDatabase db = this.getWritableDatabase();
	        Cursor cursor = db.rawQuery(query, null);

	        if (cursor.moveToFirst()) {
		        cursor.moveToFirst();
		        patient.setID(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
		        patient.set_patientID(cursor.getInt(cursor.getColumnIndex(COLUMN_PATIENTID)));
		        patient.set_regID(cursor.getString(cursor.getColumnIndex(COLUMN_REGID)));
		        patient.set_latestMessageID(cursor.getInt(cursor.getColumnIndex(COLUMN_MESSAGEID)));
		        patient.set_mobileNumber(cursor.getString(cursor.getColumnIndex(COLUMN_MOBILENUMBER)));
		        patient.set_patientName(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENTNAME)));
		        patient.set_expectedDelivery(cursor.getString(cursor.getColumnIndex(COLUMN_EXPECTEDDELIVERY)));
		        patient.set_facilityName(cursor.getString(cursor.getColumnIndex(COLUMN_FACILITYNAME)));
		        patient.set_facilityPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_FACILITYPHONENUMBER)));
		        patient.set_account(cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT)));
		        patient.set_password(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
		        cursor.close();
	        }

	        db.close();
    }catch(Exception e){
			Log.e("DB Error", e.getMessage());
	}
	    Log.i("Pregnancy Guide", "get Patient");
        return patient;
    }

    public boolean updateMobilePhoneNumber(String mobilePhoneNumber){

        boolean result = false;

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Patient patient = new Patient();
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_MOBILENUMBER,mobilePhoneNumber);
            db.update(TABLE_PATIENT, cv , "1=1", null);
            db.close();
            result = true;
        }catch (Exception e) {
            Log.e("DB Error", e.getMessage());
            result = false;
        }

	    Log.i("Pregnancy Guide", "update Patient Mobile Phone Number");
        return result;
    }

	public boolean updateAccount(String account){

		boolean result = false;

		try{
			SQLiteDatabase db = this.getWritableDatabase();
			Patient patient = new Patient();
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_ACCOUNT,account);
			db.update(TABLE_PATIENT, cv , "1=1", null);
			db.close();
			result = true;
		}catch (Exception e) {
			Log.e("DB Error", e.getMessage());
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	public boolean updatePassword(String password){

		boolean result = false;

		try{
			SQLiteDatabase db = this.getWritableDatabase();
			Patient patient = new Patient();
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_PASSWORD, password);
			db.update(TABLE_PATIENT, cv , "1=1", null);
			db.close();
			result = true;
		}catch (Exception e) {
			Log.e("DB Error", e.getMessage());
			e.printStackTrace();
			result = false;
		}

		return result;
	}

    public boolean updateRegId(String regID){

        boolean result = false;

        try{

            SQLiteDatabase db = this.getWritableDatabase();
            Patient patient = new Patient();

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_REGID,regID);
            db.update(TABLE_PATIENT, cv , "1=1", null);

            db.close();
            result = true;
        }catch (Exception e) {
            Log.e("DB Error", e.getMessage());
            result = false;
        }

        return result;
    }

    public int getNextMessageId(){

        boolean result = false;

	    int nextMessageId = this.createNextMessageID(this.getPatient().get_latestMessageID());

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_MESSAGEID, nextMessageId);
            db.update(TABLE_PATIENT, cv , "1=1", null);

            db.close();
        }catch (Exception e) {
            Log.e("DB Error", e.getMessage());
        }

        return nextMessageId;
    }

    private int createNextMessageID(int messageId){
		return messageId++;
    }
}

