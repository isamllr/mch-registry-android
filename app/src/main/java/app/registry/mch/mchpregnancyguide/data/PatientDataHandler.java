package app.registry.mch.mchpregnancyguide.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Isa on 18.07.2014.
 */
public class PatientDataHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "registry.db";
    private static final String TABLE_PATIENT = "patient";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PATIENTID = "patientid";
    public static final String COLUMN_REGID = "reg";
    public static final String COLUMN_MOBILENUMBER = "mobilenumber";
    public static final String COLUMN_PATIENTNAME = "patientname";
    public static final String COLUMN_EXPECTEDDELIVERY = "expecteddelivery";
    public static final String COLUMN_FACILITYNAME = "facilityname";
    public static final String COLUMN_FACILITYPHONENUMBER = "facilityphonenumber";

    public PatientDataHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PATIENT_TABLE =
                "CREATE TABLE " + TABLE_PATIENT + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY,"
                        + COLUMN_PATIENTID + " INTEGER,"
                        + COLUMN_REGID + " TEXT"
                        + COLUMN_MOBILENUMBER + " TEXT"
                        + COLUMN_PATIENTNAME + " TEXT"
                        + COLUMN_EXPECTEDDELIVERY + " TEXT"
                        + COLUMN_FACILITYNAME + " TEXT"
                        + COLUMN_FACILITYPHONENUMBER + " TEXT"
                        + ")";
        db.execSQL(CREATE_PATIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
        onCreate(db);
    }

    public void addRecommendation(Patient patient) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENTID,patient.get_patientID());
        values.put(COLUMN_REGID, patient.get_regID());
        values.put(COLUMN_MOBILENUMBER, patient.get_mobileNumber());
        values.put(COLUMN_PATIENTNAME, patient.get_patientName());
        values.put(COLUMN_EXPECTEDDELIVERY, patient.get_expectedDelivery());
        values.put(COLUMN_FACILITYNAME, patient.get_facilityName());
        values.put(COLUMN_FACILITYPHONENUMBER, patient.get_facilityPhoneNumber());


        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PATIENT, null, values);
        db.close();
    }

    public Patient findPatient() {
        String query = "Select * FROM " + TABLE_PATIENT + " WHERE " + COLUMN_PATIENTID + " =  1";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Patient patient = new Patient();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            patient.setID(Integer.parseInt(cursor.getString(0)));
            patient.set_patientID(Integer.parseInt(cursor.getString(1)));
            patient.set_regID(cursor.getString(2));
            patient.set_mobileNumber(cursor.getString(3));
            patient.set_patientName(cursor.getString(4));
            patient.set_expectedDelivery(cursor.getString(5));
            patient.set_facilityName(cursor.getString(6));
            patient.set_facilityPhoneNumber(cursor.getString(7));
            cursor.close();
        } else {
            patient = null;
        }
        db.close();

        return patient;
    }

    public boolean deleteRecommendation(int recommendationDay) {


        boolean result = false;
        /**TODO

        String query = "Select * FROM " + TABLE_RECOMMENDATIONS + " WHERE " + COLUMN_RECOMMENDATIONDAY + " =  \"" + Integer.toString(recommendationDay) + "\"";

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
        db.close();*/
        return result;
    }
}

