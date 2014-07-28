package app.registry.mch.mchpregnancyguide.data;

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
    private static final String TABLE_PREGNANCY = "pregnancy";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PREGNANCYID = "_pregnancyID";
    public static final String COLUMN_MESSAGEID = "_messageID";
    public static final String COLUMN_MOBILENUMBER = "_mobileNumber";
    public static final String COLUMN_PATIENTSURNAME = "_patientSurName";
	public static final String COLUMN_PATIENTLASTNAME = "_patientLastName";
    public static final String COLUMN_EXPECTEDDELIVERY = "_expectedDelivery";
    public static final String COLUMN_FACILITYNAME = "_facilityName";
    public static final String COLUMN_FACILITYPHONENUMBER = "_facilityPhoneNumber";
    public static final String COLUMN_REGID = "_regID";

    public PatientDataHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PATIENT_TABLE =
                "CREATE TABLE " + TABLE_PREGNANCY + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY,"
                        + COLUMN_PREGNANCYID + " INTEGER,"
                        + COLUMN_MESSAGEID + " TEXT,"
                        + COLUMN_MOBILENUMBER + " TEXT,"
                        + COLUMN_PATIENTSURNAME + " TEXT,"
		                + COLUMN_PATIENTLASTNAME + " TEXT,"
                        + COLUMN_EXPECTEDDELIVERY + " DATETIME,"
                        + COLUMN_FACILITYNAME + " TEXT,"
                        + COLUMN_FACILITYPHONENUMBER + " TEXT,"
                        + COLUMN_MESSAGEID + " TEXT"
                        + ")";
        db.execSQL(CREATE_PATIENT_TABLE);

        Patient patient = new Patient("regID", 1, "mobileNumber", "patientSurName", "patientLastName", "1980-01-01", "facilityPhoneNumber", "facilityName", "messageId");
        this.addPatient(patient);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREGNANCY);
        onCreate(db);
    }

    private void addPatient(Patient patient) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PREGNANCYID,patient.get_pregnancyID());
        values.put(COLUMN_MESSAGEID, patient.get_latestMessageID());
        values.put(COLUMN_MOBILENUMBER, patient.get_mobileNumber());
        values.put(COLUMN_PATIENTSURNAME, patient.get_patientSurName());
	    values.put(COLUMN_PATIENTLASTNAME, patient.get_patientLastName());
        values.put(COLUMN_EXPECTEDDELIVERY, patient.get_expectedDelivery());
        values.put(COLUMN_FACILITYNAME, patient.get_facilityName());
        values.put(COLUMN_FACILITYPHONENUMBER, patient.get_facilityPhoneNumber());
        values.put(COLUMN_REGID, patient.get_regID());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PREGNANCY, null, values);
        db.close();
    }

    public Patient getPatient() {
        String query = "Select * FROM " + TABLE_PREGNANCY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Patient patient = new Patient();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            patient.set_id(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            patient.set_pregnancyID(cursor.getInt(cursor.getColumnIndex(COLUMN_PREGNANCYID)));
            patient.set_regID(cursor.getString(cursor.getColumnIndex(COLUMN_REGID)));
            patient.set_latestMessageID(cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGEID)));
            patient.set_mobileNumber(cursor.getString(cursor.getColumnIndex(COLUMN_MOBILENUMBER)));
            patient.set_patientSurName(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENTSURNAME)));
	        patient.set_patientLastName(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENTLASTNAME)));
            patient.set_expectedDelivery(cursor.getString(cursor.getColumnIndex(COLUMN_REGID)));
            patient.set_facilityName(cursor.getString(cursor.getColumnIndex(COLUMN_FACILITYNAME)));
            patient.set_facilityPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_FACILITYPHONENUMBER)));
            cursor.close();
        }

        db.close();

        return patient;
    }

    public boolean updateMobilePhoneNumber(String mobilePhoneNumber){

        boolean result = false;

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Patient patient = new Patient();
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_MOBILENUMBER,mobilePhoneNumber);
            db.update(TABLE_PREGNANCY, cv , "1=1", null);
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
            db.update(TABLE_PREGNANCY, cv , "1=1", null);

            db.close();
            result = true;
        }catch (Exception e) {
            Log.e("DB Error", e.getMessage());
            result = false;
        }

        return result;
    }

    public boolean updateMessageId(String messageID){

        boolean result = false;

        try{

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_MESSAGEID, messageID);
            db.update(TABLE_PREGNANCY, cv , "1=1", null);

            db.close();
            result = true;
        }catch (Exception e) {
            Log.e("DB Error", e.getMessage());
            result = false;
        }

        return result;
    }

    public String createNextMessageID(){
        ///TODO: Generate a messageid
        String messageID = "todo";
        this.updateMessageId(messageID);
        return messageID;
    }
}

