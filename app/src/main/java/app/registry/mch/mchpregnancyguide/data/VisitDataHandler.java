package app.registry.mch.mchpregnancyguide.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Isa on 18.07.2014.
 */
public class VisitDataHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "registry.db";
    private static final String TABLE_VISITS = "visits";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VISITTEXT = "visittext";
    public static final String COLUMN_VISITDAY = "visitday";
    public static final String COLUMN_VISITFACILITY = "visitfacility";

    public VisitDataHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VISITS_TABLE =
                "CREATE TABLE " + TABLE_VISITS + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY,"
                        + COLUMN_VISITTEXT + " TEXT,"
                        + COLUMN_VISITDAY + " TEXT"
                        + COLUMN_VISITFACILITY + " TEXT,"
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
        values.put(COLUMN_VISITDAY, visit.get_visitDay());
        values.put(COLUMN_VISITTEXT, visit.get_visitFacility());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_VISITS, null, values);
        db.close();
    }

    public Visit findRecommendation(int visitDay) {
        String query = "Select * FROM " + TABLE_VISITS + " WHERE " + COLUMN_VISITDAY + " =  \"" + Integer.toString(visitDay) + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Visit visit = new Visit();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            visit.setID(Integer.parseInt(cursor.getString(0)));
            visit.set_visitText(cursor.getString(1));
            visit.set_visitDay(cursor.getString(2));
            visit.set_visitFacility(cursor.getString(3));
            cursor.close();
        } else {
            visit = null;
        }
        db.close();
        return visit;
    }

    public boolean deleteVisit(int visitDay) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_VISITS + " WHERE " + COLUMN_VISITDAY + " =  \"" + Integer.toString(visitDay) + "\"";

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

}