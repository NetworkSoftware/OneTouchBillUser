package smart.pro.pattasukadai.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import smart.pro.pattasukadai.Mainbean;
import smart.pro.pattasukadai.invoice.Particularbean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "patasu_main2";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create Mainbeans table
        db.execSQL(Mainbean.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        // db.execSQL("ALTER TABLE "+Mainbean.TABLE_NAME+" ADD COLUMN "+Mainbean.COLUMN_INCLUDE_GST+" TEXT");
        db.execSQL("DROP TABLE IF EXISTS " + Mainbean.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }


    public long insertMainbean(Mainbean mainbean) {

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Mainbean.COLUMN_SELLERNAME, mainbean.getSellername());
        values.put(Mainbean.COLUMN_SELLERADDRESS, mainbean.getSelleraddress());
        values.put(Mainbean.COLUMN_SELLERPHONE, mainbean.getSellerphone());
        values.put(Mainbean.COLUMN_DB_ID, mainbean.getDbid());
        values.put(Mainbean.COLUMN_BUYERNAME, mainbean.getBuyername());
        values.put(Mainbean.COLUMN_BUYERPHONE, mainbean.getBuyerphone());
        values.put(Mainbean.COLUMN_TIMESTAMP, mainbean.getTimestamp());
        values.put(Mainbean.COLUMN_PARTICULAR, new Gson().toJson(mainbean.getParticularbeans()));

        // insert row
        long id = db.insert(Mainbean.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Mainbean getMainbean(String id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Mainbean.TABLE_NAME,
                new String[]{Mainbean.COLUMN_ID,
                        Mainbean.COLUMN_SELLERNAME,
                        Mainbean.COLUMN_SELLERADDRESS,
                        Mainbean.COLUMN_SELLERPHONE,
                        Mainbean.COLUMN_DB_ID,
                        Mainbean.COLUMN_BUYERNAME,
                        Mainbean.COLUMN_BUYERPHONE,
                        Mainbean.COLUMN_PARTICULAR,
                        Mainbean.COLUMN_TIMESTAMP},
                Mainbean.COLUMN_DB_ID + "=?",
                new String[]{id}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        String partiStr = cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_PARTICULAR));
        ArrayList<Particularbean> particularbeans = (ArrayList<Particularbean>) new Gson().fromJson(partiStr, List.class);
        Mainbean mainbean = new Mainbean();
        mainbean.setId(cursor.getInt(cursor.getColumnIndex(Mainbean.COLUMN_ID)));
        mainbean.setDbid(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_DB_ID)));
        mainbean.setSellername(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERNAME)));
        mainbean.setSelleraddress(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERADDRESS)));
        mainbean.setSellerphone(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERPHONE)));
        mainbean.setBuyername(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERNAME)));
        mainbean.setBuyerphone(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERPHONE)));
        mainbean.setTimestamp(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_TIMESTAMP)));
        mainbean.setParticularbeans(particularbeans);
        // close the db connection
        cursor.close();

        return mainbean;
    }

    public List<Mainbean> getLastBillNo() {
        List<Mainbean> Mainbeans = new ArrayList<>();

        // Select All Query

        String selectQuery = "SELECT  * FROM " + Mainbean.TABLE_NAME + " ORDER BY " + Mainbean.COLUMN_DB_ID + " DESC LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Mainbean mainbean = new Mainbean();
                mainbean.setDbid(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_DB_ID)));
                Mainbeans.add(mainbean);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return Mainbeans list
        return Mainbeans;
    }


    public List<Mainbean> getAllMainbeans() {
        List<Mainbean> Mainbeans = new ArrayList<>();


        String selectQuery = "SELECT  * FROM " + Mainbean.TABLE_NAME + " ORDER BY " + Mainbean.COLUMN_DB_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String partiStr = cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_PARTICULAR));
                ArrayList<Particularbean> particularbeans = new Gson().fromJson(partiStr, new TypeToken<List<Particularbean>>() {
                }.getType());
                Mainbean mainbean = new Mainbean();
                mainbean.setId(cursor.getInt(cursor.getColumnIndex(Mainbean.COLUMN_ID)));
                mainbean.setDbid(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_DB_ID)));
                mainbean.setSellername(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERNAME)));
                mainbean.setSelleraddress(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERADDRESS)));
                mainbean.setSellerphone(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERPHONE)));
                mainbean.setBuyername(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERNAME)));
                mainbean.setBuyerphone(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERPHONE)));
                mainbean.setTimestamp(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_TIMESTAMP)));
                mainbean.setParticularbeans(particularbeans);
                Mainbeans.add(mainbean);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return Mainbeans list
        return Mainbeans;
    }

    public List<Mainbean> getAllSellerMainbeans() {
        List<Mainbean> Mainbeans = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Mainbean.TABLE_NAME + " GROUP BY " + Mainbean.COLUMN_SELLERPHONE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Mainbean mainbean = new Mainbean();
                mainbean.setId(cursor.getInt(cursor.getColumnIndex(Mainbean.COLUMN_ID)));
                mainbean.setSellername(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERNAME)));
                mainbean.setSelleraddress(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERADDRESS)));
                mainbean.setSellerphone(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERPHONE)));
                mainbean.setBuyername(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERNAME)));
                mainbean.setBuyerphone(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERPHONE)));
                mainbean.setTimestamp(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_TIMESTAMP)));

                Mainbeans.add(mainbean);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return Mainbeans list
        return Mainbeans;
    }

    public List<Mainbean> getAllBuyerMainbeans() {
        List<Mainbean> Mainbeans = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Mainbean.TABLE_NAME + " GROUP BY " + Mainbean.COLUMN_BUYERPHONE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Mainbean mainbean = new Mainbean();
                mainbean.setId(cursor.getInt(cursor.getColumnIndex(Mainbean.COLUMN_ID)));
                mainbean.setSellername(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERNAME)));
                mainbean.setSelleraddress(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERADDRESS)));
                mainbean.setSellerphone(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERPHONE)));
                mainbean.setBuyername(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERNAME)));
                mainbean.setBuyerphone(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERPHONE)));
                mainbean.setTimestamp(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_TIMESTAMP)));

                Mainbeans.add(mainbean);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return Mainbeans list
        return Mainbeans;
    }

    public List<String> getAllcategoryMainbeans() {

        Set<String> strings = new HashSet<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Mainbean.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String partiStr = cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_PARTICULAR));
                ArrayList<Particularbean> particularbeans = new Gson().fromJson(partiStr, new TypeToken<List<Particularbean>>() {
                }.getType());
                for (int i = 0; i < particularbeans.size(); i++) {
                    strings.add(particularbeans.get(i).getParticular());
                }
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return Mainbeans list
        return new ArrayList<String>(strings);
    }

    public int getMainbeansCount() {
        String countQuery = "SELECT  * FROM " + Mainbean.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateMainbean(Mainbean mainbean) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Mainbean.COLUMN_SELLERNAME, mainbean.getSellername());
        values.put(Mainbean.COLUMN_SELLERADDRESS, mainbean.getSelleraddress());
        values.put(Mainbean.COLUMN_SELLERPHONE, mainbean.getSellerphone());
        values.put(Mainbean.COLUMN_BUYERNAME, mainbean.getBuyername());
        values.put(Mainbean.COLUMN_BUYERPHONE, mainbean.getBuyerphone());
        values.put(Mainbean.COLUMN_PARTICULAR, new Gson().toJson(mainbean.getParticularbeans()));

        // updating row
        return db.update(Mainbean.TABLE_NAME, values, Mainbean.COLUMN_DB_ID + " = ?",
                new String[]{String.valueOf(mainbean.getDbid())});
    }

    public void deleteMainbean(Mainbean mainbean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Mainbean.TABLE_NAME, Mainbean.COLUMN_ID + " = ?",
                new String[]{String.valueOf(mainbean.getId())});
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Mainbean.TABLE_NAME, "1",
                new String[]{});
        db.close();
    }
}
