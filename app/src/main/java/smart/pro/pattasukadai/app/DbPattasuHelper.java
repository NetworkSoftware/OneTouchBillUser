package smart.pro.pattasukadai.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by ravi on 15/03/18.
 */

public class DbPattasuHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Pattasu_4";


    public DbPattasuHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create Mainbeans table
        db.execSQL(Pattasu.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        // db.execSQL("ALTER TABLE "+Pattasu.TABLE_NAME+" ADD COLUMN "+Pattasu.COLUMN_INCLUDE_GST+" TEXT");
        db.execSQL("DROP TABLE IF EXISTS " + Pattasu.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }


    public long insertPattasu(Pattasu pattasu) {

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Pattasu.COLUMN_NO, pattasu.getNo());
        values.put(Pattasu.COLUMN_ITEMS, pattasu.getItems());
        values.put(Pattasu.COLUMN_PRICE, pattasu.getPrice());
        values.put(Pattasu.COLUMN_TITLE, pattasu.getTitle());
        // insert row
        long id = db.insert(Pattasu.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Pattasu getPattasu(String no) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Pattasu.TABLE_NAME,
                new String[]{Pattasu.COLUMN_ID,
                        Pattasu.COLUMN_NO,
                        Pattasu.COLUMN_TITLE,
                        Pattasu.COLUMN_ITEMS,
                        Pattasu.COLUMN_PRICE},
                Pattasu.COLUMN_NO + "=?",
                new String[]{no}, null, null, null, null);
        try {
            if (cursor != null)
                cursor.moveToFirst();
            Pattasu pattasu = new Pattasu(cursor.getInt(cursor.getColumnIndex(Pattasu.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Pattasu.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(Pattasu.COLUMN_ITEMS)),
                    cursor.getString(cursor.getColumnIndex(Pattasu.COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndex(Pattasu.COLUMN_NO)));

            // close the db connection
            cursor.close();
            return pattasu;
        } catch (Exception e) {

        }
        return null;
    }

    public List<Pattasu> getAllPattasu() {
        List<Pattasu> pattasuList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Pattasu.TABLE_NAME + " ORDER BY " + Pattasu.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Pattasu pattasu = new Pattasu();
                pattasu.setId(cursor.getInt(cursor.getColumnIndex(Pattasu.COLUMN_ID)));
                pattasu.setItems(cursor.getString(cursor.getColumnIndex(Pattasu.COLUMN_ITEMS)));
                pattasu.setPrice(cursor.getString(cursor.getColumnIndex(Pattasu.COLUMN_PRICE)));
                pattasu.setTitle(cursor.getString(cursor.getColumnIndex(Pattasu.COLUMN_TITLE)));
                pattasu.setNo(cursor.getString(cursor.getColumnIndex(Pattasu.COLUMN_NO)));

                pattasuList.add(pattasu);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return Mainbeans list
        return pattasuList;
    }


    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Pattasu.TABLE_NAME, "1",
                new String[]{});
        db.close();
    }
}
