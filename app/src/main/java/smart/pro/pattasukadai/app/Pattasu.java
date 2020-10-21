package smart.pro.pattasukadai.app;

import java.io.Serializable;
import java.util.ArrayList;

import smart.pro.pattasukadai.StoreMainbean;
import smart.pro.pattasukadai.invoice.Particularbean;

public class Pattasu implements Serializable {

    public static final String TABLE_NAME = "pattasu4";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ITEMS = "items";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_NO = "noVal";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " title,"
                    + COLUMN_ITEMS + " items,"
                    + COLUMN_PRICE + " price,"
                    + COLUMN_NO + " noVal"
                    + ")";
    public  int id;
    public  String title;
    public  String items;
    public  String price;
    public  String no;


    public Pattasu() {
    }

    public Pattasu(int id, String title, String items, String price, String no) {
        this.id = id;
        this.title = title;
        this.items = items;
        this.price = price;
        this.no = no;
    }

    public Pattasu(String title, String items, String price, String no) {
        this.title = title;
        this.items = items;
        this.price = price;
        this.no = no;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}