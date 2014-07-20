package app.registry.mch.mchpregnancyguide.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Isa on 18.07.2014.
 */
public class RecommendationDataHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "registry.db";
    private static final String TABLE_RECOMMENDATIONS = "recommendations";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RECOMMENDATIONTEXT = "recomendationtext";
    public static final String COLUMN_RECOMMENDATIONDAY = "day";

    public RecommendationDataHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECOMMENDATIONS_TABLE =
                "CREATE TABLE " + TABLE_RECOMMENDATIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_RECOMMENDATIONTEXT + " TEXT,"
                + COLUMN_RECOMMENDATIONDAY + " INTEGER"
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

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_RECOMMENDATIONS, null, values);
        db.close();
    }

    public Recommendation findRecommendation(int recommendationDay) {
        String query = "Select * FROM " + TABLE_RECOMMENDATIONS + " WHERE " + COLUMN_RECOMMENDATIONDAY + " =  \"" + Integer.toString(recommendationDay) + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Recommendation recommendation = new Recommendation();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            recommendation.setID(Integer.parseInt(cursor.getString(0)));
            recommendation.set_recommendationText(cursor.getString(1));
            recommendation.set_recommendationDay(Integer.parseInt(cursor.getString(2)));
            cursor.close();
        } else {
            recommendation = null;
        }
        db.close();
        //TODO: return multiple rows
        //but currently not really needed
        return recommendation;
    }

    public boolean deleteRecommendation(int recommendationDay) {

        boolean result = false;

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
        db.close();
        return result;
    }

}
