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
public class PregnancyDataHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "registry.db";
    private static final String TABLE_PREGNANCY = "pregnancy";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PATIENTID = "_patientID";
    public static final String COLUMN_MESSAGEID = "_messageID";
    public static final String COLUMN_MOBILENUMBER = "_mobileNumber";
    public static final String COLUMN_PATIENTNAME = "_patientName";
    public static final String COLUMN_EXPECTEDDELIVERY = "_expectedDelivery";
    public static final String COLUMN_FACILITYNAME = "_facilityName";
    public static final String COLUMN_FACILITYPHONENUMBER = "_facilityPhoneNumber";
    public static final String COLUMN_REGID = "_regID";
	public static final String COLUMN_VERIFIED = "_verified";
	public static final String COLUMN_PROGRESS = "_loadingProgress";

    public PregnancyDataHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PATIENT_TABLE =
                "CREATE TABLE " + TABLE_PREGNANCY + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY,"
                        + COLUMN_PATIENTID + " INTEGER,"
                        + COLUMN_MESSAGEID + " INTEGER,"
                        + COLUMN_MOBILENUMBER + " TEXT,"
                        + COLUMN_PATIENTNAME + " TEXT,"
                        + COLUMN_EXPECTEDDELIVERY + " DATETIME,"
                        + COLUMN_FACILITYNAME + " TEXT,"
                        + COLUMN_FACILITYPHONENUMBER + " TEXT,"
                        + COLUMN_REGID + " TEXT,"
		                + COLUMN_VERIFIED + " INTEGER,"
		                + COLUMN_PROGRESS + " INTEGER"
                        + ")";
        db.execSQL(CREATE_PATIENT_TABLE);

	    String INSERT_PATIENT_TABLE =
			    "INSERT INTO " + TABLE_PREGNANCY + "("
					    + COLUMN_ID + ", "
					    + COLUMN_PATIENTID + ", "
					    + COLUMN_MESSAGEID + ", "
					    + COLUMN_MOBILENUMBER + ", "
					    + COLUMN_PATIENTNAME + ", "
					    + COLUMN_EXPECTEDDELIVERY + ", "
					    + COLUMN_FACILITYNAME + ", "
					    + COLUMN_FACILITYPHONENUMBER + ", "
					    + COLUMN_REGID + ", "
					    + COLUMN_VERIFIED + ", "
					    + COLUMN_PROGRESS
						+ ")VALUES(null, 1, 1, '+380xxxxxxxxx', '', '2000-01-01', '', '', '', 0, 0);";
	    db.execSQL(INSERT_PATIENT_TABLE);

	    Log.i("Pregnancy Guide", "PregnancyDB created & patient inserted");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREGNANCY);
        onCreate(db);
    }

    private void addPatient(Pregnancy pregnancy) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENTID, pregnancy.get_patientID());
        values.put(COLUMN_MESSAGEID, pregnancy.get_latestMessageID());
        values.put(COLUMN_MOBILENUMBER, pregnancy.get_mobileNumber());
        values.put(COLUMN_PATIENTNAME, pregnancy.get_patientName());
        values.put(COLUMN_EXPECTEDDELIVERY, pregnancy.get_expectedDelivery());
        values.put(COLUMN_FACILITYNAME, pregnancy.get_facilityName());
        values.put(COLUMN_FACILITYPHONENUMBER, pregnancy.get_facilityPhoneNumber());
        values.put(COLUMN_REGID, pregnancy.get_regID());
	    values.put(COLUMN_VERIFIED, pregnancy.get_isVerified());
	    values.put(COLUMN_PROGRESS, pregnancy.get_loadingProgress());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PREGNANCY, null, values);
        db.close();
	    Log.i("Pregnancy Guide", "Pregnancy added");
    }

    public Pregnancy getPregnancy(){
        String query = "Select * FROM " + TABLE_PREGNANCY;
	    Pregnancy pregnancy = new Pregnancy();
		try{
	        SQLiteDatabase db = this.getWritableDatabase();
	        Cursor cursor = db.rawQuery(query, null);

	        if (cursor.moveToFirst()) {
		        cursor.moveToFirst();
		        pregnancy.set_id(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
		        pregnancy.set_patientID(cursor.getInt(cursor.getColumnIndex(COLUMN_PATIENTID)));
		        pregnancy.set_regID(cursor.getString(cursor.getColumnIndex(COLUMN_REGID)));
		        pregnancy.set_latestMessageID(cursor.getInt(cursor.getColumnIndex(COLUMN_MESSAGEID)));
		        pregnancy.set_mobileNumber(cursor.getString(cursor.getColumnIndex(COLUMN_MOBILENUMBER)));
		        pregnancy.set_patientName(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENTNAME)));
		        pregnancy.set_expectedDelivery(cursor.getString(cursor.getColumnIndex(COLUMN_EXPECTEDDELIVERY)));
		        pregnancy.set_facilityName(cursor.getString(cursor.getColumnIndex(COLUMN_FACILITYNAME)));
		        pregnancy.set_facilityPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_FACILITYPHONENUMBER)));
		        pregnancy.set_isVerified(cursor.getInt(cursor.getColumnIndex(COLUMN_VERIFIED)));
		        pregnancy.set_loadingProgress(cursor.getInt(cursor.getColumnIndex(COLUMN_PROGRESS)));
		        cursor.close();
	        }

	        db.close();
    }catch(Exception e){
			Log.e("DB Error", e.getMessage());
	}
	    Log.i("Pregnancy Guide", "get Patient. Mobile number: " + pregnancy.get_mobileNumber() );
        return pregnancy;
    }

    public boolean updateMobilePhoneNumber(String mobilePhoneNumber){

        boolean result = false;

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Pregnancy pregnancy = new Pregnancy();
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_MOBILENUMBER,mobilePhoneNumber);
            db.update(TABLE_PREGNANCY, cv , "1=1", null);
            db.close();
            result = true;
        }catch (Exception e) {
            Log.e("DB Error", e.getMessage());
            result = false;
        }

	    Log.i("Pregnancy Guide", "update Patient Mobile Phone Number");
        return result;
    }

	public boolean setLoadingProgress(int progress){

		boolean result = false;

		try{

			SQLiteDatabase db = this.getWritableDatabase();
			Pregnancy pregnancy = new Pregnancy();

			ContentValues cv = new ContentValues();
			cv.put(COLUMN_PROGRESS,progress);
			db.update(TABLE_PREGNANCY, cv , "1=1", null);

			db.close();
			result = true;
		}catch (Exception e) {
			Log.e("DB Error", e.getMessage());
			result = false;
		}

		return result;
	}

    public boolean updateRegId(String regID){

        boolean result = false;

        try{

            SQLiteDatabase db = this.getWritableDatabase();
            Pregnancy pregnancy = new Pregnancy();

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_REGID,regID);
            db.update(TABLE_PREGNANCY, cv , "1=1", null);

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
	    PregnancyDataHandler pdh = new PregnancyDataHandler(null, null, null, 1);
		Pregnancy pregnancy = pdh.getPregnancy();
	    int nextMessageId = pregnancy.get_latestMessageID() + 1;

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_MESSAGEID, nextMessageId);
            db.update(TABLE_PREGNANCY, cv , "1=1", null);

            db.close();
        }catch (Exception e) {
            Log.e("DB Error", e.getMessage());
        }

        return nextMessageId;
    }

	public boolean setVerified(boolean isVerified) {
		boolean result = false;

		try{

			SQLiteDatabase db = this.getWritableDatabase();
			Pregnancy pregnancy = new Pregnancy();
			int verified = (isVerified?1:0);
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_VERIFIED, verified);
			db.update(TABLE_PREGNANCY, cv , "1=1", null);

			db.close();
			result = true;
		}catch (Exception e) {
			Log.e("DB Error", e.getMessage());
			result = false;
		}

		return result;
	}

	public boolean updateFacilityName(String pInfoFN) {

			boolean result = false;

			try{

				SQLiteDatabase db = this.getWritableDatabase();
				Pregnancy pregnancy = new Pregnancy();

				ContentValues cv = new ContentValues();
				cv.put(COLUMN_FACILITYNAME,pInfoFN);
				db.update(TABLE_PREGNANCY, cv , "1=1", null);

				db.close();
				result = true;
			}catch (Exception e) {
				Log.e("DB Error", e.getMessage());
				result = false;
			}

			return result;
		}

	public boolean updateFacilityPhone(String pInfoFP) {
		boolean result = false;

		try{

			SQLiteDatabase db = this.getWritableDatabase();
			Pregnancy pregnancy = new Pregnancy();

			ContentValues cv = new ContentValues();
			cv.put(COLUMN_FACILITYPHONENUMBER, pInfoFP);
			db.update(TABLE_PREGNANCY, cv , "1=1", null);

			db.close();
			result = true;
		}catch (Exception e) {
			Log.e("DB Error", e.getMessage());
			result = false;
		}

		return result;
	}

	public boolean updatePatientName(String pInfoPN) {
		boolean result = false;

		try{

			SQLiteDatabase db = this.getWritableDatabase();
			Pregnancy pregnancy = new Pregnancy();

			ContentValues cv = new ContentValues();
			cv.put(COLUMN_PATIENTNAME, pInfoPN);
			db.update(TABLE_PREGNANCY, cv , "1=1", null);

			db.close();
			result = true;
		}catch (Exception e) {
			Log.e("DB Error", e.getMessage());
			result = false;
		}

		return result;
	}

	public boolean updateExpectedDelivery(String pInfoED) {
		boolean result = false;

		try{

			SQLiteDatabase db = this.getWritableDatabase();
			Pregnancy pregnancy = new Pregnancy();

			ContentValues cv = new ContentValues();
			cv.put(COLUMN_EXPECTEDDELIVERY, pInfoED);
			db.update(TABLE_PREGNANCY, cv , "1=1", null);

			db.close();
			result = true;
		}catch (Exception e) {
			Log.e("DB Error", e.getMessage());
			result = false;
		}

		return result;
	}
}

