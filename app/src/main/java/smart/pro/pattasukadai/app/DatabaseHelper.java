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
    private static final String DATABASE_NAME = "Genius_db2";


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
        values.put(Mainbean.COLUMN_SELLERGSTNO, mainbean.getSellergstNo());
        values.put(Mainbean.COLUMN_SELLERBILLNO, mainbean.getSellerbillNo());
        values.put(Mainbean.COLUMN_BUYERNAME, mainbean.getBuyername());
        values.put(Mainbean.COLUMN_BUYERADDRESS, mainbean.getBuyeraddress());
        values.put(Mainbean.COLUMN_BUYERPHONE, mainbean.getBuyerphone());
        values.put(Mainbean.COLUMN_BUYERGSTNO, mainbean.getBuyergstNo());
        values.put(Mainbean.COLUMN_CGST, mainbean.getCgst());
        values.put(Mainbean.COLUMN_SGST, mainbean.getSgst());
        values.put(Mainbean.COLUMN_IGST, mainbean.getIgst());
        values.put(Mainbean.COLUMN_BANKNAME, mainbean.getBankname());
        values.put(Mainbean.COLUMN_ACCOUNTNO, mainbean.getAccountNo());
        values.put(Mainbean.COLUMN_IFCNO, mainbean.getIfcno());
        values.put(Mainbean.COLUMN_PREVIOUS, mainbean.getPrevious());
        values.put(Mainbean.COLUMN_PAKAGECOST, mainbean.getPakagecost());
        values.put(Mainbean.COLUMN_BILLMODE, mainbean.getBillmode());
        values.put(Mainbean.COLUMN_CUSTOMER_ID, mainbean.getCustomerid());
        values.put(Mainbean.COLUMN_HOLDER_NAME, mainbean.getHoldername());
        values.put(Mainbean.COLUMN_INCLUDE_GST, mainbean.getIncludegst());
        values.put(Mainbean.COLUMN_TIMESTAMP, mainbean.getTimestamp());

        values.put(Mainbean.COLUMN_PARTICULAR, new Gson().toJson(mainbean.getParticularbeans()));

        // insert row
        long id = db.insert(Mainbean.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Mainbean getMainbean(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Mainbean.TABLE_NAME,
                new String[]{Mainbean.COLUMN_ID,
                        Mainbean.COLUMN_SELLERNAME,
                        Mainbean.COLUMN_SELLERADDRESS,
                        Mainbean.COLUMN_SELLERPHONE,
                        Mainbean.COLUMN_SELLERGSTNO,
                        Mainbean.COLUMN_SELLERBILLNO,
                        Mainbean.COLUMN_BUYERNAME,
                        Mainbean.COLUMN_BUYERADDRESS,
                        Mainbean.COLUMN_BUYERPHONE,
                        Mainbean.COLUMN_BUYERGSTNO,
                        Mainbean.COLUMN_CGST,
                        Mainbean.COLUMN_SGST,
                        Mainbean.COLUMN_IGST,
                        Mainbean.COLUMN_BANKNAME,
                        Mainbean.COLUMN_ACCOUNTNO,
                        Mainbean.COLUMN_IFCNO,
                        Mainbean.COLUMN_PREVIOUS,
                        Mainbean.COLUMN_PAKAGECOST,
                        Mainbean.COLUMN_PARTICULAR,
                        Mainbean.COLUMN_CUSTOMER_ID,
                        Mainbean.COLUMN_TIMESTAMP},
                Mainbean.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare Mainbean object
        String partiStr = cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_PARTICULAR));
        ArrayList<Particularbean> particularbeans = (ArrayList<Particularbean>) new Gson().fromJson(partiStr, List.class);
        Mainbean mainbean = new Mainbean(
                cursor.getInt(cursor.getColumnIndex(Mainbean.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERNAME)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERADDRESS)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERPHONE)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERGSTNO)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERBILLNO)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERNAME)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERADDRESS)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERPHONE)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERGSTNO)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_CGST)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SGST)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_IGST)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BANKNAME)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_ACCOUNTNO)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_IFCNO)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_PREVIOUS)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_PAKAGECOST)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BILLMODE)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_HOLDER_NAME)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_TIMESTAMP)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_CUSTOMER_ID)),
                cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_INCLUDE_GST)),
                particularbeans
        );

        // close the db connection
        cursor.close();

        return mainbean;
    }

    public List<Mainbean> getLastBillNo() {
        List<Mainbean> Mainbeans = new ArrayList<>();

        // Select All Query

        String selectQuery = "SELECT  * FROM " + Mainbean.TABLE_NAME + " ORDER BY " + Mainbean.COLUMN_SELLERBILLNO + " DESC LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Mainbean mainbean = new Mainbean();
                mainbean.setSellerbillNo(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERBILLNO)));
                Mainbeans.add(mainbean);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return Mainbeans list
        return Mainbeans;
    }


    public List<Mainbean> getAllMainbeans(String mode) {
        List<Mainbean> Mainbeans = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Mainbean.TABLE_NAME + " WHERE " + Mainbean.COLUMN_BILLMODE + " = '" + mode + "' ORDER BY " +
                Mainbean.COLUMN_SELLERBILLNO + " DESC";
        if (mode.equals("all")) {
            selectQuery = "SELECT  * FROM " + Mainbean.TABLE_NAME + " ORDER BY " +
                    Mainbean.COLUMN_SELLERBILLNO + " DESC";
        }
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
                mainbean.setSellername(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERNAME)));
                mainbean.setSelleraddress(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERADDRESS)));
                mainbean.setSellerphone(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERPHONE)));
                mainbean.setSellergstNo(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERGSTNO)));
                mainbean.setSellerbillNo(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERBILLNO)));
                mainbean.setBuyername(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERNAME)));
                mainbean.setBuyeraddress(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERADDRESS)));
                mainbean.setBuyerphone(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERPHONE)));
                mainbean.setBuyergstNo(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERGSTNO)));
                mainbean.setCgst(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_CGST)));
                mainbean.setSgst(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SGST)));
                mainbean.setIgst(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_IGST)));
                mainbean.setBankname(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BANKNAME)));
                mainbean.setAccountNo(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_ACCOUNTNO)));
                mainbean.setIfcno(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_IFCNO)));
                mainbean.setPrevious(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_PREVIOUS)));
                mainbean.setPakagecost(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_PAKAGECOST)));
                mainbean.setTimestamp(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_TIMESTAMP)));
                mainbean.setParticularbeans(particularbeans);
                mainbean.setBillmode(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BILLMODE)));
                mainbean.setCustomerid(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_CUSTOMER_ID)));
                mainbean.setIncludegst(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_INCLUDE_GST)));
                mainbean.setHoldername(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_HOLDER_NAME)));

                Mainbeans.add(mainbean);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return Mainbeans list
        return Mainbeans;
    }

    public String getUniqueSeller(String name, String contact) {

        String customerId = "";
        // Select All Query

        String selectQuery = "SELECT  * FROM " + Mainbean.TABLE_NAME + " WHERE " + Mainbean.COLUMN_BUYERPHONE + " ='" + contact + "' LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    customerId = cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_CUSTOMER_ID));
                } catch (Exception e) {
                }
                break;
            } while (cursor.moveToNext());
        }
        Log.e("xxxxxxx", customerId);
        if (customerId == null || customerId.length() <= 0) {
            selectQuery = "SELECT  * FROM " + Mainbean.TABLE_NAME + " WHERE " + Mainbean.COLUMN_BUYERNAME + " ='" + name + "' LIMIT 1";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    try {
                        customerId = cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_CUSTOMER_ID));
                    } catch (Exception e) {
                    }
                    break;
                } while (cursor.moveToNext());
            }
        }
        Log.e("xxxxxxx", customerId);

        if (customerId == null || customerId.length() <= 0) {
            selectQuery = "SELECT  * FROM " + Mainbean.TABLE_NAME + " GROUP BY " + Mainbean.COLUMN_CUSTOMER_ID;
            cursor = db.rawQuery(selectQuery, null);
            ArrayList<Mainbean> mainList = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    mainList.add(new Mainbean());
                } while (cursor.moveToNext());
            }
            customerId = AppConfig.intToString(mainList.size() + 1, 4);
        }
        Log.e("xxxxxxx", customerId);

        // close db connection
        db.close();

        // return Mainbeans list
        return customerId;
    }


    public List<Mainbean> getAllSellerMainbeans() {
        List<Mainbean> Mainbeans = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Mainbean.TABLE_NAME + " GROUP BY " + Mainbean.COLUMN_HOLDER_NAME;

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
                mainbean.setSellergstNo(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERGSTNO)));
                mainbean.setSellerbillNo(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERBILLNO)));
                mainbean.setBuyername(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERNAME)));
                mainbean.setBuyeraddress(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERADDRESS)));
                mainbean.setBuyerphone(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERPHONE)));
                mainbean.setBuyergstNo(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERGSTNO)));
                mainbean.setCgst(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_CGST)));
                mainbean.setSgst(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SGST)));
                mainbean.setIgst(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_IGST)));
                mainbean.setBankname(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BANKNAME)));
                mainbean.setAccountNo(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_ACCOUNTNO)));
                mainbean.setIfcno(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_IFCNO)));
                mainbean.setPrevious(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_PREVIOUS)));
                mainbean.setPakagecost(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_PAKAGECOST)));
                mainbean.setHoldername(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_HOLDER_NAME)));
                mainbean.setBillmode(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BILLMODE)));
                mainbean.setCustomerid(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_CUSTOMER_ID)));
                mainbean.setIncludegst(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_INCLUDE_GST)));
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
        String selectQuery = "SELECT  * FROM " + Mainbean.TABLE_NAME + " GROUP BY " +
                Mainbean.COLUMN_CUSTOMER_ID;

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
                mainbean.setSellergstNo(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERGSTNO)));
                mainbean.setSellerbillNo(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SELLERBILLNO)));
                mainbean.setBuyername(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERNAME)));
                mainbean.setBuyeraddress(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERADDRESS)));
                mainbean.setBuyerphone(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERPHONE)));
                mainbean.setBuyergstNo(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BUYERGSTNO)));
                mainbean.setCgst(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_CGST)));
                mainbean.setSgst(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_SGST)));
                mainbean.setIgst(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_IGST)));
                mainbean.setBankname(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BANKNAME)));
                mainbean.setAccountNo(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_ACCOUNTNO)));
                mainbean.setIfcno(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_IFCNO)));
                mainbean.setPrevious(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_PREVIOUS)));
                mainbean.setPakagecost(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_PAKAGECOST)));
                mainbean.setBillmode(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_BILLMODE)));
                mainbean.setHoldername(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_HOLDER_NAME)));
                mainbean.setCustomerid(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_CUSTOMER_ID)));
                mainbean.setIncludegst(cursor.getString(cursor.getColumnIndex(Mainbean.COLUMN_INCLUDE_GST)));
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
        values.put(Mainbean.COLUMN_SELLERGSTNO, mainbean.getSellergstNo());
        values.put(Mainbean.COLUMN_SELLERBILLNO, mainbean.getSellerbillNo());
        values.put(Mainbean.COLUMN_BUYERNAME, mainbean.getBuyername());
        values.put(Mainbean.COLUMN_BUYERADDRESS, mainbean.getBuyeraddress());
        values.put(Mainbean.COLUMN_BUYERPHONE, mainbean.getBuyerphone());
        values.put(Mainbean.COLUMN_BUYERGSTNO, mainbean.getBuyergstNo());
        values.put(Mainbean.COLUMN_CGST, mainbean.getCgst());
        values.put(Mainbean.COLUMN_SGST, mainbean.getSgst());
        values.put(Mainbean.COLUMN_IGST, mainbean.getIgst());
        values.put(Mainbean.COLUMN_BANKNAME, mainbean.getBankname());
        values.put(Mainbean.COLUMN_ACCOUNTNO, mainbean.getAccountNo());
        values.put(Mainbean.COLUMN_IFCNO, mainbean.getIfcno());
        values.put(Mainbean.COLUMN_PREVIOUS, mainbean.getPrevious());
        values.put(Mainbean.COLUMN_PAKAGECOST, mainbean.getPakagecost());
        values.put(Mainbean.COLUMN_BILLMODE, mainbean.getBillmode());
        values.put(Mainbean.COLUMN_CUSTOMER_ID, mainbean.getCustomerid());
        values.put(Mainbean.COLUMN_HOLDER_NAME, mainbean.getHoldername());
        values.put(Mainbean.COLUMN_INCLUDE_GST, mainbean.getIncludegst());

        values.put(Mainbean.COLUMN_PARTICULAR, new Gson().toJson(mainbean.getParticularbeans()));

        // updating row
        return db.update(Mainbean.TABLE_NAME, values, Mainbean.COLUMN_SELLERBILLNO + " = ?",
                new String[]{String.valueOf(mainbean.getSellerbillNo())});
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
