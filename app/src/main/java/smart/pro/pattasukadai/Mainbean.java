package smart.pro.pattasukadai;

import java.io.Serializable;
import java.util.ArrayList;

import smart.pro.pattasukadai.invoice.Particularbean;

public class Mainbean implements Serializable {

    public static final String TABLE_NAME = "patasu_main";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DB_ID = "db_id";
    public static final String COLUMN_SELLERNAME = "sellername";
    public static final String COLUMN_SELLERADDRESS = "selleraddress";
    public static final String COLUMN_SELLERPHONE = "sellerphone";
    public static final String COLUMN_BUYERNAME = "buyername";
    public static final String COLUMN_BUYERPHONE = "buyerphone";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_PARTICULAR = "particular";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DB_ID + " db_id,"
                    + COLUMN_SELLERNAME + " sellername,"
                    + COLUMN_SELLERADDRESS + " selleraddress,"
                    + COLUMN_SELLERPHONE + " sellerphone,"
                    + COLUMN_BUYERNAME + " buyername,"
                    + COLUMN_BUYERPHONE + " buyerphone,"
                    + COLUMN_PARTICULAR + " particular,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    public int id;
    public String dbid;
    public String sellername;
    public String selleraddress;
    public String sellerphone;
    public String buyername;
    public String buyerphone;
    public String timestamp;
    public ArrayList<Particularbean> particularbeans;

    public Mainbean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDbid() {
        return dbid;
    }

    public void setDbid(String dbid) {
        this.dbid = dbid;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public String getSelleraddress() {
        return selleraddress;
    }

    public void setSelleraddress(String selleraddress) {
        this.selleraddress = selleraddress;
    }

    public String getSellerphone() {
        return sellerphone;
    }

    public void setSellerphone(String sellerphone) {
        this.sellerphone = sellerphone;
    }

    public String getBuyername() {
        return buyername;
    }

    public void setBuyername(String buyername) {
        this.buyername = buyername;
    }

    public String getBuyerphone() {
        return buyerphone;
    }

    public void setBuyerphone(String buyerphone) {
        this.buyerphone = buyerphone;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<Particularbean> getParticularbeans() {
        return particularbeans;
    }

    public void setParticularbeans(ArrayList<Particularbean> particularbeans) {
        this.particularbeans = particularbeans;
    }
}